package com.example.toeictraining.ui.fragments.test.score

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.toeictraining.base.database.dao.ExamDao
import com.example.toeictraining.base.entity.Exam
import kotlinx.coroutines.*

class ScoreTestViewModel(
    private val examDao: ExamDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    companion object {
        val TAG: String = ScoreTestViewModel::class.java.name
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val insertResultLiveData = MutableLiveData<Long>()

    fun getInsertResultLiveData(): LiveData<Long> {
        return insertResultLiveData
    }

    fun insert(exam: Exam) {
        uiScope.launch {
            insertResultLiveData.value = insertToDatabase(exam)
        }
    }

    private suspend fun insertToDatabase(exam: Exam): Long {
        return withContext(Dispatchers.IO) {
            examDao.insert(exam)
        }
    }
}
