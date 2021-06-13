package com.example.toeictraining.base.database.converter

import androidx.room.TypeConverter

class ListIntConverter {
    @TypeConverter
    fun stringToListInt(value: String?): List<Int> {
        if (value == null) {
            return mutableListOf()
        }
        val result = mutableListOf<Int>()
        val splits = value.substring(1).split("|")
        for (split in splits) {
            result.add(split.toInt())
        }
        return result
    }

    @TypeConverter
    fun listIntToString(list: MutableList<Int>): String? {
        var string = ""
        for (i in list) {
            string += "|$i"
        }
        return string
    }
}