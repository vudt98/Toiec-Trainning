package com.example.toeictraining.ui.fragments.test.result

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toeictraining.R
import com.example.toeictraining.base.database.AppDatabase
import com.example.toeictraining.base.entity.Exam
import com.example.toeictraining.base.entity.Question
import com.example.toeictraining.ui.main.MainActivity
import kotlinx.android.synthetic.main.result_test_fragment.*

class ResultTestFragment(
    private val idExam: Long
) : Fragment() {
    private lateinit var viewModel: ResultTestViewModel
    private val list = mutableListOf<Result>()
    private val questions = mutableListOf<Question>()
    private var exam: Exam? = null
    private var totalScore = 0
    private var quesIndex = 0

    companion object {
        val TAG = ResultTestFragment::class.java.name
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application: Application = requireNotNull(activity).application
        val examDao = AppDatabase.getInstance(application).examDao()
        val questionDao = AppDatabase.getInstance(application).questionDao()
        val viewModelFactory = ResultTestViewModelFactory(examDao, questionDao, application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ResultTestViewModel::class.java)
        return inflater.inflate(R.layout.result_test_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "id = $idExam")
        initViews()
        configNavigationIcon()
        handleObservable()
    }

    private fun handleObservable() {
        viewModel.getExamLiveData().observe(viewLifecycleOwner, Observer {
            exam = it
            Log.d(TAG, "exam = $exam")
            exam?.let { exam ->
                viewModel.getQuestionById(exam.questionIdList[quesIndex++])
            }
        })

        viewModel.getQuestionLiveData().observe(viewLifecycleOwner, Observer { question ->
            Log.d(TAG, "question = $question")
            questions.add(question)
            Log.d(TAG, "size = ${questions.size}")
            exam?.let {
                if (quesIndex < it.questionIdList.size) {
                    viewModel.getQuestionById(it.questionIdList[quesIndex++])
                } else {
                    Log.d(TAG, "questions =$questions")
                    exam?.answerList?.let { answerList ->
                        for (i in questions.indices) {
                            list.add(Result(questions[i], answerList[i]))
                            if (questions[i].correctAnswer == answerList[i]) {
                                totalScore += 5
                            }
                        }
                        rv_result.adapter?.notifyDataSetChanged()
                        exam?.time?.let { time ->
                            total_time.text = getString(R.string.test_time_total).plus(time)
                        }
                        exam?.score?.let {
                            total_score.text = getString(R.string.total_score_2).plus(it)
                        }
                    }
                }
            }
        })

//        viewModel.getQuestionsByIdsLiveData().observe(viewLifecycleOwner, Observer { questions ->
//            Log.d(TAG, "questions =$questions")
//            exam?.answerList?.let { answerList ->
//                for (i in questions.indices) {
//                    list.add(Result(questions[i], answerList[i]))
//                    if (questions[i].correctAnswer == answerList[i]) {
//                        totalScore += 5
//                    }
//                }
//                rv_result.adapter?.notifyDataSetChanged()
//                exam?.time?.let { time ->
//                    total_time.text = getString(R.string.test_time_total).plus(time)
//                }
//                exam?.score?.let {
//                    total_score.text = getString(R.string.total_score_2).plus(it)
//                }
//            }
//        })
    }

    private fun initViews() {
        viewModel.getExam(idExam)
        rv_result.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_result.adapter = activity?.let { activity -> ResultRecyclerViewAdapter(activity, list) }
    }

    private fun configNavigationIcon() {
        val actionBar = (activity as MainActivity).supportActionBar
        val actionBarDrawerToggle = (activity as MainActivity).getDrawerToggle()
        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp)
        actionBarDrawerToggle.setToolbarNavigationClickListener {
            (activity as MainActivity).isSave = false
            (activity as MainActivity).onBackPressed()
        }
    }
}
