package com.example.toeictraining.base.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Exam.TABLE_NAME)
data class Exam(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = FIELD_ID) var id: Long = 0,
    @ColumnInfo(name = FIELD_LIST_QUESTION_ID) var questionIdList: MutableList<Int> = mutableListOf(),
    @ColumnInfo(name = FIELD_LIST_ANSWER) var answerList: MutableList<String> = mutableListOf(),
    @ColumnInfo(name = FIELD_TIME) var time: Int = 0,
    @ColumnInfo(name = FIELD_PART) var part: Int = 0,
    @ColumnInfo(name = FIELD_TIMESTAMP) var timestamp: String,
    @ColumnInfo(name = FIELD_SCORE) var score: Int
) {
    companion object {
        const val TABLE_NAME = "exam"
        const val FIELD_ID = "id"
        const val FIELD_LIST_QUESTION_ID = "list_question_id"
        const val FIELD_LIST_ANSWER = "list_answer"
        const val FIELD_TIME = "time"
        const val FIELD_PART = "part"
        const val FIELD_TIMESTAMP = "timestamp"
        const val FIELD_SCORE = "score"
    }
}
