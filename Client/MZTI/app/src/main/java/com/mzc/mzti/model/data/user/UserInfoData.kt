package com.mzc.mzti.model.data.user

import com.mzc.mzti.model.data.mbti.MBTI
import java.io.Serializable

private const val TAG: String = "UserInfoData"

data class UserInfoData(
    val id: String,
    val generateType: String,
    val token: String,
    val nickname: String,
    val mbti: MBTI,
    val profileImg: String
) : Serializable {

    override fun toString(): String {
        return "$TAG{" +
                "id=$id, " +
                "nickname=$nickname, " +
                "mbti=${mbti.name}}"
    }

}
