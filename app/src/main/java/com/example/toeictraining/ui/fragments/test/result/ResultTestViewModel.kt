package com.example.toeictraining.ui.fragments.test.result

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.toeictraining.base.database.dao.ExamDao
import com.example.toeictraining.base.database.dao.QuestionDao
import com.example.toeictraining.base.entity.Exam
import com.example.toeictraining.base.entity.Question
import kotlinx.coroutines.*

class ResultTestViewModel(
    private val examDao: ExamDao,
    private val questionDao: QuestionDao,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()

    companion object {
        val TAG: String = ResultTestViewModel::class.java.name
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var examLiveData = MutableLiveData<Exam>()
    private var questionLiveData = MutableLiveData<Question>()
//    private var questions = MutableLiveData<List<Question>>()

    fun getExamLiveData(): LiveData<Exam> {
        return examLiveData
    }

    fun getQuestionLiveData(): LiveData<Question> {
        return questionLiveData
    }

    fun getQuestionById(id:Int) {
        uiScope.launch {
            questionLiveData.value = getQuestionFromDatabase(id)
        }
    }

    private suspend fun getQuestionFromDatabase(id:Int): Question {
        return withContext(Dispatchers.IO) {
            questionDao.getQuestionsById(id)
        }
    }

//    fun getQuestionsByIdsLiveData(): LiveData<List<Question>> {
//        return questions
//    }

//    fun getQuestionsByIds(ids: MutableList<Int>) {
//        uiScope.launch {
//            questions.value = getQuestionsByIdsFromDatabase(ids)
//        }
//    }

//    private suspend fun getQuestionsByIdsFromDatabase(ids: MutableList<Int>): List<Question> {
//        return withContext(Dispatchers.IO) {
//            questionDao.loadAllByIds(ids.toIntArray())
//        }
//    }

    fun getExam(id: Long) {
        uiScope.launch {
            examLiveData.value = getQuestionsFromDatabase(id)
        }
    }

    private suspend fun getQuestionsFromDatabase(id: Long): Exam? {
        return withContext(Dispatchers.IO) {
            examDao.getExamById(id)
        }
    }
}
