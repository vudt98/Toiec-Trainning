package com.example.toeictraining.ui.fragments.vocabulary

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.toeictraining.base.BaseViewModel
import com.example.toeictraining.data.model.Category
import com.example.toeictraining.data.repository.TopicRepository

class VocabularyViewModel(private val topicRepository: TopicRepository) : BaseViewModel() {

    val categories: LiveData<List<Category>> = liveData {
        try {
            emit(topicRepository.getCategories())
        } catch (e: Exception) {
            emit(emptyList())
            message.value = e.toString()
            e.printStackTrace()
        }
    }
}
