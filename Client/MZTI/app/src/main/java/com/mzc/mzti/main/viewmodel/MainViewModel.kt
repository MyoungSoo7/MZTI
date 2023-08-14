package com.mzc.mzti.main.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.model.data.download.DownloadResult
import com.mzc.mzti.model.data.router.MztiTabRouter
import com.mzc.mzti.model.repository.download.DownloadRepository
import kotlinx.coroutines.launch

private const val TAG: String = "MainViewModel"

class MainViewModel(
    private val downloadRepository: DownloadRepository
) : BaseViewModel() {

    /**
     * 현재 선택된 탭이 어떤 탭인지
     */
    private val _tabRouter: MutableLiveData<MztiTabRouter> =
        MutableLiveData(MztiTabRouter.TAB_FRIENDS)
    val tabRouter: LiveData<MztiTabRouter> get() = _tabRouter

    /**
     * 비트맵 저장 결과
     */
    private val _saveBmpResult: MutableLiveData<DownloadResult<Uri>?> = MutableLiveData(null)
    val saveBmpResult: LiveData<DownloadResult<Uri>?> get() = _saveBmpResult

    fun setTabRouter(pTabRouter: MztiTabRouter) {
        if (tabRouter.value != pTabRouter) {
            _tabRouter.value = pTabRouter
        }
    }

    // region UserProfile Tab
    fun requestSaveBitmap(pBitmap: Bitmap) {
        setProgressFlag(true)

        viewModelScope.launch {
            val result = downloadRepository.saveBitmap(pBitmap)
            _saveBmpResult.postValue(result)
        }
    }
    // endregion UserProfile Tab

}