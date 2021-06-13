package com.example.toeictraining.ui.fragments.study

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.toeictraining.R
import com.example.toeictraining.base.BaseFragment
import com.example.toeictraining.base.entity.Topic
import com.example.toeictraining.base.entity.Word
import com.example.toeictraining.di.ScopeNames
import com.example.toeictraining.utils.Constants
import com.example.toeictraining.utils.WordHelper
import kotlinx.android.synthetic.main.fragment_study.*
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class StudyFragment private constructor() : BaseFragment<StudyViewModel>(), View.OnClickListener {

    override val layoutResource: Int = R.layout.fragment_study

    override val viewModel: StudyViewModel by viewModel()

    private lateinit var animatorSet: AnimatorSet

    private val mediaPlayer by lazy {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun initComponents() {
        textShowDetail?.setOnClickListener(this)
        textDidntKnow?.setOnClickListener(this)
        textKnew?.setOnClickListener(this)
        textOrigin?.setOnClickListener(this)
        imageStudyBack?.setOnClickListener(this)
        imageSound?.setOnClickListener(this)
    }

    override fun initData() {
        viewModel.mainTopic = arguments?.getParcelable(Constants.ARGUMENT_TOPIC)
            ?: get(named(ScopeNames.EMPTY_TOPIC))
        super.initData()

        textTopicName?.text = viewModel.mainTopic.name
    }

    override fun observeData() = with(viewModel) {
        super.observeData()

        word.observe(viewLifecycleOwner, Observer(::showWordData))
    }

    private fun showWordData(word: Word) {
        textOrigin?.text = word.origin
        textPronoun?.text = word.pronunciation
        textExplain?.text = word.explanation
        textType?.text = word.type
        textExample?.text = word.example
        textExampleTranslate?.text = word.exampleTranslate
        textLevel?.text = WordHelper.getLevelContent(word.level)

        Glide.with(this@StudyFragment)
            .asBitmap()
            .override(200, 200)
            .fitCenter()
            .load(word.imageUrl)
            .into(imageWord)

        textShowDetail?.isVisible = true
        groupDetail?.isVisible = false
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imageStudyBack -> activity?.onBackPressed()
            R.id.textShowDetail -> shiftFlashCardStatus()
            R.id.textOrigin -> shiftFlashCardStatus()
            R.id.textDidntKnow -> changeNextWord(isKnown = false)
            R.id.textKnew -> changeNextWord(isKnown = true)
            R.id.imageSound -> playSound()
        }
    }

    private fun shiftFlashCardStatus() {
        textShowDetail.isVisible = !textShowDetail.isVisible
        groupDetail.isVisible = !groupDetail.isVisible
    }

    private fun changeNextWord(isKnown: Boolean) {
        flashCard?.setAnimation(R.animator.anim_move_from_left)

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                viewModel.updateWordLevel(isKnown)
                viewModel.changeWord()
                flashCard?.setAnimation(R.animator.anim_move_from_right)
            }
        })

    }

    private fun playSound() {
        val word = viewModel.word.value ?: return
        mediaPlayer.apply {
            stop()
            reset()
            setDataSource(word.getSoundLink())
            setOnPreparedListener { it.start() }
            prepareAsync()
        }
    }

    private fun View.setAnimation(resId: Int) {
        animatorSet = AnimatorInflater.loadAnimator(context, resId) as AnimatorSet
        animatorSet.run {
            setTarget(this)
            start()
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    companion object {

        const val TAG = "StudyFragment"

        fun newInstance(topic: Topic): StudyFragment = StudyFragment().apply {
            arguments = bundleOf(Constants.ARGUMENT_TOPIC to topic)
        }
    }
}
