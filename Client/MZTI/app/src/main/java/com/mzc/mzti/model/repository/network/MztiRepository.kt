package com.mzc.mzti.model.repository.network

import android.content.Context
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseNetworkRepository
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.common.util.FileUtil
import com.mzc.mzti.model.data.compare.CompareMbtiData
import com.mzc.mzti.model.data.friends.FriendsDataWrapper
import com.mzc.mzti.model.data.friends.FriendsOtherProfileData
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.network.NetworkResult
import com.mzc.mzti.model.data.question.MbtiAnswerData
import com.mzc.mzti.model.data.question.MbtiQuestionDataWrapper
import com.mzc.mzti.model.data.question.MbtiTestResultData
import com.mzc.mzti.model.data.sign.SignUpData
import com.mzc.mzti.model.data.user.UserInfoData
import com.mzc.mzti.model.data.user.UserProfileData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

private const val TAG: String = "MztiRepository"
private const val MEDIA_TYPE: String = "application/octet-stream"

class MztiRepository(
    private val context: Context
) : BaseNetworkRepository(context, TAG) {

    private val fileUtil = FileUtil(context)

    suspend fun makeLoginRequest(
        pLoginId: String,
        pLoginPw: String
    ): NetworkResult<UserInfoData> {
        return withContext(Dispatchers.IO) {
            val hsParams = hashMapOf<String, Any>().apply {
                put("loginId", pLoginId)
                put("password", pLoginPw)
            }

            val loginUrl = "${BASE_URL}api/members/login"
            val message = sendRequestToMztiServer(loginUrl, hsParams, POST)
            DLog.d(TAG, "message=$message")

            if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)
                    val data = jsonParserUtil.getLoginResponseDTO(jsonRoot)

                    if (data != null) {
                        NetworkResult.Success(data)
                    } else {
                        NetworkResult.Fail("resultCode 확인 필요")
                    }
                } catch (e: JSONException) {
                    DLog.e(TAG, e.stackTraceToString())
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Fail(
                    context.getString(R.string.login_fail_msg)
                )
            }
        }
    }

    suspend fun makeCheckIdRequest(pSignUpId: String): NetworkResult<Boolean> {
        return withContext(Dispatchers.IO) {
            val hsParams = hashMapOf<String, Any>().apply {
                put("loginId", pSignUpId)
            }

            val checkIdUrl = "${BASE_URL}api/members/isDuplicate"
            val message = sendRequestToMztiServer(checkIdUrl, hsParams, GET)
            DLog.d(TAG, "message=$message")

            if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)
                    jsonParserUtil.getIdCheckResult(jsonRoot)
                } catch (e: JSONException) {
                    DLog.e(TAG, e.stackTraceToString())
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Fail(
                    context.getString(R.string.api_connection_fail_msg)
                )
            }
        }
    }

    suspend fun makeUserInfoRequest(
        pUserToken: String,
        pGenerateType: String
    ): NetworkResult<UserInfoData> {
        return withContext(Dispatchers.IO) {
            val hsParams = hashMapOf<String, Any>()

            val userInfoUrl = "${BASE_URL}api/members"
            val message = sendRequestToMztiServer(
                userInfoUrl,
                hsParams,
                GET,
                authorization = "$pGenerateType $pUserToken"
            )

            if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)
                    val data = jsonParserUtil.getUserInfoData(jsonRoot)

                    if (data != null) {
                        NetworkResult.Success(data)
                    } else {
                        NetworkResult.Fail("resultCode 확인 필요")
                    }
                } catch (e: JSONException) {
                    DLog.e(TAG, e.stackTraceToString())
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Fail(
                    context.getString(R.string.api_connection_fail_msg)
                )
            }
        }
    }

    suspend fun makeSignUpRequest(
        pSignUpData: SignUpData
    ): NetworkResult<Boolean> {
        return withContext(Dispatchers.IO) {
            val hsParams = hashMapOf<String, Any>().apply {
                put("loginId", pSignUpData.signUpId)
                put("password", pSignUpData.signUpPw)
                put("username", pSignUpData.signUpNickname)
                put("mbti", pSignUpData.signUpMbti.name)
            }

            val signUpUrl = "${BASE_URL}api/members/signup"
            val message = sendRequestToMztiServer(signUpUrl, hsParams, POST)

            if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)
                    jsonParserUtil.getSignUpResponse(jsonRoot)
                } catch (e: JSONException) {
                    DLog.e(TAG, e.stackTraceToString())
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Fail(
                    context.getString(R.string.api_connection_fail_msg)
                )
            }
        }
    }

    suspend fun makeFriendListRequest(
        pUserToken: String,
        pGenerateType: String
    ): NetworkResult<List<FriendsDataWrapper>> {
        return withContext(Dispatchers.IO) {
            val hsParams = hashMapOf<String, Any>()

            val friendListUrl = "${BASE_URL}api/members/friendList"
            val message = sendRequestToMztiServer(
                friendListUrl,
                hsParams,
                GET,
                authorization = "$pGenerateType $pUserToken"
            )
            DLog.d("${TAG}_makeFriendListRequest", "message=$message")

            if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)
                    val data = jsonParserUtil.getFriendListResponse(jsonRoot)
                    NetworkResult.Success(data)
                } catch (e: JSONException) {
                    DLog.e(TAG, e.stackTraceToString())
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Fail(
                    context.getString(R.string.api_connection_fail_msg)
                )
            }
        }
    }

    suspend fun makeAddFriendRequest(
        pFriendId: String,
        pUserToken: String,
        pGenerateType: String
    ): NetworkResult<FriendsOtherProfileData> {
        return withContext(Dispatchers.IO) {
            val hsParams = hashMapOf<String, Any>().apply {
                put("loginId", pFriendId)
            }

            val addFriendUrl = "${BASE_URL}api/members/addFriend"
            val message = sendRequestToMztiServer(
                addFriendUrl,
                hsParams,
                POST,
                authorization = "$pGenerateType $pUserToken"
            )

            if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)
                    jsonParserUtil.getAddFriendResponse(jsonRoot)
                } catch (e: JSONException) {
                    DLog.e(TAG, e.stackTraceToString())
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Fail(
                    context.getString(R.string.api_connection_fail_msg)
                )
            }
        }
    }

    suspend fun makeRemoveFriendRequest(
        pFriendId: String,
        pUserToken: String,
        pGenerateType: String
    ): NetworkResult<String> {
        return withContext(Dispatchers.IO) {
            val hsParams = hashMapOf<String, Any>().apply {
                put("loginId", pFriendId)
            }

            val removeFriendUrl = "${BASE_URL}api/members/deleteFriend"
            val message = sendRequestToMztiServer(
                removeFriendUrl,
                hsParams,
                POST,
                authorization = "$pGenerateType $pUserToken"
            )

            if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)
                    jsonParserUtil.getRemoveFriendResponse(jsonRoot)
                } catch (e: JSONException) {
                    DLog.e(TAG, e.stackTraceToString())
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Fail(
                    context.getString(R.string.api_connection_fail_msg)
                )
            }
        }
    }

    suspend fun makeUserProfileRequest(
        pUserToken: String,
        pGenerateType: String
    ): NetworkResult<UserProfileData> {
        return withContext(Dispatchers.IO) {
            val hsParams = hashMapOf<String, Any>()

            val userProfileUrl = "${BASE_URL}api/members/getProfile"
            val message = sendRequestToMztiServer(
                userProfileUrl,
                hsParams,
                GET,
                authorization = "$pGenerateType $pUserToken"
            )

            if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)
                    jsonParserUtil.getUserProfileResponse(jsonRoot)
                } catch (e: JSONException) {
                    DLog.e(TAG, e.stackTraceToString())
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Fail(
                    context.getString(R.string.api_connection_fail_msg)
                )
            }
        }
    }

    suspend fun makeQuestionRequest(
        pQuestionCount: Int,
        pMbti: MBTI,
        pUserToken: String,
        pGenerateType: String
    ): NetworkResult<MbtiQuestionDataWrapper> {
        return withContext(Dispatchers.IO) {
            val hsParams = hashMapOf<String, Any>().apply {
                put("questionCount", pQuestionCount)
                put("mbti", pMbti.name)
            }

            val questionUrl = "${BASE_URL}api/question"
            val message = sendRequestToMztiServer(
                questionUrl,
                hsParams,
                GET,
                authorization = "$pGenerateType $pUserToken"
            )

            if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)
                    jsonParserUtil.getQuestionResponse(jsonRoot)
                } catch (e: JSONException) {
                    DLog.e(TAG, e.stackTraceToString())
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Fail(
                    context.getString(R.string.api_connection_fail_msg)
                )
            }
        }
    }

    suspend fun makeQuestionResult(
        pMbti: MBTI,
        pMBtiAnswerList: List<MbtiAnswerData>,
        pUserToken: String,
        pGenerateType: String
    ): NetworkResult<MbtiTestResultData> {
        return withContext(Dispatchers.IO) {
            val answerArray = JSONArray().apply {
                for (answerData in pMBtiAnswerList) {
                    put(JSONObject().apply {
                        put("type", answerData.questionType)
                        put("correctFlag", answerData.answerFlag)
                    })
                }
            }

            val hsParams = hashMapOf<String, Any>().apply {
                put("mbti", pMbti.name)
                put("answerList", answerArray)
            }

            val questionResultUrl = "${BASE_URL}api/question/result"
            val message = sendRequestToMztiServer(
                questionResultUrl,
                hsParams,
                POST,
                authorization = "$pGenerateType $pUserToken"
            )
            DLog.d(TAG, "questionResult=$message")

            if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)
                    jsonParserUtil.getQuestionResultResponse(jsonRoot, pMbti)
                } catch (e: JSONException) {
                    DLog.e(TAG, e.stackTraceToString())
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Fail(
                    context.getString(R.string.api_connection_fail_msg)
                )
            }
        }
    }

    suspend fun makeEditProfileRequest(
        pUserToken: String,
        pGenerateType: String,
        pUserNickname: String? = null,
        pUserMBTI: MBTI? = null,
        pUserProfileImgPath: String? = null
    ): NetworkResult<UserInfoData> {
        return withContext(Dispatchers.IO) {
            DLog.d(
                "${TAG}_editProfile",
                "username=$pUserNickname, mbti=${pUserMBTI?.name}, profileImg=$pUserProfileImgPath"
            )
            val editProfileUrl = "${BASE_URL}api/members/edit"

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

            if (pUserNickname != null || pUserMBTI != null) {
                val userInfoObj = JSONObject().apply {
                    if (pUserNickname != null) {
                        put("username", pUserNickname)
                    }
                    if (pUserMBTI != null) {
                        put("mbti", pUserMBTI.name)
                    }
                }
                val userInfoJsonFile = fileUtil.jsonObject2JsonFile(userInfoObj)

                multipartBody.addFormDataPart(
                    "userInfo",
                    userInfoJsonFile.name,
                    userInfoJsonFile.asRequestBody("application/json".toMediaType())
                )
            }
            if (pUserProfileImgPath != null) {
                val profileImgFile = File(pUserProfileImgPath)
                DLog.d(TAG, "imgFileSize=${profileImgFile.length() / 1024}")

                multipartBody.addFormDataPart(
                    "profileImage",
                    profileImgFile.name,
                    profileImgFile.asRequestBody(MEDIA_TYPE.toMediaType())
                )
            }
            val body = multipartBody.build()

            val request = Request.Builder()
                .addHeader("Authorization", "$pGenerateType $pUserToken")
                .url(editProfileUrl)
                .post(body)
                .build()

            val response = okHttpClient.newCall(request).execute()

            DLog.d(TAG, "responseCode=${response.code}, message=${response.message}")
            if (response.isSuccessful) {
                val responseBody = response.body?.string()

                if (responseBody != null) {
                    DLog.d(TAG, "responseBody=$responseBody")
                    val jsonRoot = JSONObject(responseBody)
                    jsonParserUtil.getEditProfileResponse(jsonRoot, pUserToken, pGenerateType)
                } else {
                    NetworkResult.Fail("ResponseBody is Null!")
                }
            } else {
                NetworkResult.Fail("ResultCode=${response.code}")
            }
        }
    }

    suspend fun makeMbtiInfoRequest(
        pMbti: MBTI,
        pUserToken: String,
        pGenerateType: String,
    ): NetworkResult<List<CompareMbtiData>> {
        return withContext(Dispatchers.IO) {
            val hsParams = hashMapOf<String, Any>().apply {
                put("mbti", pMbti.name)
            }

            val mbtiInfoUrl = "${BASE_URL}api/info"
            val message = sendRequestToMztiServer(
                mbtiInfoUrl,
                hsParams,
                GET,
                authorization = "$pGenerateType $pUserToken"
            )

            if (message.isNotEmpty()) {
                try {
                    val jsonRoot = JSONObject(message)
                    jsonParserUtil.getMbtiInfoResponse(jsonRoot)
                } catch (e: JSONException) {
                    DLog.e(TAG, e.stackTraceToString())
                    NetworkResult.Error(e)
                }
            } else {
                NetworkResult.Fail(
                    context.getString(R.string.api_connection_fail_msg)
                )
            }
        }
    }

}