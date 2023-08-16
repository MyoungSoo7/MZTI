package com.mzc.mzti.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.network.NetworkResult
import com.mzc.mzti.model.data.question.MbtiAnswerData
import com.mzc.mzti.model.data.question.MbtiQuestionData
import com.mzc.mzti.model.data.question.MbtiQuestionDataWrapper
import com.mzc.mzti.model.data.question.MbtiTestResultData
import com.mzc.mzti.model.repository.network.MztiRepository
import kotlinx.coroutines.launch

private const val TAG: String = "TestViewModel"

class TestViewModel(
    private val mztiRepository: MztiRepository
) : BaseViewModel() {

    private val _questionDataWrapper: MutableLiveData<MbtiQuestionDataWrapper> = MutableLiveData()
    val questionDataWrapper: LiveData<MbtiQuestionDataWrapper> get() = _questionDataWrapper

    private var _mbti: MBTI = MBTI.ISTJ
    private val mbti: MBTI get() = _mbti

    private val _mbtiTestResult: MutableLiveData<MbtiTestResultData> = MutableLiveData()
    val mbtiTestResultData: LiveData<MbtiTestResultData> get() = _mbtiTestResult

    fun init(pMbti: MBTI) {
        _mbti = pMbti
    }

    fun requestQuestion() {
        setProgressFlag(true)
        viewModelScope.launch {
            val result = mztiRepository.makeQuestionRequest(
                pQuestionCount = 16,
                pMbti = mbti,
                pUserToken = MztiSession.userToken,
                pGenerateType = MztiSession.generateType
            )

            when (result) {
                is NetworkResult.Success<MbtiQuestionDataWrapper> -> {
                    val data = result.data
                    DLog.d(TAG, "questionResponse=$data")
                    _questionDataWrapper.value = data
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

    fun requestQuestionResult(pMBtiAnswerList: List<MbtiAnswerData>) {
        setProgressFlag(true)
        viewModelScope.launch {
            val result = mztiRepository.makeQuestionResult(
                pMbti = mbti,
                pMBtiAnswerList = pMBtiAnswerList,
                pUserToken = MztiSession.userToken,
                pGenerateType = MztiSession.generateType
            )

            when (result) {
                is NetworkResult.Success<MbtiTestResultData> -> {
                    val data = result.data
                    DLog.d(TAG, "testResult=$data")
                    _mbtiTestResult.value = data
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

}