package com.example.toeictraining.data.model

import com.example.toeictraining.base.entity.Topic

data class DailyWork(val id: Int, val content: String, var isDone: Boolean, private val type: Int) {

    var topic: Topic? = null

    fun isTest() = type == TEST_WORK

    fun isVocabulary() = type == VOCABULARY_WORK

    companion object {
        const val TEST_WORK = 0
        const val VOCABULARY_WORK = 1
    }
}
