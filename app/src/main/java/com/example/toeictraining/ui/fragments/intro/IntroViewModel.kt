package com.example.toeictraining.ui.fragments.intro

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.toeictraining.base.BaseViewModel
import com.example.toeictraining.utils.Constants
import com.example.toeictraining.utils.DateUtil

class IntroViewModel(
    private val sharedPreferences: SharedPreferences
) : BaseViewModel() {

    private val _targetScore: MutableLiveData<Int> = MutableLiveData()
    private val _startPracticeDay = MutableLiveData<String>()
    private val _endPracticeDay = MutableLiveData<String>()
    private val _practiceMode = MutableLiveData<Int>()

    val targetScore: LiveData<Int> get() = _targetScore
    val startPracticeDay: LiveData<String> = _startPracticeDay
    val endPracticeDay: LiveData<String> = _endPracticeDay
    val practiceMode: LiveData<Int> = _practiceMode

    override fun onCreate() {
        super.onCreate()
        saveStartDay(DateUtil.getCurrentDate())
    }

    private fun saveStartDay(time: String) {
        sharedPreferences.edit().putString(Constants.PREFERENCE_START_DAY, time).apply()
        _startPracticeDay.value = time
    }

    fun saveDeadline(time: String) {
        sharedPreferences.edit().putString(Constants.PREFERENCE_END_DAY, time).apply()
        _endPracticeDay.value = time
    }

    fun savePracticeMode(practiceMode: Int) {
        sharedPreferences.edit().putInt(Constants.PREFERENCE_PRACTICE_MODE, practiceMode).apply()
        _practiceMode.value = practiceMode
    }

    fun saveTargetScore(score: Int) {
        sharedPreferences.edit().putInt(Constants.PREFERENCE_TARGET_SCORE, score).apply()
        _targetScore.value = score
    }

}