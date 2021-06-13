package com.example.toeictraining.ui.fragments.test.result

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.toeictraining.base.database.dao.ExamDao
import com.example.toeictraining.base.database.dao.QuestionDao

class ResultTestViewModelFactory(
    private val examDao: ExamDao,
    private val questionDao: QuestionDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultTestViewModel::class.java)) {
            return ResultTestViewModel(examDao, questionDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}