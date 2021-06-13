package com.example.toeictraining.utils

import com.example.toeictraining.base.entity.Word
import com.example.toeictraining.utils.WordHelper.LEVEL_MAX
import com.example.toeictraining.utils.WordHelper.LEVEL_MIN

object WordHelper {
    const val LEVEL_MIN = 0
    const val LEVEL_1 = 1
    const val LEVEL_2 = 2
    const val LEVEL_3 = 3
    const val LEVEL_MAX = 4
    const val NEVER = "Chưa học"
    const val REVIEWING = "Đang ôn tập"
    const val MASTER = "Nhớ như in"

    fun getLevelContent(level: Int) = when (level) {
        LEVEL_MIN -> NEVER
        LEVEL_1, LEVEL_2, LEVEL_3 -> REVIEWING
        LEVEL_MAX -> MASTER
        else -> NEVER
    }

    fun getNewLevel(level: Int, isKnown: Boolean?): Int {
        isKnown ?: return level
        val delta = if (isKnown) 1 else -1
        val newLevel = level + delta
        return when {
            newLevel < LEVEL_MIN -> LEVEL_MIN
            newLevel > LEVEL_MAX -> LEVEL_MAX
            else -> newLevel
        }
    }

    fun getLevel(): Int {
        val random = Math.random() * 100
        return when {
            random < 5 -> LEVEL_MAX
            random < 15 -> LEVEL_3
            random < 30 -> LEVEL_2
            random < 60 -> LEVEL_1
            else -> LEVEL_MIN
        }
    }
}

fun List<Word>.getPriorityWord(): Word {
    var filteredWords: List<Word>
    do {
        val level = WordHelper.getLevel()
        filteredWords = filter { it.level == level }

    } while (filteredWords.isNullOrEmpty())

    return filteredWords.random()
}

fun List<Word>.getProgressProperties(): Triple<Int, Int, Int> {
    val maxProgress = size
    val mainProgress = filter { it.level == LEVEL_MAX }.size
    val secondProgress = filter { it.level == LEVEL_MIN }.size
    return Triple(maxProgress, mainProgress, secondProgress)
}
