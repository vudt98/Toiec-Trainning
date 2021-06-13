package com.example.toeictraining.ui.main

import android.content.SharedPreferences
import android.text.format.DateUtils
import android.util.Log
import com.example.toeictraining.base.BaseViewModel
import com.example.toeictraining.utils.Constants
import com.example.toeictraining.utils.PracticeMode

class MainViewModel(private val sharedPreferences: SharedPreferences) : BaseViewModel() {

    override fun onCreate() {
        super.onCreate()
        val lastAccessTime = sharedPreferences.getLong(Constants.PREFERENCE_LAST_ACCESS, 0L)
        if (lastAccessTime == 0L || !DateUtils.isToday(lastAccessTime)) {
            initDailyWorks()
        }
        sharedPreferences.edit()
            .putLong(Constants.PREFERENCE_LAST_ACCESS, System.currentTimeMillis())
            .apply()
    }

    private fun resetDailyWork() {
        sharedPreferences.edit().run {
            putInt(Constants.PREFERENCE_PRACTICE_MODE, PracticeMode.HIGH)
        }.apply()
        initDailyWorks()
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
}
