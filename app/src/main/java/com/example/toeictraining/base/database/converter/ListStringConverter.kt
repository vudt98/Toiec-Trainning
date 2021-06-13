package com.example.toeictraining.base.database.converter

import androidx.room.TypeConverter

class ListStringConverter {
    @TypeConverter
    fun stringToListString(value: String?): MutableList<String> {
        if (value == null) {
            return mutableListOf()
        }
        val splits = value.substring(1).split("|")
        return splits.toMutableList()
    }

    @TypeConverter
    fun listStringToString(list: MutableList<String>): String? {
        var string = ""
        for (s in list) {
            string += "|$s"
        }
        return string
    }
}