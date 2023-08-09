package com.mzc.mzti.intro.viewmodel

import android.app.Application
import com.mzc.mzti.base.BaseViewModel

class IntroViewModel(
    private val application: Application
) : BaseViewModel() {


    companion object {
        private const val TAG: String = "IntroViewModel"
    }

}