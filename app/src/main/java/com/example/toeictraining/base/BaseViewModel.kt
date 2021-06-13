package com.example.toeictraining.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent

abstract class BaseViewModel : ViewModel(), KoinComponent {

    val message: MutableLiveData<String> = MutableLiveData()

    open fun onCreate() {}


}



