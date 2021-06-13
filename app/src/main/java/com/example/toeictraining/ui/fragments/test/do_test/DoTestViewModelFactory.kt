package com.example.toeictraining.ui.fragments.test.do_test

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.toeictraining.base.database.dao.QuestionDao

class DoTestViewModelFactory(
    private val dataSource: QuestionDao,
    private val application: Application,
    private val part: Int
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DoTestViewModel::class.java)) {
            return DoTestViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}