package com.example.toeictraining.data.repository

import com.example.toeictraining.base.entity.Topic
import com.example.toeictraining.data.model.Category

interface TopicRepository {

    suspend fun getCategories(): List<Category>

    suspend fun updateTopic(topic: Topic)

    suspend fun getTopics(): List<Topic>
}
