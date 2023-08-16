package com.mzc.mzti.base

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mzc.mzti.friends.viewmodel.AddFriendViewModel
import com.mzc.mzti.friends.viewmodel.FriendMbtiViewModel
import com.mzc.mzti.intro.viewmodel.IntroViewModel
import com.mzc.mzti.main.viewmodel.MainViewModel
import com.mzc.mzti.model.repository.download.DownloadRepository
import com.mzc.mzti.model.repository.image.ImageRepository
import com.mzc.mzti.model.repository.network.MztiRepository
import com.mzc.mzti.profileedit.viewmodel.UserProfileEditViewModel
import com.mzc.mzti.sign.viewmodel.SignViewModel
import com.mzc.mzti.test.viewmodel.TestViewModel


open class BaseViewModel : ViewModel() {

    /**
     * ProgressBar on/off flag
     */
    private val _progressFlag: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressFlag: LiveData<Boolean> get() = _progressFlag

    /**
     * Exception Data
     *
     * Exception이 발생한 경우, 일시적인 오류임을 알리는 Toast 메시지를 출력함
     */
    private val _exceptionData: MutableLiveData<Exception> = MutableLiveData()
    val exceptionData: LiveData<Exception> get() = _exceptionData

    /**
     * API Fail Message
     *
     * API Request 실패 시, Fail Message를 Toast 메시지로 출력해야 함
     */
    private val _apiFailMsg: MutableLiveData<String> = MutableLiveData()
    val apiFailMsg: LiveData<String> get() = _apiFailMsg

    /**
     * Toast Message
     */
    private val _toastMsg: MutableLiveData<String> = MutableLiveData()
    val toastMsg: LiveData<String> get() = _toastMsg

    fun setProgressFlag(pProgressFlag: Boolean) {
        if (progressFlag.value != pProgressFlag)
            _progressFlag.value = pProgressFlag
    }

    fun setExceptionData(pException: Exception) {
        _exceptionData.value = pException
    }

    fun setApiFailMsg(pMsg: String) {
        _apiFailMsg.value = pMsg
    }

    fun setToastMsg(pMsg: String) {
        _toastMsg.value = pMsg
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            // Intro ViewModel
            if (modelClass.isAssignableFrom(IntroViewModel::class.java)) {
                return IntroViewModel(
                    MztiRepository(application)
                ) as T
            }
            // Main ViewModel
            else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(
                    DownloadRepository(application),
                    MztiRepository(application)
                ) as T
            }
            // UserProfileEdit ViewModel
            else if (modelClass.isAssignableFrom(UserProfileEditViewModel::class.java)) {
                return UserProfileEditViewModel(
                    MztiRepository(application),
                    ImageRepository(application)
                ) as T
            }
            // Sign ViewModel
            else if (modelClass.isAssignableFrom(SignViewModel::class.java)) {
                return SignViewModel(
                    MztiRepository(application)
                ) as T
            }
            // AddFriend ViewModel
            else if (modelClass.isAssignableFrom(AddFriendViewModel::class.java)) {
                return AddFriendViewModel(
                    MztiRepository(application)
                ) as T
            }
            // Test ViewModel
            else if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
                return TestViewModel(
                    MztiRepository(application)
                ) as T
            }
            // FriendMbti ViewModel
            else if (modelClass.isAssignableFrom(FriendMbtiViewModel::class.java)) {
                return FriendMbtiViewModel(
                    MztiRepository(application)
                ) as T
            }
            // 식별되지 않은 ViewModel
            else {
                throw IllegalArgumentException("Unknown ViewModel Class!")
            }
        }
    }


    companion object {
        private const val TAG: String = "BaseViewModel"
    }
}