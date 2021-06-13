package com.example.toeictraining.ui.fragments.reminder


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.toeictraining.R
import com.example.toeictraining.base.BaseFragment
import com.example.toeictraining.utils.*
import com.example.toeictraining.works.RemindWorker
import com.jaredrummler.materialspinner.MaterialSpinner
import kotlinx.android.synthetic.main.fragment_setting.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class RemindFragment private constructor() : BaseFragment<RemindViewModel>(),
    View.OnClickListener,
    CompoundButton.OnCheckedChangeListener,
    MaterialSpinner.OnItemSelectedListener<String> {

    override val layoutResource: Int get() = R.layout.fragment_setting
    override val viewModel: RemindViewModel by viewModel()

    private val remindTopicAdapter: RemindTopicAdapter = get()
    private val targetScores: List<String> by lazy {
        resources.getStringArray(R.array.scores).toList()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun initComponents() {
        spinnerTargetScore?.setItems(targetScores)
        recyclerReviews?.adapter = remindTopicAdapter.apply {
            onTopicSelected = { topic ->
                viewModel.updateTopicReviews(topic)
            }
        }

        context?.run {
            spinnerPracticeMode?.setItems(
                getString(R.string.title_practice_low),
                getString(R.string.title_practice_normal),
                getString(R.string.title_practice_high)
            )
        }
        nestedScrollView.scrollTo(0, 0)
        registerEvents()
    }

    override fun initData() {
        super.initData()
        switchReminder.isChecked = viewModel.isOnReviewMode()
    }

    override fun observeData() = with(viewModel) {
        super.observeData()

        startPracticeDay.observe(viewLifecycleOwner, Observer(textSettingStartDay::setText))
        endPracticeDay.observe(viewLifecycleOwner, Observer(textOfficialDeadline::setText))
        practiceMode.observe(viewLifecycleOwner, Observer(::showPracticeMode))

        topics.observe(viewLifecycleOwner, Observer(remindTopicAdapter::submitList))
        reviewMode.observe(viewLifecycleOwner, Observer(::onObserverReviewMode))
        targetScore.observe(viewLifecycleOwner, Observer {
            spinnerTargetScore.text = it.toString()
        })
        reviewTopic.observe(viewLifecycleOwner, Observer {
            context?.run {
                toast(getString(R.string.msg_remind_topics) + " " + it)
            }
        })

        testRemindTime.observe(viewLifecycleOwner, Observer {
            textDoWorkExamTime?.text = it
            startRemindDaily()
        })
        vocabularyRemindTime.observe(viewLifecycleOwner, Observer {
            textDoWorkVocabularyTime?.text = it
            startRemindDaily()
        })
    }

    private fun showPracticeMode(practiceMode: Int) {
        context?.run {
            spinnerPracticeMode.selectedIndex = practiceMode - 1
        }
    }

    private fun onObserverReviewMode(isEnable: Boolean) {
        recyclerReviews.isVisible = isEnable == true
        switchReviewWords.isChecked = isEnable == true
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.textSettingStartDay -> context?.showDatePickerDialog(DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                viewModel.saveStartDay(DateUtil.getDate(dayOfMonth, month, year))
            })
            R.id.textOfficialDeadline -> context?.showDatePickerDialog(DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                viewModel.saveEndDay(DateUtil.getDate(dayOfMonth, month, year))
            })
            R.id.imageSaveReminder -> toast(getString(R.string.msg_save_setting))
            R.id.imageBackSetting -> activity?.onBackPressed()
            R.id.textDoWorkExamTime -> context?.showTimePickerDialog(TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                textDoWorkExamTime.text =
                    String.format(DateUtil.TIME_FORMAT, hourOfDay, minute).also {
                        viewModel.saveTestRemindTime(it)
                    }
            })
            R.id.textDoWorkVocabularyTime -> context?.showTimePickerDialog(TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                textDoWorkVocabularyTime.text =
                    String.format(DateUtil.TIME_FORMAT, hourOfDay, minute).also {
                        viewModel.saveVocabularyRemindTime(it)
                    }
            })
        }
    }

    override fun onItemSelected(view: MaterialSpinner?, position: Int, id: Long, item: String?) {
        if (view?.id == R.id.spinnerPracticeMode) {
            viewModel.savePracticeMode(
                when (item) {
                    getString(R.string.title_practice_low) -> PracticeMode.LOW
                    getString(R.string.title_practice_normal) -> PracticeMode.NORMAL
                    getString(R.string.title_practice_high) -> PracticeMode.HIGH
                    else -> PracticeMode.EMPTY
                }
            )
        } else if (view?.id == R.id.spinnerTargetScore) {
            item?.let {
                viewModel.saveTargetScore(it.toInt())
            }
        }

    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.switchReminder -> handleTimeRemindSwitch(buttonView, isChecked)
            R.id.switchReviewWords -> handleReviewModeEnable(buttonView, isChecked)
        }
    }

    private fun handleTimeRemindSwitch(buttonView: CompoundButton, isChecked: Boolean) {
        groupConstraintDailyReminder?.isVisible = isChecked
        viewModel.saveEnableRemindMode(isChecked)
        if (isChecked) {
            viewModel.getRemindTime()
        } else {
            stopRemindDaily()
            viewModel.clearRemindTime()
        }
    }

    private fun handleReviewModeEnable(buttonView: CompoundButton, isChecked: Boolean) {
        recyclerReviews?.isVisible = isChecked == true
        viewModel.saveEnableReviewMode(isChecked)
    }

    private fun startRemindDaily() {
        val testTime = viewModel.testRemindTime.value ?: DateUtil.DEFAULT_TIME
        val vocabularyTime = viewModel.vocabularyRemindTime.value ?: DateUtil.DEFAULT_TIME
        stopRemindDaily()
        val constraints = Constraints.Builder().setRequiresCharging(true).build()

        val testRemindMessageData = Data.Builder()
            .putString(Constants.KEY_MESSAGE, getString(R.string.msg_remind_do_test))
            .build()
        val vocabularyRemindMessageData = Data.Builder()
            .putString(Constants.KEY_MESSAGE, getString(R.string.msg_remind_learn_vocabulary))
            .build()

        val testRequest = PeriodicWorkRequestBuilder<RemindWorker>(1, TimeUnit.DAYS)
            .addTag(Constants.TAG_DAILY_REMINDER)
            .setInitialDelay(DateUtil.getDelayMinutes(testTime), TimeUnit.MINUTES)
            .setInputData(testRemindMessageData)
            .setConstraints(constraints)
            .build()
        val vocabularyRequest = PeriodicWorkRequestBuilder<RemindWorker>(1, TimeUnit.DAYS)
            .addTag(Constants.TAG_DAILY_REMINDER)
            .setInitialDelay(DateUtil.getDelayMinutes(vocabularyTime), TimeUnit.MINUTES)
            .setInputData(vocabularyRemindMessageData)
            .setConstraints(constraints)
            .build()

        context?.let {
            WorkManager.getInstance(it).apply {
                enqueue(testRequest)
                enqueue(vocabularyRequest)
            }
        }
    }

    private fun stopRemindDaily() {
        context?.let {
            WorkManager.getInstance(it).cancelAllWorkByTag(Constants.TAG_DAILY_REMINDER)
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    private fun registerEvents() {
        switchReminder.setOnCheckedChangeListener(this)
        switchReviewWords.setOnCheckedChangeListener(this)
        imageBackSetting.setOnClickListener(this)
        imageSaveReminder.setOnClickListener(this)
        textDoWorkExam.setOnClickListener(this)
        textDoWorkVocabulary.setOnClickListener(this)
        textDoWorkVocabularyTime.setOnClickListener(this)
        textDoWorkExamTime.setOnClickListener(this)
        textSettingStartDay.setOnClickListener(this)
        textOfficialDeadline.setOnClickListener(this)
        spinnerPracticeMode.setOnItemSelectedListener(this)
        spinnerTargetScore.setOnItemSelectedListener(this)
        textSettingStartDay.setOnClickListener(this)
    }

    companion object {
        val TAG: String = RemindFragment::class.java.name
        fun newInstance() = RemindFragment()
    }
}
