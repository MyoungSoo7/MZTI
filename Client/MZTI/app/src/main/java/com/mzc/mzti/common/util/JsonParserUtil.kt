package com.mzc.mzti.common.util

import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.model.data.friends.FriendsDataWrapper
import com.mzc.mzti.model.data.friends.FriendsLayoutType
import com.mzc.mzti.model.data.friends.FriendsMyProfileData
import com.mzc.mzti.model.data.friends.FriendsOtherProfileData
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.mbti.MbtiBadgeData
import com.mzc.mzti.model.data.mbti.MbtiSize
import com.mzc.mzti.model.data.mbti.getMBTI
import com.mzc.mzti.model.data.network.NetworkResult
import com.mzc.mzti.model.data.user.UserInfoData
import com.mzc.mzti.model.data.user.UserProfileData
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
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

private const val KEY_FRIEND_LIST: String = "friendlist"

private const val KEY_TEST_RESULT: String = "testResult"
private const val KEY_IS_FLAG: String = "isFlag"
private const val KEY_SIZE: String = "size"

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
            val nickname: String = getString(resultData, KEY_USER_NAME)
            val strMbti: String = getString(resultData, KEY_MBTI)
            val profileImg: String = getString(resultData, KEY_PROFILE_IMG)

            UserInfoData(
                id = loginId,
                generateType = generateType,
                token = accessToken,
                nickname = nickname,
                mbti = getMBTI(strMbti),
                profileImg = profileImg
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

    fun getFriendListResponse(jsonRoot: JSONObject): List<FriendsDataWrapper> {
        val ret = arrayListOf(
            FriendsDataWrapper(
                FriendsLayoutType.MY_PROFILE,
                FriendsMyProfileData(
                    nickname = MztiSession.userNickname,
                    mbti = MztiSession.userMbti,
                    profileImg = MztiSession.userProfileImg
                )
            )
        )

        val resultCode = getInt(jsonRoot, KEY_RESULT_CODE)
        if (resultCode != 200) {
            return ret
        }

        val otherProfileList = arrayListOf<FriendsOtherProfileData>()

        val resultData = getJsonObject(jsonRoot, KEY_RESULT_DATA)
        if (resultData != null) {
            val friendListArray = getJSONArray(resultData, KEY_FRIEND_LIST)

            if (friendListArray != null) {
                for (idx in 0 until friendListArray.length()) {
                    if (!friendListArray.isNull(idx)) {
                        val obj = friendListArray.getJSONObject(idx)

                        if (obj != null) {
                            val loginId = getString(obj, KEY_LOGIN_ID)
                            val userName = getString(obj, KEY_USER_NAME)
                            val profileImg = getString(obj, KEY_PROFILE_IMG)
                            val strMbti = getString(obj, KEY_MBTI)

                            otherProfileList.add(
                                FriendsOtherProfileData(
                                    id = loginId,
                                    nickname = userName,
                                    mbti = getMBTI(strMbti),
                                    profileImg = profileImg
                                )
                            )
                        }
                    }
                }
            }
        }
        otherProfileList.sort()

        ret.add(
            FriendsDataWrapper(
                FriendsLayoutType.FRIEND_COUNT,
                otherProfileList.size
            )
        )

        for (otherProfile in otherProfileList) {
            ret.add(
                FriendsDataWrapper(
                    FriendsLayoutType.OTHER_PROFILE,
                    otherProfile
                )
            )
        }

        return ret
    }

    fun getAddFriendResponse(jsonRoot: JSONObject): NetworkResult<FriendsOtherProfileData> {
        val resultCode = getInt(jsonRoot, KEY_RESULT_CODE)
        if (resultCode != 200) {
            return NetworkResult.Fail("API Request Fail, resultCode=$resultCode")
        }

        val resultData = getJsonObject(jsonRoot, KEY_RESULT_DATA)
        return if (resultData != null) {
            val loginId = getString(resultData, KEY_LOGIN_ID)
            val userName = getString(resultData, KEY_USER_NAME)
            val profileImg = getString(resultData, KEY_PROFILE_IMG)
            val strMBTI = getString(resultData, KEY_MBTI)

            NetworkResult.Success(
                FriendsOtherProfileData(
                    id = loginId,
                    nickname = userName,
                    mbti = getMBTI(strMBTI),
                    profileImg = profileImg
                )
            )
        } else {
            return NetworkResult.Fail("ResultData=$resultData")
        }
    }

    fun getRemoveFriendResponse(jsonRoot: JSONObject): NetworkResult<String> {
        val resultCode = getInt(jsonRoot, KEY_RESULT_CODE)
        val resultData = getString(jsonRoot, KEY_RESULT_DATA)
        return if (resultCode == 200) {
            NetworkResult.Success(resultData)
        } else {
            NetworkResult.Fail(resultData)
        }
    }

    fun getUserProfileResponse(jsonRoot: JSONObject): NetworkResult<UserProfileData> {
        val resultCode = getInt(jsonRoot, KEY_RESULT_CODE)
        if (resultCode != 200) {
            return NetworkResult.Fail("API Request Fail, resultCode=$resultCode")
        }

        val resultData = getJsonObject(jsonRoot, KEY_RESULT_DATA)
        return if (resultData != null) {
            val loginId = getString(resultData, KEY_LOGIN_ID)
            val userName = getString(resultData, KEY_USER_NAME)
            val profileImg = getString(resultData, KEY_PROFILE_IMG)
            val strMBTI = getString(resultData, KEY_MBTI)

            val mbtiBadgeList = arrayListOf<MbtiBadgeData>()
            val testResultArray = getJSONArray(resultData, KEY_TEST_RESULT)
            if (testResultArray != null) {
                for (idx in 0 until testResultArray.length()) {
                    if (!testResultArray.isNull(idx)) {
                        val obj = testResultArray.getJSONObject(idx)

                        if (obj != null) {
                            val strBadgeMbti = getString(obj, KEY_MBTI)
                            val isFlag = getBoolean(obj, KEY_IS_FLAG)
                            val sizeArray = getJSONArray(obj, KEY_SIZE)

                            if (sizeArray != null) {
                                var mbtiSize0 = 0
                                var mbtiSize1 = 0
                                var mbtiSize2 = 0
                                var mbtiSize3 = 0

                                try {
                                    mbtiSize0 = sizeArray.getInt(0)
                                    mbtiSize1 = sizeArray.getInt(1)
                                    mbtiSize2 = sizeArray.getInt(2)
                                    mbtiSize3 = sizeArray.getInt(3)
                                } catch (e: Exception) {
                                    DLog.e(TAG, e.stackTraceToString())
                                }

                                mbtiBadgeList.add(
                                    MbtiBadgeData(
                                        mbti = getMBTI(strBadgeMbti),
                                        isFlag = isFlag,
                                        mbtiSize0 = MbtiSize.values()[mbtiSize0],
                                        mbtiSize1 = MbtiSize.values()[mbtiSize1],
                                        mbtiSize2 = MbtiSize.values()[mbtiSize2],
                                        mbtiSize3 = MbtiSize.values()[mbtiSize3]
                                    )
                                )
                            }
                        }
                    }
                }
            }

            val userProfileData = UserProfileData(
                id = loginId,
                nickname = userName,
                mbti = getMBTI(strMBTI),
                profileImg = profileImg,
                mbtiBadgeList = mbtiBadgeList
            )
            NetworkResult.Success(userProfileData)
        } else {
            NetworkResult.Fail("ResultData=$resultData")
        }
    }

}