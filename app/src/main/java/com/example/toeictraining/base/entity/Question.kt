package com.example.toeictraining.base.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.toeictraining.base.enums.QuestionLevel

@Entity(tableName = Question.TABLE_NAME)
data class Question(
    @PrimaryKey @ColumnInfo(name = FIELD_ID) var id: Int = 0,
    @ColumnInfo(name = FIELD_ID_PART) var idPart: Int = 0,
    @ColumnInfo(name = FIELD_GROUP_QUESTION_ID) var groupQuestionId: Int? = null,
    @ColumnInfo(name = FIELD_SCRIPT) var script: String? = null,
    @ColumnInfo(name = FIELD_CONTENT) var content: String? = null,
    @ColumnInfo(name = FIELD_A) var a: String = "",
    @ColumnInfo(name = FIELD_B) var b: String = "",
    @ColumnInfo(name = FIELD_C) var c: String = "",
    @ColumnInfo(name = FIELD_D) var d: String? = null,
    @ColumnInfo(name = FIELD_SOUND_LINK) var soundLink: String? = null,
    @ColumnInfo(name = FIELD_IMAGE_LINK) var imageLink: String? = null,
    @ColumnInfo(name = FIELD_TYPE) var type: QuestionLevel = QuestionLevel.EASY,
    @ColumnInfo(name = FIELD_CORRECT_ANSWER) var correctAnswer: String = ""

) {
    companion object {
        const val TABLE_NAME = "question"
        const val FIELD_ID = "id"
        const val FIELD_GROUP_QUESTION_ID = "groupQuestionID"
        const val FIELD_SCRIPT = "script"
        const val FIELD_IMAGE_LINK = "imageLink"
        const val FIELD_CONTENT = "content"
        const val FIELD_A = "a"
        const val FIELD_B = "b"
        const val FIELD_C = "c"
        const val FIELD_D = "d"
        const val FIELD_SOUND_LINK = "soundLink"
        const val FIELD_TYPE = "type"
        const val FIELD_CORRECT_ANSWER = "correctAnswer"
        const val FIELD_ID_PART = "idPart"
    }
}
