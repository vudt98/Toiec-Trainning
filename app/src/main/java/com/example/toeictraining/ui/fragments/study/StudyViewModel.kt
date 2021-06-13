package com.example.toeictraining.ui.fragments.study

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.toeictraining.base.BaseViewModel
import com.example.toeictraining.base.entity.Topic
import com.example.toeictraining.base.entity.Word
import com.example.toeictraining.data.repository.TopicRepository
import com.example.toeictraining.data.repository.WordRepository
import com.example.toeictraining.di.ScopeNames
import com.example.toeictraining.utils.DateUtil
import com.example.toeictraining.utils.WordHelper
import com.example.toeictraining.utils.getPriorityWord
import com.example.toeictraining.utils.getProgressProperties
import kotlinx.coroutines.launch
import org.koin.core.get
import org.koin.core.qualifier.named

class StudyViewModel(
    private val wordRepository: WordRepository,
    private val topicRepository: TopicRepository
) : BaseViewModel() {

    var mainTopic: Topic = get(named(ScopeNames.EMPTY_TOPIC))

    private val words = mutableListOf<Word>()
    private val _word: MutableLiveData<Word> = MutableLiveData()

    val word: LiveData<Word> get() = _word

    override fun onCreate() {
        viewModelScope.launch {
            words.addAll(wordRepository.getWordsByTopic(mainTopic.id))
            changeWord()
        }
    }

    fun changeWord() {
        _word.postValue(words.getPriorityWord())
        updateTopic()
    }

    fun updateWordLevel(isKnown: Boolean) {
        word.value?.run {

            level = WordHelper.getNewLevel(level, isKnown)

            viewModelScope.launch {
                wordRepository.updateWord(this@run)
            }
        }
    }

    private fun updateTopic() = viewModelScope.launch {

        val progressProperties = words.getProgressProperties()
        mainTopic.apply {
            lastTime = DateUtil.getCurrentDate()
            total = progressProperties.first
            master = progressProperties.second
            newWord = progressProperties.third
        }
        topicRepository.updateTopic(mainTopic)
    }
}
