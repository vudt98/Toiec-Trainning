package com.example.toeictraining.data.repository

import com.example.toeictraining.base.database.dao.TopicDao
import com.example.toeictraining.base.entity.Topic
import com.example.toeictraining.data.model.Category
import com.example.toeictraining.utils.Constants

class TopicRepositoryImpl(private val topicDao: TopicDao) : TopicRepository {

    override suspend fun getCategories(): List<Category> {

        val categoryNames = topicDao.getCategoryNames()
        val topics = topicDao.getTopics()
        return categoryNames.map { name ->
            val subTopics = topics.filter { it.category == name }
            Category(
                name = name,
                color = subTopics.first().color ?: Constants.DEFAULT_COLOR,
                topics = subTopics
            )
        }
    }

    override suspend fun updateTopic(topic: Topic) = topicDao.updateTopic(topic)

    override suspend fun getTopics(): List<Topic> = topicDao.getTopics()
}
