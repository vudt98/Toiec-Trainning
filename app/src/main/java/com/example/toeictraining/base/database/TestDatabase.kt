package com.example.toeictraining.base.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.toeictraining.base.database.converter.ListIntConverter
import com.example.toeictraining.base.database.converter.ListStringConverter
import com.example.toeictraining.base.database.converter.QuestionLevelConverter
import com.example.toeictraining.base.database.dao.QuestionDao
import com.example.toeictraining.base.entity.Question

@Database(entities = [Question::class], version = 1, exportSchema = false)
@TypeConverters(
    QuestionLevelConverter::class,
    ListIntConverter::class,
    ListStringConverter::class
)
abstract class TestDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context) =
            INSTANCE
                ?: getDatabase(
                    context
                ).also { INSTANCE = it }

        private fun getDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java, "test"
            ).build()
    }
}