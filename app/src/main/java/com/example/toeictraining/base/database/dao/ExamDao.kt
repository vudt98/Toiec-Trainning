package com.example.toeictraining.base.database.dao

import androidx.room.*
import com.example.toeictraining.base.entity.Exam

@Dao
interface ExamDao {
    @Query("SELECT * FROM exam")
    fun getAll(): List<Exam>

    @Query("SELECT * FROM exam WHERE id = :id")
    fun getExamById(id: Long): Exam

    @Query("SELECT * FROM exam WHERE id IN (:examIds)")
    fun loadAllByIds(examIds: IntArray): List<Exam>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exam: Exam): Long

    @Delete
    fun delete(exam: Exam)
}