package com.mzc.mzti.intro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.model.data.network.NetworkResult
import com.mzc.mzti.model.data.user.UserInfoData
import com.mzc.mzti.model.repository.network.MztiRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroViewModel(
    private val mztiRepository: MztiRepository
) : BaseViewModel() {

    /**
     * 저장공간 권한이 허용되었는지 여부
     */
    private var _isStoragePermissionGranted: Boolean = false
    val isStoragePermissionGranted: Boolean get() = _isStoragePermissionGranted

    /**
     * 카메라 권한이 허용되었는지 여부
     */
    private var _isCameraPermissionGranted: Boolean = false
    val isCameraPermissionGranted: Boolean get() = _isCameraPermissionGranted

    /**
     * 필요한 모든 권한이 허용되었는지 여부
     */
    private val _isAllRequiredPermissionGranted: MutableLiveData<Boolean> = MutableLiveData(false)
    val isAllRequiredPermissionGranted: LiveData<Boolean> get() = _isAllRequiredPermissionGranted

    private val _userInfoData: MutableLiveData<NetworkResult<UserInfoData>> = MutableLiveData()
    val userInfoData: LiveData<NetworkResult<UserInfoData>> get() = _userInfoData

    /**
     * 저장공간 권한 허용 여부를 변경하는 함수
     */
    fun setStoragePermissionState(pIsGranted: Boolean) {
        _isStoragePermissionGranted = pIsGranted
        checkAllRequiredPermissionGranted()
    }

    /**
     * 카메라 권한 허용 여부를 변경하는 함수
     */
    fun setCameraPermissionState(pIsGranted: Boolean) {
        _isCameraPermissionGranted = pIsGranted
        checkAllRequiredPermissionGranted()
    }

    /**
     * 필요한 모든 권한이 허용되었는지 확인하는 함수
     */
    private fun checkAllRequiredPermissionGranted() {
        _isAllRequiredPermissionGranted.value =
            isStoragePermissionGranted && isCameraPermissionGranted
        DLog.d(TAG, "granted=${isAllRequiredPermissionGranted.value}")
    }

    fun requestUserInfo(pUserToken: String, pGenerateType: String) {
        viewModelScope.launch {
            _userInfoData.value = NetworkResult.Fail("Dummy Fail")
        }
    }

    companion object {
        private const val TAG: String = "IntroViewModel"
    }

}