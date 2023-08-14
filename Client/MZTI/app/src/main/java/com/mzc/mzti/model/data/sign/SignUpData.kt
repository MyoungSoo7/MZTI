package com.mzc.mzti.model.data.sign

import com.mzc.mzti.model.data.mbti.MBTI

class SignUpData {

    private var _signUpId: String = ""
    val signUpId: String get() = _signUpId

    private var _signUpPw: String = ""
    val signUpPw: String get() = _signUpPw

    private var _signUpPwAgain: String = ""
    val signUpPwAgain: String get() = _signUpPwAgain

    private var _signUpNickname: String = ""
    val signUpNickname: String get() = _signUpNickname

    private var _signUpMbti: MBTI = MBTI.MZTI
    val signUpMbti: MBTI get() = _signUpMbti

    val enableCheckId: Boolean get() = signUpId.isNotEmpty()

    val enableCheckPw: Boolean get() = signUpPw.length >= 6 && signUpPw.isNotBlank() && signUpPw == signUpPwAgain

    val enableCheckNickname: Boolean get() = signUpNickname.length in 1 until 9

    val enableCheckMbti: Boolean get() = signUpMbti != MBTI.MZTI

    fun clear() {
        _signUpId = ""
        _signUpPw = ""
        _signUpPwAgain = ""
        _signUpNickname = ""
        _signUpMbti = MBTI.MZTI
    }

    fun setSignUpId(pSignUpId: String) {
        _signUpId = pSignUpId
    }

    fun setSignUpPw(pSignUpPw: String) {
        _signUpPw = pSignUpPw
    }

    fun setSignUpPwAgain(pSignUpPwAgain: String) {
        _signUpPwAgain = pSignUpPwAgain
    }

    fun setSignUpNickname(pSignUpNickname: String) {
        _signUpNickname = pSignUpNickname
    }

    fun setSignUpMbti(pSignUpMBTI: MBTI) {
        _signUpMbti = pSignUpMBTI
    }

}