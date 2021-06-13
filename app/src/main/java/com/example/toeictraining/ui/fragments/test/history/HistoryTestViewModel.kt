package com.example.toeictraining.ui.fragments.test.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.toeictraining.base.database.dao.ExamDao
import com.example.toeictraining.base.entity.Exam
import kotlinx.coroutines.*

class HistoryTestViewModel(
    private val examDao: ExamDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var exams = MutableLiveData<List<Exam>>()

    fun getQuestionsLiveData(): MutableLiveData<List<Exam>> {
        return exams
    }

    init {
        uiScope.launch {
            exams.value = withContext(Dispatchers.IO) {
                examDao.getAll()
            }
        }
    }
}
