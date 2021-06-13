package com.example.toeictraining.ui.fragments.test.score

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.toeictraining.base.database.dao.ExamDao
import com.example.toeictraining.base.database.dao.QuestionDao

class ScoreTestViewModelFactory(
    private val examDao: ExamDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreTestViewModel::class.java)) {
            return ScoreTestViewModel(examDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}