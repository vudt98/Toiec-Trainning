package com.example.toeictraining.base.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.toeictraining.base.database.converter.ListIntConverter
import com.example.toeictraining.base.database.converter.ListStringConverter
import com.example.toeictraining.base.database.converter.QuestionLevelConverter
import com.example.toeictraining.base.database.dao.*
import com.example.toeictraining.base.entity.*

private const val DATABASE_NAME = "toeic_600.db"
private const val DATABASE_VERSION = 2

@Database(
    entities = [Topic::class, Word::class, Question::class, Part::class, Exam::class],
    version = DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(
    QuestionLevelConverter::class,
    ListIntConverter::class,
    ListStringConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao
    abstract fun topicDao(): TopicDao
    abstract fun questionDao(): QuestionDao
    abstract fun partDao(): PartDao
    abstract fun examDao(): ExamDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: getDatabase(context).also { INSTANCE = it }

        private fun getDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .createFromAsset("databases/$DATABASE_NAME")
                .fallbackToDestructiveMigration()
                .build()
    }
}
