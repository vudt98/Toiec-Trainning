package com.example.toeictraining.ui.fragments.test.start_test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.toeictraining.R
import com.example.toeictraining.base.enums.ExamLevel
import com.example.toeictraining.ui.fragments.test.Constant
import com.example.toeictraining.ui.fragments.test.do_test.DoTestFragment
import com.example.toeictraining.ui.fragments.test.history.HistoryTestFragment
import com.example.toeictraining.ui.main.MainActivity
import com.example.toeictraining.utils.DateUtil
import kotlinx.android.synthetic.main.start_test_fragment.*

class StartTestFragment(
    private val part: Int
) : Fragment() {

    companion object {
        val TAG = StartTestFragment::class.java.name
    }

    private lateinit var viewModel: StartTestViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.start_test_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StartTestViewModel::class.java)

        initViews()
        configNavigationIcon()
    }

    private fun initViews() {
        (activity as MainActivity).apply {
            setTitle("")
            setRightButtonText(getString(R.string.history))
            setOnClickToolbarRightButton(View.OnClickListener {
                (activity as MainActivity).openFragment(
                    HistoryTestFragment(),
                    true
                )
            })
        }
        setTextContent()
        button_start.setOnClickListener {
            var examLevel: ExamLevel = ExamLevel.EASY
            if (radio_medium.isChecked) examLevel = ExamLevel.MEDIUM
            if (radio_hard.isChecked) examLevel = ExamLevel.HARD
            (activity as MainActivity).openFragment(
                DoTestFragment(part, examLevel),
                false
            )
        }
    }

    private fun setTextContent() {
        text_correct_answer.text =
            getString(R.string.part).plus(" ").plus(part)
        if (part == 8) {
            text_correct_answer.text = getString(R.string.test_full)
        }
        text_time.text =
            getString(R.string.time).plus(DateUtil.secondsToStringTime(Constant.TIMES_PART[part]))
    }

    private fun configNavigationIcon() {
        val actionBar = (activity as MainActivity).supportActionBar
        val actionBarDrawerToggle = (activity as MainActivity).getDrawerToggle()
        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white_24dp)
        actionBarDrawerToggle.setToolbarNavigationClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }
}
