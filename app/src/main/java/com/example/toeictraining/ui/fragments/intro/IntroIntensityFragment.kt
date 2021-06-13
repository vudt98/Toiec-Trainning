package com.example.toeictraining.ui.fragments.intro

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.toeictraining.R
import com.example.toeictraining.base.BaseActivity
import com.example.toeictraining.base.BaseFragment
import com.example.toeictraining.utils.PracticeMode
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.intro_intensity_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class IntroIntensityFragment : BaseFragment<IntroViewModel>(),
    View.OnClickListener,
    AdapterView.OnItemSelectedListener {

    override val layoutResource: Int get() = R.layout.intro_intensity_fragment

    override val viewModel by viewModel<IntroViewModel>()

    private val intensities by lazy<Array<String>> {
        context?.resources?.getStringArray(R.array.intensity) ?: emptyArray()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
        navigation_view?.visibility = View.GONE

    }

    override fun initComponents() {
        spinnerSelect()
        btnIntensityFinish.setOnClickListener(this)
        btnIntensityBack.setOnClickListener(this)
    }

    private fun spinnerSelect() {
        context?.run {
            val adapter = ArrayAdapter(this, R.layout.intensity_spinner_item, intensities).apply {
                setDropDownViewResource(R.layout.intensity_spinner_dropdown_item)
            }
            spnIntensity?.adapter = adapter
            spnIntensity?.onItemSelectedListener = this@IntroIntensityFragment
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnIntensityFinish -> (activity as BaseActivity).backIntroScreen()
            R.id.btnIntensityBack -> (activity as BaseActivity).goBack(IntroScoreFragment())
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        spnIntensity?.setSelection(0)
        viewModel.savePracticeMode(PracticeMode.LOW)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.savePracticeMode(position + 1)
    }


    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
        navigation_view?.visibility = View.VISIBLE
    }
}
