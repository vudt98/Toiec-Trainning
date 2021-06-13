package com.example.toeictraining.base.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = Word.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = Topic::class,
            parentColumns = [Topic.FIELD_ID],
            childColumns = [Word.FIELD_TOPIC_ID]
        )
    ]
)
@Parcelize
data class Word(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = FIELD_ID) val id: Int,
    @ColumnInfo(name = FIELD_ORIGIN) val origin: String,
    @ColumnInfo(name = FIELD_EXPLANATION) val explanation: String,
    @ColumnInfo(name = FIELD_TYPE) val type: String,
    @ColumnInfo(name = FIELD_PRONUNCIATION) val pronunciation: String? = null,
    @ColumnInfo(name = FIELD_IMAGE_URL) val imageUrl: String,
    @ColumnInfo(name = FIELD_EXAMPLE) val example: String,
    @ColumnInfo(name = FIELD_EXAMPLE_TRANSLATION) val exampleTranslate: String? = null,
    @ColumnInfo(name = FIELD_TOPIC_ID) val topicId: Int,
    @ColumnInfo(name = FIELD_LEVEL) var level: Int = 0,
    @ColumnInfo(name = FIELD_SOUND) val sound: String? = null
) : Parcelable {

    fun getSoundLink(): String =
        "https://600tuvungtoeic.com/audio/${origin.replace(' ', '_')}.mp3"

    companion object {
        const val TABLE_NAME = "tbl_word"
        const val FIELD_ID = "id"
        const val FIELD_ORIGIN = "origin"
        const val FIELD_EXPLANATION = "explanation"
        const val FIELD_TYPE = "type"
        const val FIELD_PRONUNCIATION = "pronunciation"
        const val FIELD_IMAGE_URL = "image_url"
        const val FIELD_EXAMPLE = "example"
        const val FIELD_EXAMPLE_TRANSLATION = "example_translation"
        const val FIELD_TOPIC_ID = "topic_id"
        const val FIELD_LEVEL = "level"
        const val FIELD_SOUND = "sound"
    }
}
