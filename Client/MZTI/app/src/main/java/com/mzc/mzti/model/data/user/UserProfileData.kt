package com.mzc.mzti.model.data.user

import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.mbti.MbtiBadgeData

data class UserProfileData(
    val id: String,
    val nickname: String,
    val mbti: MBTI,
    val profileImg: String,
    val mbtiBadgeList: List<MbtiBadgeData>
)