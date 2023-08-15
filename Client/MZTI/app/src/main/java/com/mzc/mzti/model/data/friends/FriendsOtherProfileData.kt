package com.mzc.mzti.model.data.friends

import com.mzc.mzti.model.data.mbti.MBTI
import java.io.Serializable

data class FriendsOtherProfileData(
    val id: String,
    val nickname: String,
    val mbti: MBTI,
    val profileImg: String
) : Comparable<FriendsOtherProfileData>, Serializable {
    override fun compareTo(other: FriendsOtherProfileData): Int =
        compareValuesBy(this, other) { it.nickname }

}