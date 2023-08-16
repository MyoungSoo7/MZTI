package com.mzc.mzti.profileedit.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.network.NetworkResult
import com.mzc.mzti.model.data.user.UserInfoData
import com.mzc.mzti.model.data.user.UserProfileData
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

    private val _userProfileImg: MutableLiveData<String?> = MutableLiveData()
    val userProfileImg: LiveData<String?> get() = _userProfileImg

    private val _editProfileResult: MutableLiveData<UserInfoData> = MutableLiveData()
    val editProfileResult: LiveData<UserInfoData> get() = _editProfileResult

    fun init(
        pUserNickname: String,
        pUserMBTI: MBTI
    ) {
        _userNickname.value = pUserNickname
        _userMBTI.value = pUserMBTI
    }

    fun setUserNickname(pUserNickname: String) {
        _userNickname.value = pUserNickname
    }

    fun setUserMBTI(pUserMBTI: MBTI) {
        _userMBTI.value = pUserMBTI
    }

    fun setUserProfileImg(pUserProfileImg: Uri) {
        setProgressFlag(true)
        viewModelScope.launch {
            val imgPath = imageRepository.copyImageToCacheDir(pUserProfileImg)
            _userProfileImg.value = imgPath
        }
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
            val result = mztiRepository.makeEditProfileRequest(
                pUserToken = MztiSession.userToken,
                pGenerateType = MztiSession.generateType,
                pUserNickname = userNickname.value,
                pUserMBTI = userMBTI.value,
                pUserProfileImgPath = userProfileImg.value
            )

            when (result) {
                is NetworkResult.Success<UserInfoData> -> {
                    val data = result.data
                    _editProfileResult.value = data
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