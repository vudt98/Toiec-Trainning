package com.example.toeictraining.data.repository

import com.example.toeictraining.base.database.dao.WordDao
import com.example.toeictraining.base.entity.Word

class WordRepositoryImpl(private val wordDao: WordDao) : WordRepository {

    override suspend fun getWords(): List<Word> = wordDao.getWords()

    override suspend fun getWordsByTopic(topicId: Int): List<Word> =
        wordDao.getWordsByTopic(topicId)

    override suspend fun updateWord(word: Word) = wordDao.updateWord(word)
}
