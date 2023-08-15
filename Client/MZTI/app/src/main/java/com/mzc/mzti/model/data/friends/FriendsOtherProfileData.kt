package com.mzc.mzti.model.data.friends

import com.mzc.mzti.model.data.mbti.MBTI

data class FriendsOtherProfileData(
    val id: String,
    val nickname: String,
    val mbti: MBTI,
    val profileImg: String
)