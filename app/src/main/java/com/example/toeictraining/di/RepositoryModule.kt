package com.example.toeictraining.di

import com.example.toeictraining.base.database.AppDatabase
import com.example.toeictraining.data.repository.TopicRepositoryImpl
import com.example.toeictraining.data.repository.WordRepositoryImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single(named(ScopeNames.WORD_REPOSITORY)) {
        WordRepositoryImpl(wordDao = get<AppDatabase>(named(ScopeNames.APP_DATABASE)).wordDao())
    }

    single(named(ScopeNames.TOPIC_REPOSITORY)) {
        TopicRepositoryImpl(topicDao = get<AppDatabase>(named(ScopeNames.APP_DATABASE)).topicDao())
    }
}
