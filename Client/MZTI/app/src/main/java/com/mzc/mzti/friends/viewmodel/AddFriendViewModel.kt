package com.mzc.mzti.friends.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.model.data.friends.FriendsOtherProfileData
import com.mzc.mzti.model.data.network.NetworkResult
import com.mzc.mzti.model.repository.network.MztiRepository
import kotlinx.coroutines.launch

class AddFriendViewModel(
    private val mztiRepository: MztiRepository
) : BaseViewModel() {

    private val _friendId: MutableLiveData<String> = MutableLiveData("")
    val friendId: LiveData<String> get() = _friendId

    private val _addFriendResult: MutableLiveData<FriendsOtherProfileData> = MutableLiveData()
    val addFriendResult: LiveData<FriendsOtherProfileData> get() = _addFriendResult

    fun setFriendId(pFriendId: String) {
        _friendId.value = pFriendId
    }

    fun requestAddFriend() {
        val id = friendId.value
        if (id?.isNotEmpty() == true) {
            setProgressFlag(true)
            viewModelScope.launch {
                val result = mztiRepository.makeAddFriendRequest(
                    id,
                    MztiSession.userToken,
                    MztiSession.generateType
                )
                when (result) {
                    is NetworkResult.Success<FriendsOtherProfileData> -> {
                        val data = result.data
                        _addFriendResult.value = data
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

}