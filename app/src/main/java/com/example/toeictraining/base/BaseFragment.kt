package com.example.toeictraining.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.toeictraining.R
import kotlinx.android.synthetic.main.activity_main.view.*

abstract class BaseFragment<VM : BaseViewModel> : Fragment(), LifecycleOwner {

    protected abstract val layoutResource: Int
    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(layoutResource, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        initData()
        observeData()
    }

    protected abstract fun initComponents()

    protected open fun initData() {
        viewModel.onCreate()
    }

    protected open fun observeData() {
        viewModel.message.observe(viewLifecycleOwner, Observer { data ->
            context?.run {
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
            }
        })
    }

    protected fun replaceFragment(id: Int = R.id.content, fragment: Fragment, addToBackStack: Boolean) =
        activity?.supportFragmentManager?.beginTransaction()?.replace(id, fragment)?.apply {
            if (addToBackStack) addToBackStack(null)
        }?.commit()

    protected fun addFragment(id: Int = R.id.content, fragment: Fragment, addToBackStack: Boolean) =
        activity?.supportFragmentManager?.beginTransaction()?.add(id, fragment)?.apply {
            if (addToBackStack) addToBackStack(null)
        }?.commit()

    protected fun toast(message: String) = context?.let {
        Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
    }
}
