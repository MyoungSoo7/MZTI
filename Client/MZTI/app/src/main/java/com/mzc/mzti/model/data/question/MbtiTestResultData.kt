package com.mzc.mzti.model.data.question

import com.mzc.mzti.model.data.mbti.MbtiBadgeData
import com.mzc.mzti.model.data.mbti.MbtiSize

data class MbtiTestResultData(
    val mbtiBadgeData: MbtiBadgeData,
    val totalScore: Int
)
