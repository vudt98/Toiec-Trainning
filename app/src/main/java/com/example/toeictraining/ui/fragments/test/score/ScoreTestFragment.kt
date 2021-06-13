package com.example.toeictraining.ui.fragments.test.score

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.toeictraining.R
import com.example.toeictraining.base.database.AppDatabase
import com.example.toeictraining.base.entity.Exam
import com.example.toeictraining.ui.fragments.test.do_test.QuestionStatus
import com.example.toeictraining.ui.fragments.test.home.HomeTestFragment
import com.example.toeictraining.ui.fragments.test.result.ResultTestFragment
import com.example.toeictraining.ui.fragments.test.start_test.StartTestFragment
import com.example.toeictraining.ui.main.MainActivity
import com.example.toeictraining.utils.Constants
import com.example.toeictraining.utils.DateUtil
import com.huma.room_for_asset.defaultSharedPreferences
import kotlinx.android.synthetic.main.score_test_fragment.*
import kotlin.math.roundToInt

class ScoreTestFragment(
    private val questionsStatus: List<QuestionStatus>,
    private val totalTime: Int,
    private val timestamp: String,
    private val part: Int
) :
    Fragment(), View.OnClickListener {

    companion object {
        val TAG = ScoreTestFragment::class.java.name
    }

    private val level = arrayOf("Yếu", "Trung Bình", "Tốt", "Rất Tốt")
    private lateinit var viewModel: ScoreTestViewModel
    private var totalScore = 0
    private var idExam: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application: Application = requireNotNull(this.activity).application
        val examDao = AppDatabase.getInstance(application).examDao()
        val viewModelFactory = ScoreTestViewModelFactory(examDao, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ScoreTestViewModel::class.java)
        return inflater.inflate(R.layout.score_test_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ScoreTestViewModel::class.java)
        initViews()
        handleObservable()
        text_result.text =
            getString(R.string.test_time_total).plus(DateUtil.secondsToStringTime(totalTime))
        var listenRightQues = 0
        var readRightQues = 0
        var listenScore = 0
        var readScore = 0

        val listAnswer = mutableListOf<String>()
        val listQuestionId = mutableListOf<Int>()

        for (questionStatus in questionsStatus) {
            //create exam
            listAnswer.add(questionStatus.answer)
            listQuestionId.add(questionStatus.data.id)
            if (questionStatus.data.correctAnswer == questionStatus.answer) {
                if (questionStatus.data.soundLink != null) {
                    listenRightQues += 1
                    continue
                }
                readRightQues += 1
            }
        }
        if (part == 8) {
            for (i in 1..listenRightQues) {
                if (i <= 6) listenScore = 5
                else listenScore += 5
            }
            for (i in 1..readRightQues) {
                if (i <= 6) readScore = 5
                else readScore += 5
            }
        } else {
            listenScore = listenRightQues * 5
            readScore = readRightQues * 5
        }

        Log.d(TAG, "listenRightQues = $listenRightQues, listenScore = $listenScore")
        Log.d(TAG, "readRightQues = $readRightQues, readScore = $readScore")


        //save exam
        val exam = Exam(
            questionIdList = listQuestionId,
            answerList = listAnswer,
            time = totalTime,
            part = part,
            timestamp = timestamp,
            score = listenScore + readScore
        )
        if ((activity as MainActivity).isSave) {
            viewModel.insert(exam)
        }
        //chỉ bài full mới show điểm đọc, điểm nghe
        if (part == 8) {
            text_listen_score?.visibility = View.VISIBLE
            text_listen_score?.text = getString(R.string.listen_score).plus("$listenScore" + "/495")
            text_read_score?.visibility = View.VISIBLE
            text_read_score?.text = getString(R.string.read_score).plus("$readScore" + "/495")
        }
        totalScore = listenScore + readScore
        total_score.text = totalScore.toString()
        if (questionsStatus.size == 200) {
            saveRecentResults()
            text_expand?.visibility = View.VISIBLE
            text_expand?.setOnClickListener {
                if (it.tag == null) {
                    layout_detail.visibility = View.VISIBLE
                    text_expand.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.expand_48,
                        0
                    )

                    //part 1
                    setDetailPart(1, 6, progressbar_part_1, text_evaluate_1)
                    //part 2
                    setDetailPart(7, 31, progressbar_part_2, text_evaluate_2)
                    //part 3
                    setDetailPart(32, 70, progressbar_part_3, text_evaluate_3)
                    //part 4
                    setDetailPart(71, 100, progressbar_part_4, text_evaluate_4)
                    //part 5
                    setDetailPart(101, 130, progressbar_part_5, text_evaluate_5)
                    //part 6
                    setDetailPart(131, 146, progressbar_part_6, text_evaluate_6)
                    //part 7
                    setDetailPart(147, 200, progressbar_part_7, text_evaluate_7)
                    //set tag
                    it.tag = true
                } else {
                    layout_detail.visibility = View.GONE
                    text_expand.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.collapse_48,
                        0
                    )
                    it.tag = null
                }
            }
            button_learn_more_1?.setOnClickListener(this)
            button_learn_more_2?.setOnClickListener(this)
            button_learn_more_3?.setOnClickListener(this)
            button_learn_more_4?.setOnClickListener(this)
            button_learn_more_5?.setOnClickListener(this)
            button_learn_more_6?.setOnClickListener(this)
            button_learn_more_7?.setOnClickListener(this)
        }
        configNavigationIcon()
    }

    private fun handleObservable() {
        viewModel.getInsertResultLiveData().observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "idExam = $it")
            idExam = it
        })
    }

    private fun setDetailPart(
        startQuestion: Int,
        endQuestion: Int,
        progressBar: ProgressBar,
        textView: TextView
    ) {
        // tính số câu đúng part 1
        var countCorrectAnswerPart = 0
        for (i in startQuestion.minus(1)..endQuestion.minus(1)) {
            if (questionsStatus[i].answer == questionsStatus[i].data.correctAnswer) {
                countCorrectAnswerPart++
            }
        }
        //set progress cho part 1
        val progress = (countCorrectAnswerPart * 100.0 / (endQuestion - startQuestion)).roundToInt()
        progressBar.progress = progress
        //set level of part
        textView.text = level[0]
        if (progress >= 50) {
            textView.text = level[1]
        }
        if (progress >= 80) {
            textView.text = level[2]
            button_learn_more_1.visibility = View.INVISIBLE
            button_learn_more_1.isEnabled = false
        }
        if (progress >= 95) {
            textView.text = level[3]
            button_learn_more_1.visibility = View.INVISIBLE
            button_learn_more_1.isEnabled = false
        }
    }

    private fun initViews() {
        (activity as MainActivity).setRightButtonText("")
        (activity as MainActivity).setTitle("")

        button_continue_do.setOnClickListener(this)
        button_result.setOnClickListener(this)
    }

    private fun configNavigationIcon() {
        val actionBar = (activity as MainActivity).supportActionBar
        val actionBarDrawerToggle = (activity as MainActivity).getDrawerToggle()
        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        actionBarDrawerToggle.setToolbarNavigationClickListener {
            (activity as MainActivity).isSave = true
            (activity as MainActivity).openFragment(
                HomeTestFragment(),
                false
            )
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_continue_do -> {
                (activity as MainActivity).isSave = true
                (activity as MainActivity).openFragment(
                    HomeTestFragment(),
                    false
                )
            }
            R.id.button_result -> {
                Log.d(TAG, "idExam = $idExam")
                idExam?.let {
                    (activity as MainActivity).openFragment(
                        ResultTestFragment(it),
                        true
                    )
                }
            }
            R.id.button_learn_more_1 -> {
                (activity as MainActivity).openFragment(
                    StartTestFragment(1),
                    false
                )
            }
            R.id.button_learn_more_2 -> {
                (activity as MainActivity).openFragment(
                    StartTestFragment(2),
                    false
                )
            }
            R.id.button_learn_more_3 -> {
                (activity as MainActivity).openFragment(
                    StartTestFragment(3),
                    false
                )
            }
            R.id.button_learn_more_4 -> {
                (activity as MainActivity).openFragment(
                    StartTestFragment(4),
                    false
                )
            }
            R.id.button_learn_more_5 -> {
                (activity as MainActivity).openFragment(
                    StartTestFragment(5),
                    false
                )
            }
            R.id.button_learn_more_6 -> {
                (activity as MainActivity).openFragment(
                    StartTestFragment(6),
                    false
                )
            }
            R.id.button_learn_more_7 -> {
                (activity as MainActivity).openFragment(
                    StartTestFragment(7),
                    false
                )
            }
        }
    }

    private fun saveRecentResults() {
        val partRanges =
            listOf(0 to 5, 6 to 30, 31 to 69, 70 to 99, 100 to 129, 130 to 145, 146 to 199)

        val recentResultProgresses = partRanges.map { partRange ->
            val numberCorrects = questionsStatus.subList(partRange.first, partRange.second + 1)
                .filter { it.answer == it.data.correctAnswer }
                .size
            val total = partRange.second - partRange.first + 1
            (numberCorrects * 100f / total).roundToInt()
        }.joinToString()
        context?.run {
            defaultSharedPreferences.edit()
                .putString(Constants.PREFERENCE_RECENT_RESULTS_PROGRESS, recentResultProgresses)
                .apply()
        }
    }
}
