package com.mzc.mzti.profileedit.view

import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mzc.mzti.base.BaseActivity
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.databinding.ActivityUserProfileEditBinding
import com.mzc.mzti.model.data.user.UserInfoData
import com.mzc.mzti.profileedit.viewmodel.UserProfileEditViewModel

private const val TAG: String = "UserProfileEditActivity"
const val KEY_USER_INFO_DATA: String = "user_info_data"

class UserProfileEditActivity : BaseActivity() {

    private val model: UserProfileEditViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(application)
        )[UserProfileEditViewModel::class.java]
    }
    private val binding: ActivityUserProfileEditBinding by lazy {
        ActivityUserProfileEditBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setObserver()
        init()
    }

    private fun setObserver() {
        model.progressFlag.observe(this, Observer { flag ->
            if (flag) {
                showProgress()
            } else {
                dismissProgress()
            }
        })
    }

    private fun init() {

    }

}