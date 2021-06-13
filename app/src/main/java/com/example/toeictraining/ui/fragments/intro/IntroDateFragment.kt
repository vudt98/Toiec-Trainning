package com.example.toeictraining.ui.fragments.intro

import android.app.DatePickerDialog
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.toeictraining.R
import com.example.toeictraining.base.BaseFragment
import com.example.toeictraining.ui.main.MainActivity
import com.example.toeictraining.utils.DateUtil
import com.example.toeictraining.utils.showDatePickerDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.intro_date_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class IntroDateFragment private constructor() : BaseFragment<IntroViewModel>(),
    View.OnClickListener {

    override val layoutResource: Int get() = R.layout.intro_date_fragment

    override val viewModel by viewModel<IntroViewModel>()

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
        navigation_view?.visibility = View.GONE
    }

    override fun initComponents() {
        textDate.setOnClickListener(this)
        btnDateNext.setOnClickListener(this)
    }

    override fun initData() {
        super.initData()
        viewModel.saveDeadline(DateUtil.getCurrentDate())
        textDate?.text = DateUtil.DATE_FORMAT
    }

    override fun observeData() {
        super.observeData()
        viewModel.endPracticeDay.observe(viewLifecycleOwner, Observer(textDate::setText))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.textDate -> context?.showDatePickerDialog(DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                viewModel.saveDeadline(DateUtil.getDate(dayOfMonth, month, year))
            })
            R.id.btnDateNext -> (activity as MainActivity).goNext(IntroScoreFragment())
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
        navigation_view?.visibility = View.GONE
    }

    companion object {
        fun newInstance() = IntroDateFragment()
    }
}
