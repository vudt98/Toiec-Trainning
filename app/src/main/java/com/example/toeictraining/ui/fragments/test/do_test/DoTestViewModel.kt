package com.example.toeictraining.ui.fragments.test.do_test

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.toeictraining.base.database.dao.QuestionDao
import com.example.toeictraining.base.entity.Question
import com.example.toeictraining.base.enums.ExamLevel
import com.example.toeictraining.ui.fragments.test.Constant
import kotlinx.coroutines.*

class DoTestViewModel(
    private val questionDao: QuestionDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    val indexMain = MutableLiveData<Int>()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var questionsLiveData = MutableLiveData<List<Question>>()

    fun getQuestionsLiveData(): LiveData<List<Question>> {
        return questionsLiveData
    }

    fun getQuestions(part: Int, examLevel: ExamLevel, limit: Int) {
        uiScope.launch {
            questionsLiveData.value = getQuestionsFromDatabase(part, examLevel, limit)
        }
    }

    private suspend fun getQuestionsFromDatabase(
        part: Int,
        examLevel: ExamLevel,
        limit: Int
    ): List<Question>? {
        return withContext(Dispatchers.IO) {
            if (part == 8) {
                questionDao.getQuestionByTypeAndLimit(examLevel.name, limit)
            } else {
                questionDao.getQuestionByPartAndTypeAndLimit(part, examLevel.name, limit)
            }
        }
    }
}
