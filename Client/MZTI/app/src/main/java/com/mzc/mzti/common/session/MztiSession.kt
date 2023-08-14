package com.mzc.mzti.common.session

import android.content.Context
import android.content.SharedPreferences
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.model.data.mbti.MBTI

private const val TAG: String = "MztiSession"

private const val LOGIN_PREF_NAME: String = "mzti"

private const val IS_LOGIN: String = "is_login"
private const val USER_ID: String = "user_id"
private const val GENERATE_TYPE: String = "generate_type"
private const val USER_TOKEN: String = "user_token"
private const val USER_NICKNAME: String = "user_nickname"
private const val USER_MBTI: String = "user_mbti"
private const val USER_PROFILE_IMG: String = "user_profile_img"

object MztiSession {

    private var _sharedPreferences: SharedPreferences? = null
    private val sharedPreferences: SharedPreferences get() = _sharedPreferences!!

    private var _editor: SharedPreferences.Editor? = null
    private val editor: SharedPreferences.Editor get() = _editor!!

    fun init(pContext: Context) {
        DLog.d(TAG, "init MZTI Session!")
        _sharedPreferences = pContext.getSharedPreferences(LOGIN_PREF_NAME, Context.MODE_PRIVATE)
        _editor = sharedPreferences.edit()
    }

    val isLogin: Boolean get() = sharedPreferences.getBoolean(IS_LOGIN, false)

    val userId: String get() = sharedPreferences.getString(USER_ID, "") ?: ""

    val generateType: String get() = sharedPreferences.getString(GENERATE_TYPE, "") ?: ""

    val userToken: String get() = sharedPreferences.getString(USER_TOKEN, "") ?: ""

    val userNickname: String get() = sharedPreferences.getString(USER_NICKNAME, "") ?: ""

    val userMbti: MBTI get() = MBTI.values()[sharedPreferences.getInt(USER_MBTI, 16)]

    val userProfileImg: String get() = sharedPreferences.getString(USER_PROFILE_IMG, "") ?: ""

    fun login(
        pUserId: String,
        pGenerateType: String,
        pUserToken: String,
        pUserNickname: String,
        pUserMBTI: MBTI,
        pUserProfileImg: String
    ) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(USER_ID, pUserId)
        editor.putString(GENERATE_TYPE, pGenerateType)
        editor.putString(USER_TOKEN, pUserToken)
        editor.putString(USER_NICKNAME, pUserNickname)
        editor.putInt(USER_MBTI, pUserMBTI.ordinal)
        editor.putString(USER_PROFILE_IMG, pUserProfileImg)
        editor.commit()
    }

    fun update(
        pUserNickname: String? = null,
        pUserMBTI: MBTI?,
        pUserProfileImg: String?
    ) {
        if (pUserNickname != null) {
            editor.putString(USER_NICKNAME, pUserNickname)
        }
        if (pUserMBTI != null) {
            editor.putInt(USER_MBTI, pUserMBTI.ordinal)
        }
        if (pUserProfileImg != null) {
            editor.putString(USER_PROFILE_IMG, pUserProfileImg)
        }

        editor.commit()
    }

    fun logout() {
        editor.clear()
        editor.commit()
    }

}