package com.mzc.mzti.intro.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mzc.mzti.base.BaseViewModel

class IntroViewModel(
    private val application: Application
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
    }

    companion object {
        private const val TAG: String = "IntroViewModel"
    }

}