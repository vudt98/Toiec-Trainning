package com.example.toeictraining.ui.fragments.intro

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.toeictraining.R
import com.example.toeictraining.base.BaseFragment
import com.example.toeictraining.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.intro_score_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class IntroScoreFragment : BaseFragment<IntroViewModel>(),
    View.OnClickListener,
    AdapterView.OnItemSelectedListener {

    override val layoutResource: Int get() = R.layout.intro_score_fragment

    override val viewModel by viewModel<IntroViewModel>()

    private val scores by lazy<Array<String>> {
        resources.getStringArray(R.array.scores)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
        navigation_view?.visibility = View.GONE
    }

    override fun initComponents() {
        spinnerSelect()
        btnScoreNext.setOnClickListener(this)
        btnScoreBack.setOnClickListener(this)
    }

    private fun spinnerSelect() {
        context?.run {
            val adapter = ArrayAdapter(this, R.layout.intensity_spinner_item, scores).apply {
                setDropDownViewResource(R.layout.intensity_spinner_dropdown_item)
            }
            spnScore?.adapter = adapter
            spnScore?.onItemSelectedListener = this@IntroScoreFragment
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnScoreNext -> (activity as MainActivity).goNext(IntroIntensityFragment())
            R.id.btnScoreBack -> (activity as MainActivity).goBack(IntroDateFragment.newInstance())
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        spnScore?.setSelection(0)
        viewModel.saveTargetScore(scores[0].toInt())
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.saveTargetScore(scores[position].toInt())
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
        navigation_view?.visibility = View.GONE
    }
}
