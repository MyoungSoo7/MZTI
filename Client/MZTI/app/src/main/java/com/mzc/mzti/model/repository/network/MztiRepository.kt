package com.mzc.mzti.model.repository.network

import android.content.Context
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseNetworkRepository
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.model.data.network.NetworkResult
import com.mzc.mzti.model.data.sign.SignUpData
import com.mzc.mzti.model.data.user.UserInfoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

private const val TAG: String = "MztiRepository"

class MztiRepository(
    private val context: Context
) : BaseNetworkRepository(context, TAG) {

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
                    context.getString(R.string.api_connection_fail_msg)
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
                put("nickname", pSignUpData.signUpNickname)
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

}