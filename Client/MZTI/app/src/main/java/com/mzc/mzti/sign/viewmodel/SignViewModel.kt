package com.mzc.mzti.sign.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.network.NetworkResult
import com.mzc.mzti.model.data.router.SignRouter
import com.mzc.mzti.model.data.router.SignUpState
import com.mzc.mzti.model.data.sign.SignUpData
import com.mzc.mzti.model.data.user.UserInfoData
import com.mzc.mzti.model.repository.network.MztiRepository
import kotlinx.coroutines.launch

class SignViewModel(
    private val mztiRepository: MztiRepository
) : BaseViewModel() {

    private val _signRouter: MutableLiveData<SignRouter> = MutableLiveData(SignRouter.SIGN_IN)
    val signRouter: LiveData<SignRouter> get() = _signRouter

    private var _loginId: String = ""
    private val loginId: String get() = _loginId

    private var _loginPw: String = ""
    private val loginPw: String get() = _loginPw

    private val _enableLogin: MutableLiveData<Boolean> = MutableLiveData(false)
    val enableLogin: LiveData<Boolean> get() = _enableLogin

    private val _loginResult: MutableLiveData<UserInfoData> = MutableLiveData()
    val loginResult: LiveData<UserInfoData> get() = _loginResult

    private val _signUpState: MutableLiveData<SignUpState> = MutableLiveData(SignUpState.ID)
    val signUpState: LiveData<SignUpState> get() = _signUpState

    private val signUpData: SignUpData = SignUpData()

    private val _enableCheckId: MutableLiveData<Boolean> = MutableLiveData(false)
    val enableCheckId: LiveData<Boolean> get() = _enableCheckId

    private val _enableCheckPw: MutableLiveData<Boolean> = MutableLiveData(false)
    val enableCheckPw: LiveData<Boolean> get() = _enableCheckPw

    private val _enableCheckNickname: MutableLiveData<Boolean> = MutableLiveData(false)
    val enableCheckNickname: LiveData<Boolean> get() = _enableCheckNickname

    private val _enableCheckMbti: MutableLiveData<Boolean> = MutableLiveData(false)
    val enableCheckMbti: LiveData<Boolean> get() = _enableCheckMbti

    fun setSignRouter(pSignRouter: SignRouter) {
        if (signRouter.value != pSignRouter) {
            _signRouter.value = pSignRouter
        }
    }

    fun setLoginId(pLoginId: String) {
        _loginId = pLoginId
        _enableLogin.value = loginId.isNotEmpty() && loginPw.isNotEmpty()
    }

    fun setLoginPw(pLoginPw: String) {
        _loginPw = pLoginPw
        _enableLogin.value = loginId.isNotEmpty() && loginPw.isNotEmpty()
    }

    fun requestLogin() {
        viewModelScope.launch {
            when (val result = mztiRepository.makeLoginRequest(loginId, loginPw)) {
                is NetworkResult.Success<UserInfoData> -> {
                    val userInfoData = result.data
                    MztiSession.login(
                        pUserId = userInfoData.id,
                        pGenerateType = userInfoData.generateType,
                        pUserToken = userInfoData.token,
                        pUserNickname = userInfoData.nickname,
                        pUserMBTI = userInfoData.mbti,
                        pUserProfileImg = userInfoData.profileImg
                    )
                    _loginResult.value = userInfoData
                }

                is NetworkResult.Fail -> {
                    setApiFailMsg(result.msg)
                }

                is NetworkResult.Error -> {
                    setExceptionData(result.exception)
                }
            }
        }
    }

    fun clearSignUpData() {
        signUpData.clear()
    }

    fun setSignUpId(pSignUpId: String) {
        signUpData.setSignUpId(pSignUpId)
        _enableCheckId.value = signUpData.enableCheckId
    }

    fun setSignUpPw(pSignUpPw: String) {
        signUpData.setSignUpPw(pSignUpPw)
        _enableCheckPw.value = signUpData.enableCheckPw
    }

    fun setSignUpPwAgain(pSignUpAgain: String) {
        signUpData.setSignUpPwAgain(pSignUpAgain)
        _enableCheckPw.value = signUpData.enableCheckPw
    }

    fun setSignUpNickname(pSignUpNickname: String) {
        signUpData.setSignUpNickname(pSignUpNickname)
        _enableCheckNickname.value = signUpData.enableCheckNickname
    }

    fun setSignUpMbti(pSignUpMbti: MBTI) {
        signUpData.setSignUpMbti(pSignUpMbti)
        _enableCheckMbti.value = signUpData.enableCheckMbti
    }

    fun checkSignUpId(): Boolean {
        val ret = signUpData.enableCheckId
        _enableCheckId.value = ret
        if (ret) {
            requestCheckId()
        }
        return ret
    }

    fun checkSignUpPw(): Boolean {
        val ret = signUpData.enableCheckPw
        _enableCheckPw.value = ret
        if (ret) {
            moveToNextSignUpState()
        }
        return ret
    }

    fun checkSignUpNickname(): Boolean {
        val ret = signUpData.enableCheckNickname
        _enableCheckNickname.value = ret
        if (ret) {
            moveToNextSignUpState()
        }
        return ret
    }

    fun checkSignUpMbti(): Boolean {
        val ret = signUpData.enableCheckMbti
        _enableCheckMbti.value = ret
        if (ret) {
            requestSignUp()
        }
        return ret
    }

    fun moveToNextSignUpState() {
        _signUpState.value = when (signUpState.value) {
            SignUpState.ID -> SignUpState.PW
            SignUpState.PW -> SignUpState.NICKNAME
            SignUpState.NICKNAME -> SignUpState.MBTI
            else -> SignUpState.MBTI
        }
    }

    fun moveToPrevSignUpState() {
        _signUpState.value = when (signUpState.value) {
            SignUpState.PW -> SignUpState.ID
            SignUpState.NICKNAME -> SignUpState.PW
            SignUpState.MBTI -> SignUpState.NICKNAME
            else -> SignUpState.ID
        }
    }

    private fun requestCheckId() {
        setProgressFlag(true)
        viewModelScope.launch {
            when (val result = mztiRepository.makeCheckIdRequest(signUpData.signUpId)) {
                is NetworkResult.Success<Boolean> -> {
                    if (result.data) {
                        setProgressFlag(false)
                        moveToNextSignUpState()
                    } else {
                        setApiFailMsg("이미 사용중인 ID 입니다.")
                    }
                }

                is NetworkResult.Fail -> {
                    setApiFailMsg(result.msg)
                }

                is NetworkResult.Error -> {
                    setExceptionData(result.exception)
                }
            }
        }
    }

    private fun requestSignUp() {
        setProgressFlag(true)
        viewModelScope.launch {

        }
    }

}