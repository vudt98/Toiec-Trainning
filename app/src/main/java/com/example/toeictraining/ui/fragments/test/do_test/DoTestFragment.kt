package com.example.toeictraining.ui.fragments.test.do_test

import android.app.AlertDialog
import android.app.Application
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.toeictraining.R
import com.example.toeictraining.base.database.AppDatabase
import com.example.toeictraining.base.entity.Question
import com.example.toeictraining.base.enums.ExamLevel
import com.example.toeictraining.ui.fragments.test.Constant
import com.example.toeictraining.ui.fragments.test.score.ScoreTestFragment
import com.example.toeictraining.ui.main.MainActivity
import com.example.toeictraining.utils.DateUtil
import kotlinx.android.synthetic.main.dialog_do_test.*
import kotlinx.android.synthetic.main.do_test_fragment.*
import kotlinx.android.synthetic.main.toolbar.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

class DoTestFragment(
    private val part: Int,
    private val examLevel: ExamLevel
) : Fragment(), View.OnClickListener {

    companion object {
        val TAG = DoTestFragment::class.java.name
        const val THRESHOLD_SPEED = 60
    }

    private var tempPart = part
    private var secondTotalTime: Int = 0
    private lateinit var viewModel: DoTestViewModel
    private var mediaPlayer: MediaPlayer? = null
    private val questionsStatus = mutableListOf<QuestionStatus>()
    private var totalTime = 0
    private var timestamp: String = ""
    private var level: ExamLevel = examLevel
    private val listQuestionId = mutableListOf<Int>()
    private val questions = mutableListOf<Question>()
    private val tempQuestions = mutableListOf<Question>()
    private var countDownTimer: CountDownTimer? = null
    private fun setCountDownTimer(time: Int) {
        countDownTimer = object : CountDownTimer(time * 1000L, 1000L) {
            override fun onFinish() {
                (activity as MainActivity).openFragment(
                    ScoreTestFragment(questionsStatus, secondTotalTime, timestamp, tempPart),
                    false
                )
            }

            override fun onTick(millisUntilFinished: Long) {
                totalTime = (millisUntilFinished / 1000.0).roundToInt()
                (activity as MainActivity).setTitle(
                    DateUtil.secondsToStringTime(
                        (millisUntilFinished / 1000.0).roundToInt()
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        timestamp = SimpleDateFormat(getString(R.string.date_time_format), Locale.getDefault())
            .format(Calendar.getInstance().time)
        val application: Application = requireNotNull(activity).application
        val dataSource = AppDatabase.getInstance(application).questionDao()
        val viewModelFactory = DoTestViewModelFactory(dataSource, application, tempPart)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DoTestViewModel::class.java)
        return inflater.inflate(
            R.layout.do_test_fragment,
            container,
            false
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).showLoadingDialog()
        initViews()
        handleObservable()
    }

    private fun drawAnswer(answerView: TextView) {
        answerView.tag = true
        answerView.setBackgroundColor(
            ResourcesCompat.getColor(
                resources,
                R.color.colorPrimary,
                null
            )
        )
        answerView.setTextColor(Color.WHITE)
    }

    private fun removeAnswer(answerView: TextView) {
        answerView.tag = null
        answerView.setBackgroundColor(Color.WHITE)
        answerView.setTextColor(Color.BLACK)
    }

    private fun handleObservable() {
        viewModel.indexMain.observe(
            viewLifecycleOwner,
            Observer {
                scrollView2.scrollTo(0, 0)
                val question = questionsStatus[it]
                (activity as MainActivity).showLoadingDialog()
                //script
                text_script.visibility = View.GONE
                question.data.script?.let { script ->
                    text_script.visibility = View.VISIBLE
                    text_script.text = script
                }
                //content
                text_question_content.visibility = View.GONE
                question.data.content?.let { content ->
                    text_question_content.visibility = View.VISIBLE
                    text_question_content.text = content
                }
                //audio
                question.data.soundLink?.let {
                    mediaPlayer?.stop()
                    mediaPlayer = MediaPlayer()
                    mediaPlayer?.setAudioAttributes(
                        AudioAttributes.Builder().setContentType(
                            AudioAttributes.CONTENT_TYPE_MUSIC
                        ).build()
                    )
                    mediaPlayer?.setDataSource(context!!, Uri.parse(question.data.soundLink))
                    mediaPlayer?.prepare()
                    mediaPlayer?.setOnPreparedListener {
                        (activity as MainActivity).cancelLoadingDialog()
                        mediaPlayer?.start()
                    }
                }
                //image
                image_question.visibility = View.GONE
                question.data.imageLink?.let { link ->
                    image_question.visibility = View.VISIBLE
                    Glide.with(context!!)
                        .load(link)
                        .fitCenter()
                        .into(image_question)
                }
                //answer
                text_a.text = getString(R.string.a).plus(". ").plus(question.data.a)
                text_b.text = getString(R.string.b).plus(". ").plus(question.data.b)
                text_c.text = getString(R.string.c).plus(". ").plus(question.data.c)
                text_d.visibility = View.GONE
                question.data.d?.let { d ->
                    text_d.visibility = View.VISIBLE
                    text_d.text = getString(R.string.d).plus(". ").plus(d)
                }
                removeDrawAnswers()
                when (question.answer) {
                    getString(R.string.a) -> drawAnswer(text_a)
                    getString(R.string.b) -> drawAnswer(text_b)
                    getString(R.string.c) -> drawAnswer(text_c)
                    getString(R.string.d) -> drawAnswer(text_d)
                }
                /*khi có link sound, không cancelloadingdialog, đợi loading xong sound,
                cancelloadingdialog bên mediaPlayer*/
                if (question.data.soundLink == null) {
                    (activity as MainActivity).cancelLoadingDialog()
                }
            })

        viewModel.getQuestionsLiveData().observe(viewLifecycleOwner, Observer {
            questions.addAll(it)
            while (questions.size > Constant.COUNT_QUESTIONS_OF_PART[tempPart]) {
                questions.removeAt(questions.size - 1)
            }
            //check size của questions hiện tại với size thực tế mối part
            // đề phòng trường hợp thiếu câu hỏi
            if (questions.size == Constant.COUNT_QUESTIONS_OF_PART[tempPart]) {
                // đối với part = 8 (test full),
                // tạo một vòng lặp random cho 7 part
                if (part == 8) {
                    tempPart++
                    tempQuestions.addAll(questions)
                    questions.clear()
                    if (tempPart < 8) {
                        viewModel.getQuestions(
                            tempPart,
                            level,
                            Constant.COUNT_QUESTIONS_OF_PART[tempPart]
                        )
                    } else {
                        questions.addAll(tempQuestions)
                        startPrepareQuestions()
                    }
                } else {
                    startPrepareQuestions()
                }
            } else {
//                o	Khi chọn EASY thì chỉ query tất cả các câu EASY. NEW: Nếu hết câu EASY, thì query tiếp MEDIUM, rồi đến EASY cho các câu còn lại.
//                o	Khi chọn MEDIUM, query tất cả các câu MEDIUM. NEW: Nếu hết câu MEDIUM, thì query tiếp EASY, rồi đến HARD cho các câu còn lại.
//                o	Khi chọn đề HARD, query tất cả các câu HARD. Nếu hết câu HARD thì query tiếp MEDIUM, rồi đến EASY cho các câu còn lại.
                when (level) {
                    ExamLevel.EASY -> {
                        if (examLevel == ExamLevel.EASY) {
                            level = ExamLevel.MEDIUM
                        }
                        if (examLevel == ExamLevel.MEDIUM) {
                            level = ExamLevel.HARD
                        }
                    }
                    ExamLevel.MEDIUM -> {
                        if (examLevel == ExamLevel.EASY) {
                            level = ExamLevel.HARD
                        }
                        if (examLevel == ExamLevel.MEDIUM) {
                            level = ExamLevel.EASY
                        }
                        if (examLevel == ExamLevel.HARD) {
                            level = ExamLevel.EASY
                        }
                    }
                    ExamLevel.HARD -> {
                        if (examLevel == ExamLevel.HARD) {
                            level = ExamLevel.MEDIUM
                        }
                    }
                }
                viewModel.getQuestions(
                    tempPart,
                    level,
                    Constant.COUNT_QUESTIONS_OF_PART[tempPart] - questions.size
                )
            }
        })
    }

    private fun startPrepareQuestions() {
        prepareData(questions)
        recyclerViewQuestion.adapter?.notifyDataSetChanged()
        if (countDownTimer == null) {
            setCountDownTimer(secondTotalTime)
        }
        countDownTimer?.start()
    }

    private fun initViews() {
        (activity as MainActivity).setRightButtonText(getString(R.string.submit_test))
        configNavigationIcon()
        (activity as MainActivity).toolbar_button_right.setOnClickListener(this)
        text_a.setOnClickListener(this)
        text_b.setOnClickListener(this)
        text_c.setOnClickListener(this)
        text_d.setOnClickListener(this)
        setRecyclerQuestion()

        secondTotalTime = Constant.TIMES_PART[tempPart]
        //load data
        if (part == 8) {
            tempPart = 1
        }
        viewModel.getQuestions(tempPart, level, Constant.COUNT_QUESTIONS_OF_PART[tempPart])
    }

    private fun prepareData(questionsFromDB: List<Question>) {
        for (i in questionsFromDB.indices) {
            questionsStatus.add(
                QuestionStatus(
                    i + 1,
                    QuestionStatus.Status.NOT_DONE,
                    questionsFromDB[i],
                    ""
                )
            )
            listQuestionId.add(questionsFromDB[i].id)
        }
        questionsStatus[0].status = QuestionStatus.Status.MAIN
    }

    private fun setRecyclerQuestion() {
        recyclerViewQuestion.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false).apply {
                    scrollToPositionWithOffset(
                        0,
                        (Resources.getSystem().displayMetrics.widthPixels - (Resources.getSystem().displayMetrics.density * 60).toInt()) / 2
                    )
                }
            adapter = ListQuestionAdapter(activity as MainActivity, questionsStatus, viewModel)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.HORIZONTAL
                ).apply {
                    setDrawable(context?.getDrawable(R.drawable.divider_recyclerview_horizontal_9)!!)
                }
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                private var firstTimePoint = 0L
                private var lastTimePoint = 0L
                private var fp1 = 0

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> {
                            lastTimePoint = System.currentTimeMillis()
                            val fp2 =
                                (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            val lp2 =
                                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                            if ((fp2 - fp1) != 0) {
                                val speed = abs((lastTimePoint - firstTimePoint) / (fp2 - fp1))
                                if (speed < THRESHOLD_SPEED && fp2 == 0) {
                                    setMainPosition(0)
                                } else if (speed < THRESHOLD_SPEED && lp2 == (questionsStatus.size - 1)) {
                                    setMainPosition(questionsStatus.size - 1)
                                } else {
                                    val item =
                                        (layoutManager as LinearLayoutManager).findViewByPosition(
                                            abs(lp2 - fp2) / 2 + fp2
                                        )
                                    val textQuestion =
                                        (item as LinearLayout).getChildAt(0) as TextView
                                    val positionQuestion =
                                        textQuestion.text.toString().toInt() - 1 // question bắt đầu = 1, position bắt đầu = 0
                                    setMainPosition(positionQuestion)
                                }
                                adapter?.notifyDataSetChanged()
                            }
                        }

                        RecyclerView.SCROLL_STATE_DRAGGING -> {
                            firstTimePoint = System.currentTimeMillis()
                            fp1 =
                                (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        }
                    }
                }
            })
        }
    }

    private fun setMainPosition(pos: Int) {
        (activity as MainActivity).showLoadingDialog()
        viewModel.indexMain.value?.let {
            if (questionsStatus[it].answer.isNotBlank()) {
                questionsStatus[it].status =
                    QuestionStatus.Status.DONE // load lại status
            } else {
                questionsStatus[it].status =
                    QuestionStatus.Status.NOT_DONE // load lại status
            }
        }
        questionsStatus[pos].status = QuestionStatus.Status.MAIN
    }

    private fun configNavigationIcon() {
        val actionBar = (activity as MainActivity).supportActionBar
        val actionBarDrawerToggle = (activity as MainActivity).getDrawerToggle()
        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp)
        actionBarDrawerToggle.setToolbarNavigationClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }

    override fun onDestroyView() {
        countDownTimer?.cancel()
        mediaPlayer?.stop()
        super.onDestroyView()
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        mediaPlayer?.stop()
        super.onDestroy()
    }

    override fun onPause() {
        countDownTimer?.cancel()
        mediaPlayer?.pause()
        super.onPause()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_button_right -> {
                val alertDialog = AlertDialog.Builder(context)
                    .setView(R.layout.dialog_do_test)
                    .setCancelable(false)
                    .create()
                alertDialog?.let {
                    it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    it.show()
                    it.button_submit.setOnClickListener {
                        (activity as MainActivity).openFragment(
                            ScoreTestFragment(
                                questionsStatus,
                                secondTotalTime - totalTime,
                                timestamp,
                                tempPart
                            ),
                            false
                        )
                        alertDialog.cancel()
                    }
                    it.button_not_submit.setOnClickListener {
                        alertDialog.cancel()
                    }
                }
            }
            R.id.text_a -> {
                recyclerViewQuestion.adapter?.let {
                    viewModel.indexMain.value?.let {
                        if (text_a.tag == null) {
                            removeDrawAnswers()
                            questionsStatus[it].answer = getString(R.string.a)
                            drawAnswer(text_a)
                        } else {
                            questionsStatus[it].answer = ""
                            removeAnswer(text_a)
                        }
                    }
                }
            }
            R.id.text_b -> {
                recyclerViewQuestion.adapter?.let {
                    viewModel.indexMain.value?.let {
                        if (text_b.tag == null) {
                            removeDrawAnswers()
                            questionsStatus[it].answer = getString(R.string.b)
                            drawAnswer(text_b)
                        } else {
                            questionsStatus[it].answer = ""
                            removeAnswer(text_b)
                        }
                    }
                }
            }
            R.id.text_c -> {
                recyclerViewQuestion.adapter?.let {
                    viewModel.indexMain.value?.let {
                        if (text_c.tag == null) {
                            removeDrawAnswers()
                            questionsStatus[it].answer = getString(R.string.c)
                            drawAnswer(text_c)
                        } else {
                            questionsStatus[it].answer = ""
                            removeAnswer(text_c)
                        }
                    }
                }
            }
            R.id.text_d -> {
                recyclerViewQuestion.adapter?.let {
                    viewModel.indexMain.value?.let {
                        if (text_d.tag == null) {
                            removeDrawAnswers()
                            questionsStatus[it].answer = getString(R.string.d)
                            drawAnswer(text_d)
                        } else {
                            questionsStatus[it].answer = ""
                            removeAnswer(text_d)
                        }
                    }
                }
            }
        }
    }

    private fun removeDrawAnswers() {
        removeAnswer(text_a)
        removeAnswer(text_b)
        removeAnswer(text_c)
        removeAnswer(text_d)
    }
}
