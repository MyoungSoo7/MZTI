package com.mzc.mzti.friends.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.model.data.compare.CompareMbtiData
import com.mzc.mzti.model.data.friends.FriendsOtherProfileData
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.network.NetworkResult
import com.mzc.mzti.model.data.user.UserInfoData
import com.mzc.mzti.model.repository.network.MztiRepository
import kotlinx.coroutines.launch

private const val TAG: String = "FriendMbtiViewModel"

class FriendMbtiViewModel(
    private val mztiRepository: MztiRepository
) : BaseViewModel() {

    private var _mbti: MBTI = MBTI.MZTI
    val mbti: MBTI get() = _mbti

    private var _friendInfo: FriendsOtherProfileData =
        FriendsOtherProfileData("", "", MBTI.MZTI, "")
    val friendInfo: FriendsOtherProfileData get() = _friendInfo

    private val _mbtiInfoResult: MutableLiveData<List<CompareMbtiData>> = MutableLiveData()
    val mbtiInfoResult: LiveData<List<CompareMbtiData>> get() = _mbtiInfoResult

    fun init(pMBTI: MBTI, pFriendInfo: FriendsOtherProfileData) {
        _mbti = pMBTI
        _friendInfo = pFriendInfo
    }

    fun requestMbtiInfo() {
        setProgressFlag(true)
        viewModelScope.launch {
            val result = mztiRepository.makeMbtiInfoRequest(
                mbti,
                MztiSession.userToken,
                MztiSession.generateType
            )

            when (result) {
                is NetworkResult.Success<List<CompareMbtiData>> -> {
                    val data = result.data
                    DLog.d(TAG, "data=$data")
                    _mbtiInfoResult.value = data
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