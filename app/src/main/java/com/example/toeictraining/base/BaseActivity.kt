package com.example.toeictraining.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.toeictraining.R

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val layoutResource: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)
        initComponent()
        initData()
    }

    protected abstract fun initComponent()

    protected abstract fun initData()

    fun openFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        supportFragmentManager.beginTransaction().replace(R.id.content, fragment).apply {
            if (addToBackStack) addToBackStack(null)
        }.commit()
    }

    fun goNext(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left)
            .replace(R.id.content, fragment, fragment.javaClass.simpleName)
            .addToBackStack(null)
            .commit()
    }

    fun goBack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.animator.slide_out_right, R.animator.slide_in_left)
            .replace(R.id.content, fragment, fragment.javaClass.simpleName)
            .addToBackStack(null)
            .commit()
    }

    fun backIntroScreen() = with(supportFragmentManager) {
        for (count in 1..backStackEntryCount) popBackStack()
    }
}
