package com.example.toeictraining.data.model

import com.example.toeictraining.base.entity.Topic

data class Category(
    val name: String,
    val color: String,
    val topics: List<Topic>,
    var isExpanded: Boolean = false
)
