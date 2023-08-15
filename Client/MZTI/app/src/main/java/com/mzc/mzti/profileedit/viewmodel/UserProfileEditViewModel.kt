package com.mzc.mzti.profileedit.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.repository.image.ImageRepository
import com.mzc.mzti.model.repository.network.MztiRepository
import kotlinx.coroutines.launch

private const val TAG: String = "UserProfileEditViewModel"

class UserProfileEditViewModel(
    private val mztiRepository: MztiRepository,
    private val imageRepository: ImageRepository
) : BaseViewModel() {

    private val _userNickname: MutableLiveData<String> = MutableLiveData("")
    val userNickname: LiveData<String> get() = _userNickname

    private var _userMBTI: MutableLiveData<MBTI> = MutableLiveData(MBTI.MZTI)
    val userMBTI: LiveData<MBTI> get() = _userMBTI

    private var _userProfileImg: MutableLiveData<Uri> = MutableLiveData()
    val userProfileImg: LiveData<Uri> get() = _userProfileImg

    fun setUserNickname(pUserNickname: String) {
        _userNickname.value = pUserNickname
    }

    fun setUserMBTI(pUserMBTI: MBTI) {
        _userMBTI.value = pUserMBTI
    }

    fun setUserProfileImg(pUserProfileImg: Uri) {

        _userProfileImg.value = pUserProfileImg
    }

    fun checkProfileEdited(): Boolean {
        val originName = MztiSession.userNickname
        val originMBTI = MztiSession.userMbti

        return if (userNickname.value == originName && userMBTI.value == originMBTI && userProfileImg.value == null) {
            false
        } else {
            requestEditProfile()
            true
        }
    }

    private fun requestEditProfile() {
        setProgressFlag(true)
        viewModelScope.launch {

        }
    }

}