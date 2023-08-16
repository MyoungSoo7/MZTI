package com.mzc.mzti.main.viewmodel

import android.graphics.Bitmap
import android.net.NetworkRequest
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.model.data.download.DownloadResult
import com.mzc.mzti.model.data.friends.FriendsDataWrapper
import com.mzc.mzti.model.data.friends.FriendsLayoutType
import com.mzc.mzti.model.data.friends.FriendsOtherProfileData
import com.mzc.mzti.model.data.network.NetworkResult
import com.mzc.mzti.model.data.router.MztiTabRouter
import com.mzc.mzti.model.data.user.UserProfileData
import com.mzc.mzti.model.repository.download.DownloadRepository
import com.mzc.mzti.model.repository.network.MztiRepository
import kotlinx.coroutines.launch

private const val TAG: String = "MainViewModel"

class MainViewModel(
    private val downloadRepository: DownloadRepository,
    private val mztiRepository: MztiRepository
) : BaseViewModel() {

    /**
     * 현재 선택된 탭이 어떤 탭인지
     */
    private val _tabRouter: MutableLiveData<MztiTabRouter> =
        MutableLiveData(MztiTabRouter.TAB_FRIENDS)
    val tabRouter: LiveData<MztiTabRouter> get() = _tabRouter

    /**
     * true -> 사용자가 로그아웃 버튼 클릭시, true로 변경됨
     */
    private val _logoutFlag: MutableLiveData<Boolean> = MutableLiveData(false)
    val logoutFlag: LiveData<Boolean> get() = _logoutFlag

    /**
     * 비트맵 저장 결과
     */
    private val _saveBmpResult: MutableLiveData<DownloadResult<Uri>?> = MutableLiveData(null)
    val saveBmpResult: LiveData<DownloadResult<Uri>?> get() = _saveBmpResult

    private val _friendsList: MutableLiveData<List<FriendsDataWrapper>> = MutableLiveData()
    val friendsList: LiveData<List<FriendsDataWrapper>> get() = _friendsList

    /**
     * 삭제할 친구 ID
     */
    private var _removeFriendId: String = ""
    private val removeFriendId: String get() = _removeFriendId

    private val _learningAgree: MutableLiveData<Boolean> = MutableLiveData(false)
    val learningAgree: LiveData<Boolean> get() = _learningAgree

    private val _userProfileData: MutableLiveData<UserProfileData?> = MutableLiveData(null)
    val userProfileData: LiveData<UserProfileData?> get() = _userProfileData

    fun setTabRouter(pTabRouter: MztiTabRouter) {
        if (tabRouter.value != pTabRouter) {
            _tabRouter.value = pTabRouter
        }
    }

    fun setLogoutFlag(pFlag: Boolean) {
        if (logoutFlag.value != pFlag) {
            _logoutFlag.value = pFlag
        }
    }

    // region Friends Tab
    fun requestFriendsList() {
        if (friendsList.value?.isEmpty() != false) {
            setProgressFlag(true)
            viewModelScope.launch {
                val result = mztiRepository.makeFriendListRequest(
                    MztiSession.userToken,
                    MztiSession.generateType
                )

                when (result) {
                    is NetworkResult.Success<List<FriendsDataWrapper>> -> {
                        val data = result.data
                        _friendsList.value = data
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

    fun requestAddFriend(pOtherProfileData: FriendsOtherProfileData) {
        setProgressFlag(true)
        viewModelScope.launch {
            val prevList = friendsList.value ?: arrayListOf()
            val newList = arrayListOf<FriendsDataWrapper>()

            val otherProfileList = arrayListOf(pOtherProfileData)
            for (data in prevList) {
                if (data.data is FriendsOtherProfileData) {
                    otherProfileList.add(data.data)
                } else if (data.type == FriendsLayoutType.FRIEND_COUNT) {
                    newList.add(
                        FriendsDataWrapper(
                            FriendsLayoutType.FRIEND_COUNT,
                            (data.data as Int) + 1
                        )
                    )
                } else {
                    newList.add(data)
                }
            }
            otherProfileList.sort()

            for (otherProfile in otherProfileList) {
                newList.add(
                    FriendsDataWrapper(
                        FriendsLayoutType.OTHER_PROFILE,
                        otherProfile
                    )
                )
            }

            _friendsList.value = newList
        }
    }

    fun setRemoveFriendId(pFriendId: String) {
        _removeFriendId = pFriendId
    }

    fun requestRemoveFriend() {
        val id = removeFriendId
        if (id.isNotEmpty()) {
            setProgressFlag(true)
            viewModelScope.launch {
                val result = mztiRepository.makeRemoveFriendRequest(
                    id,
                    MztiSession.userToken,
                    MztiSession.generateType
                )

                when (result) {
                    is NetworkResult.Success<String> -> {
                        val prevList = friendsList.value ?: arrayListOf()
                        val newList = arrayListOf<FriendsDataWrapper>()

                        val otherProfileList = arrayListOf<FriendsOtherProfileData>()
                        for (data in prevList) {
                            if (data.data is FriendsOtherProfileData) {
                                otherProfileList.add(data.data)
                            } else if (data.type == FriendsLayoutType.FRIEND_COUNT) {
                                newList.add(
                                    FriendsDataWrapper(
                                        FriendsLayoutType.FRIEND_COUNT,
                                        (data.data as Int) + 1
                                    )
                                )
                            } else {
                                newList.add(data)
                            }
                        }

                        for (otherProfile in otherProfileList) {
                            if (otherProfile.id != id) {
                                newList.add(
                                    FriendsDataWrapper(
                                        FriendsLayoutType.OTHER_PROFILE,
                                        otherProfile
                                    )
                                )
                            }
                        }

                        _friendsList.value = newList
                        setToastMsg(result.data)
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
    // endregion Friends Tab

    // region Learning Tab
    fun setLearningAgree(pLearningAgree: Boolean) {
        _learningAgree.value = pLearningAgree
    }

    fun setLearningAgreeClicked() {
        _learningAgree.value = when (learningAgree.value) {
            true -> false
            false -> true
            else -> false
        }
    }
    // endregion Learning Tab

    // region UserProfile Tab
    fun requestSaveBitmap(pBitmap: Bitmap) {
        setProgressFlag(true)

        viewModelScope.launch {
            val result = downloadRepository.saveBitmap(pBitmap)
            _saveBmpResult.postValue(result)
        }
    }

    fun requestUserProfile() {
        if (userProfileData.value == null) {
            setProgressFlag(true)

            viewModelScope.launch {
                val result = mztiRepository.makeUserProfileRequest(
                    MztiSession.userToken,
                    MztiSession.generateType
                )

                when (result) {
                    is NetworkResult.Success<UserProfileData> -> {
                        val data = result.data
                        _userProfileData.value = data
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
    // endregion UserProfile Tab

}