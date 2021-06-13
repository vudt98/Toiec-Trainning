package com.example.toeictraining.base.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.toeictraining.base.entity.Topic

@Dao
interface TopicDao {
    @Query("SELECT * FROM ${Topic.TABLE_NAME}")
    suspend fun getTopics(): List<Topic>

    @Query("SELECT DISTINCT category FROM ${Topic.TABLE_NAME}")
    suspend fun getCategoryNames(): List<String>

    @Update
    suspend fun updateTopic(topic: Topic)
}
