package com.example.toeictraining.di

import android.app.Application
import com.example.toeictraining.ui.fragments.reminder.remindModule
import com.example.toeictraining.ui.fragments.study.studyModule
import com.example.toeictraining.ui.fragments.vocabulary.vocabularyModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    appModule,
                    repositoryModule,
                    viewModelModule,
                    vocabularyModule,
                    studyModule,
                    remindModule
                )
            )
        }
    }
}