package com.mzc.mzti.common.util

import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.mbti.getMBTI
import com.mzc.mzti.model.data.network.NetworkResult
import com.mzc.mzti.model.data.user.UserInfoData
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale

private const val TAG: String = "JsonParserUtil"

private const val KEY_RESULT_CODE: String = "result_code"
private const val KEY_RESULT_DATA: String = "result_data"

private const val KEY_LOGIN_ID: String = "loginId"
private const val KEY_GENERATE_TYPE: String = "generateType"
private const val KEY_ACCESS_TOKEN: String = "accessToken"

private const val KEY_USER_NAME: String = "username"
private const val KEY_MBTI: String = "mbti"
private const val KEY_PROFILE_IMG: String = "profileImage"

class JsonParserUtil {

    // region Base Function
    fun getString(jsonObj: JSONObject, key: String, strDefault: String = "") =
        if (jsonObj.has(key) && !jsonObj.isNull(key)) jsonObj.getString(key)
        else strDefault

    fun getBoolean(jsonObj: JSONObject, key: String, default: Boolean = false): Boolean {
        return if (jsonObj.has(key) && !jsonObj.isNull(key)) {
            val value = jsonObj.getString(key).trim()

            when (value.lowercase(Locale.ROOT)) {
                "yes",
                "true",
                "y",
                "1" -> true

                else -> false
            }
        } else {
            default
        }
    }

    fun getInt(jsonObj: JSONObject, key: String, intDefault: Int = -1): Int {
        return if (jsonObj.has(key) && !jsonObj.isNull(key)) {
            val value = jsonObj.getString(key).trim()

            try {
                value.toInt()
            } catch (e: NumberFormatException) {
                intDefault
            }
        } else {
            intDefault
        }
    }

    fun getLong(jsonObj: JSONObject, key: String, longDefault: Long = -1): Long {
        return if (jsonObj.has(key) && !jsonObj.isNull(key)) {
            val value = jsonObj.getString(key).trim()

            try {
                value.toLong()
            } catch (e: NumberFormatException) {
                longDefault
            }
        } else {
            longDefault
        }
    }

    fun getFloat(jsonObj: JSONObject, key: String, floatDefault: Float = -1f): Float {
        return if (jsonObj.has(key) && !jsonObj.isNull(key)) {
            val value = jsonObj.getString(key).trim()

            try {
                value.toFloat()
            } catch (e: NumberFormatException) {
                floatDefault
            }
        } else {
            floatDefault
        }
    }

    fun getDouble(jsonObj: JSONObject, key: String, doubleDefault: Double = -1.0): Double {
        return if (jsonObj.has(key) && !jsonObj.isNull(key)) {
            val value = jsonObj.getString(key).trim()

            try {
                value.toDouble()
            } catch (e: NumberFormatException) {
                doubleDefault
            }
        } else {
            doubleDefault
        }
    }

    fun getJsonObject(jsonObject: JSONObject, key: String): JSONObject? {
        return if (jsonObject.has(key) && !jsonObject.isNull(key)) {
            try {
                jsonObject.getJSONObject(key)
            } catch (e: JSONException) {
                null
            }
        } else {
            null
        }
    }

    fun getJSONArray(jsonObject: JSONObject, key: String): JSONArray? {
        return if (jsonObject.has(key) && !jsonObject.isNull(key)) {
            try {
                jsonObject.getJSONArray(key)
            } catch (e: JSONException) {
                null
            }
        } else {
            null
        }
    }
    // endregion Base Function

    fun getLoginResponseDTO(jsonRoot: JSONObject): UserInfoData? {
        val resultCode = getInt(jsonRoot, KEY_RESULT_CODE)
        if (resultCode != 200) {
            return null
        }

        val resultData = getJsonObject(jsonRoot, KEY_RESULT_DATA)
        return if (resultData != null) {
            val loginId: String = getString(resultData, KEY_LOGIN_ID)
            val generateType: String = getString(resultData, KEY_GENERATE_TYPE)
            val accessToken: String = getString(resultData, KEY_ACCESS_TOKEN)

            UserInfoData(
                id = loginId,
                generateType = generateType,
                token = accessToken,
                nickname = "",
                mbti = MBTI.MZTI,
                profileImg = ""
            )
        } else {
            null
        }
    }

    fun getUserInfoData(jsonRoot: JSONObject): UserInfoData? {
        val resultCode = getInt(jsonRoot, KEY_RESULT_CODE)
        if (resultCode != 200) {
            return null
        }

        val resultData = getJsonObject(jsonRoot, KEY_RESULT_DATA)
        return if (resultData != null) {
            val loginId = getString(resultData, KEY_LOGIN_ID)
            val generateType = getString(resultData, KEY_GENERATE_TYPE)
            val accessToken = getString(resultData, KEY_ACCESS_TOKEN)
            val nickname = getString(resultData, KEY_USER_NAME)
            val mbti = getString(resultData, KEY_MBTI)
            val profileImg = getString(resultData, KEY_PROFILE_IMG)

            UserInfoData(
                id = loginId,
                generateType = generateType,
                token = accessToken,
                nickname = nickname,
                mbti = getMBTI(mbti),
                profileImg = profileImg
            )
        } else {
            null
        }
    }

    fun getIdCheckResult(jsonRoot: JSONObject): NetworkResult<Boolean> {
        val resultCode = getInt(jsonRoot, KEY_RESULT_CODE)
        if (resultCode != 200) {
            return NetworkResult.Fail("API Request Fail, resultCode=$resultCode")
        }

        return when (val resultData = getString(jsonRoot, KEY_RESULT_DATA)) {
            "중복" -> NetworkResult.Success(false)
            "중복아님" -> NetworkResult.Success(true)
            else -> NetworkResult.Fail("resultData=$resultData")
        }
    }

    fun getSignUpResponse(jsonRoot: JSONObject): NetworkResult<Boolean> {
        val resultCode = getInt(jsonRoot, KEY_RESULT_CODE)
        return if (resultCode != 200) {
            val failMsg = getString(jsonRoot, KEY_RESULT_DATA)
            NetworkResult.Fail(failMsg)
        } else {
            NetworkResult.Success(true)
        }
    }

}