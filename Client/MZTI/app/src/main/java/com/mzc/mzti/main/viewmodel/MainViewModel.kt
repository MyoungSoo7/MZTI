package com.mzc.mzti.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.model.data.router.MztiTabRouter

private const val TAG: String = "MainViewModel"

class MainViewModel : BaseViewModel() {

    /**
     * 현재 선택된 탭이 어떤 탭인지
     */
    private val _tabRouter: MutableLiveData<MztiTabRouter> =
        MutableLiveData(MztiTabRouter.TAB_FRIENDS)
    val tabRouter: LiveData<MztiTabRouter> get() = _tabRouter

    fun setTabRouter(pTabRouter: MztiTabRouter) {
        if (tabRouter.value != pTabRouter) {
            _tabRouter.value = pTabRouter
        }
    }

}