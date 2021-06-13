package com.example.toeictraining.ui.fragments.test.do_test

import com.example.toeictraining.base.entity.Question

class QuestionStatus(
    var index: Int,
    var status: Status,
    var data: Question,
    var answer: String
) {
    enum class Status {
        NOT_DONE, DONE, MAIN
    }
}