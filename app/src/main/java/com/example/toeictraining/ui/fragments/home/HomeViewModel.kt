package com.example.toeictraining.ui.fragments.home

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.toeictraining.base.BaseViewModel
import com.example.toeictraining.base.entity.Topic
import com.example.toeictraining.data.model.DailyWork
import com.example.toeictraining.data.repository.TopicRepository
import com.example.toeictraining.utils.Constants
import com.example.toeictraining.utils.DateUtil
import com.example.toeictraining.utils.PracticeMode
import kotlinx.coroutines.launch

class HomeViewModel(
    private val topicRepository: TopicRepository,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel() {

    private val _dailyWorks = MutableLiveData<List<DailyWork>>()
    private val _recentResults = MutableLiveData<List<Int>>()
    private val _requireSetting = MutableLiveData<Boolean>()
    private val _practiceTime = MutableLiveData<Pair<String, String>>()

    val dailyWorks: LiveData<List<DailyWork>> get() = _dailyWorks
    val recentResults: LiveData<List<Int>> get() = _recentResults
    val requireSetting: LiveData<Boolean> get() = _requireSetting
    val practiceTime: LiveData<Pair<String, String>> get() = _practiceTime

    override fun onCreate() {
        super.onCreate()
        createDailyWorks()
        getDeadline()
        getTopics()
        getRecentResults()
    }

    private fun createDailyWorks() {
        if (isPracticeModeChanged()) {
            initDailyWorks()
        }
    }

    private fun isPracticeModeChanged(): Boolean {
        val practiceMode =
            sharedPreferences.getInt(Constants.PREFERENCE_PRACTICE_MODE, PracticeMode.EMPTY)
        val works = dailyWorks.value
        return practiceMode == PracticeMode.EMPTY
                || (!works.isNullOrEmpty() && works.size != practiceMode * 2)
    }

    private fun initDailyWorks() {
        val practiceMode =
            sharedPreferences.getInt(Constants.PREFERENCE_PRACTICE_MODE, PracticeMode.LOW)
        val dailyWorkParts = (1..7).toList().shuffled().take(practiceMode).joinToString()
        val dailyWorkTopics = (0..49).toList().shuffled().take(practiceMode).joinToString()

        sharedPreferences.edit().run {
            putString(Constants.PREFERENCE_DAILY_WORK_PART, dailyWorkParts)
            putString(Constants.PREFERENCE_DAILY_WORK_TOPIC, dailyWorkTopics)
        }.apply()
    }

    private fun getDeadline() {
        val startDay = sharedPreferences.getString(Constants.PREFERENCE_START_DAY, null)
        val endDay = sharedPreferences.getString(Constants.PREFERENCE_END_DAY, null)
        if (startDay == null || endDay == null) {
            _requireSetting.value = true
            return
        }
        _practiceTime.value = startDay to endDay
    }

    private fun getTopics() {
        viewModelScope.launch {
            val topics = topicRepository.getTopics()
            getDailyWorks(topics)
        }
    }

    private fun getDailyWorks(topics: List<Topic>) {
        val partsData = sharedPreferences.getString(Constants.PREFERENCE_DAILY_WORK_PART, null)
        val topicsData = sharedPreferences.getString(Constants.PREFERENCE_DAILY_WORK_TOPIC, null)
        if (partsData == null || topicsData == null) return

        val partIds = partsData.split(Constants.ARRAY_SEPARATOR).map { it.toInt() }
        val topicIds = topicsData.split(Constants.ARRAY_SEPARATOR).map { it.toInt() }

        _dailyWorks.value = mutableListOf<DailyWork>().apply {
            addAll(partIds.map {
                DailyWork(
                    id = it,
                    content = "Làm bài thi thử <i>Part $it</i>",
                    isDone = false,
                    type = DailyWork.TEST_WORK
                )
            })
            addAll(topicIds.map {
                DailyWork(
                    id = it,
                    content = "Học từ vựng topic <i>${topics[it].name}</i>",
                    isDone = topics[it].lastTime?.let { time ->
                        DateUtil.isToday(time)
                    } ?: false,
                    type = DailyWork.VOCABULARY_WORK
                ).apply {
                    topic = topics[it]
                }
            })
        }
    }

    private fun getRecentResults() {
        val recentResultsData =
            sharedPreferences.getString(Constants.PREFERENCE_RECENT_RESULTS_PROGRESS, null)
        recentResultsData ?: return
        _recentResults.value = recentResultsData.split(Constants.ARRAY_SEPARATOR).map { it.toInt() }
    }

    fun makeSettingDone() {
        _requireSetting.value = false
    }
}
