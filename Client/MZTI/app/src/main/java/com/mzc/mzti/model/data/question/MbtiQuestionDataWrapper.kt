package com.mzc.mzti.model.data.question

import com.mzc.mzti.model.data.mbti.MBTI

data class MbtiQuestionDataWrapper(
    val mbti: MBTI,
    val mbtiQuestionList: List<MbtiQuestionData>
)