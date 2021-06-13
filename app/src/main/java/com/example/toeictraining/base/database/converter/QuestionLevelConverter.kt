package com.example.toeictraining.base.database.converter

import androidx.room.TypeConverter
import com.example.toeictraining.base.enums.QuestionLevel

class QuestionLevelConverter {
    @TypeConverter
    fun stringToQuestionLevel(value: String?): QuestionLevel {
        if (value == null) {
            return QuestionLevel.EASY
        }
        return QuestionLevel.valueOf(value)
    }

    @TypeConverter
    fun questionLevelToString(questionLevel: QuestionLevel): String? {
        return questionLevel.name
    }
}