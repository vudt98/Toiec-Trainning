package com.example.toeictraining.ui.fragments.test.history

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.toeictraining.base.database.dao.ExamDao
import com.example.toeictraining.base.database.dao.QuestionDao

class HistoryTestViewModelFactory(
    private val examDao: ExamDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryTestViewModel::class.java)) {
            return HistoryTestViewModel(examDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}