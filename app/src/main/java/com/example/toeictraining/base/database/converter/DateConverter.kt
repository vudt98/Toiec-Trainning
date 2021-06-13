package com.example.toeictraining.base.database.converter

import androidx.room.TypeConverter
import com.example.toeictraining.base.enums.QuestionLevel
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    @TypeConverter
    fun stringToDate(value: String?): Date? {
        if (value == null) {
            return null
        }
        return SimpleDateFormat.getDateTimeInstance().parse(value)
    }

    @TypeConverter
    fun DateToString(date: Date?): String? {
        if (date == null) {
            return null
        }
        return SimpleDateFormat.getDateTimeInstance().format(date)
    }
}