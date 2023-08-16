package com.mzc.mzti.model.data.question

data class MbtiQuestionData(
    val questionContent: String,
    val questionType: Int,
    val answerList: List<String>,
    val answerIdx: Int
)
