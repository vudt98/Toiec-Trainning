package com.example.toeictraining.base.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.toeictraining.base.entity.Part
import com.example.toeictraining.base.entity.Question

@Dao
interface PartDao {
    @Query("SELECT * FROM part")
    fun getAll(): List<Part>

    @Query("SELECT * FROM part WHERE id IN (:partIds)")
    fun loadAllByIds(partIds: IntArray): List<Part>

    @Insert
    fun insertAll(vararg part: Part)

    @Delete
    fun delete(part: Part)
}