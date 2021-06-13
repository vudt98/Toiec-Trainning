package com.example.toeictraining.base.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Part.TABLE_NAME)
data class Part(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = FIELD_ID) var id: Int = 0,
    @ColumnInfo(name = FIELD_DESCRIPTION) var description: String = "",
    @ColumnInfo(name = FIELD_COUNT) var count: Int = 0
) {
    companion object {
        const val TABLE_NAME = "part"
        const val FIELD_ID = "id"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_COUNT = "count"
    }
}