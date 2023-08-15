package com.mzc.mzti.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseActivity
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.databinding.ActivityMainBinding
import com.mzc.mzti.friends.view.FriendsFragment
import com.mzc.mzti.intro.viewmodel.IntroViewModel
import com.mzc.mzti.main.viewmodel.MainViewModel
import com.mzc.mzti.model.data.download.DownloadResult
import com.mzc.mzti.model.data.router.MztiTabRouter
import com.mzc.mzti.profile.view.UserProfileFragment
import com.mzc.mzti.sign.view.SignActivity

private const val TAG: String = "MainActivity"

class MainActivity : BaseActivity() {

    private val model: MainViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(application)
        )[MainViewModel::class.java]
    }
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val friendsFragment: FriendsFragment = FriendsFragment()
    private val userProfileFragment: UserProfileFragment = UserProfileFragment()

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

        model.tabRouter.observe(this, Observer { tabRouter ->
            when (tabRouter) {
                MztiTabRouter.TAB_FRIENDS -> {
                    setFragment(R.id.fl_main, friendsFragment)
                }

                MztiTabRouter.TAB_COMPARE -> {

                }

                MztiTabRouter.TAB_LEARNING -> {

                }

                MztiTabRouter.TAB_PROFILE -> {
                    setFragment(R.id.fl_main, userProfileFragment)
                }

                else -> {
                    DLog.e(TAG, "Unknown TabRouter!, $tabRouter")
                }
            }
        })

        model.logoutFlag.observe(this, Observer { flag ->
            if (flag) {
                MztiSession.logout()

                val signIntent = Intent(this, SignActivity::class.java)
                startActivity(signIntent)
                finish()

                model.setLogoutFlag(false)
            }
        })

        model.saveBmpResult.observe(this, Observer { result ->
            when (result) {
                is DownloadResult.Success<Uri?> -> {
                    model.setProgressFlag(false)
                    showToastMsg(getString(R.string.toast_msg_success_save_mbti_card))
                }

                is DownloadResult.Fail -> {
                    model.setProgressFlag(false)
                    DLog.e(TAG, result.msg)
                }

                is DownloadResult.Error -> {
                    model.setProgressFlag(false)
                    DLog.e(TAG, result.exception.stackTraceToString())
                }

                else -> {
                }
            }
        })
    }

    private fun init() {
        binding.apply {
            navMain.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_friends -> {
                        model.setTabRouter(MztiTabRouter.TAB_FRIENDS)
                        true
                    }

                    R.id.menu_compare -> {
                        model.setTabRouter(MztiTabRouter.TAB_COMPARE)
                        true
                    }

                    R.id.menu_learning -> {
                        model.setTabRouter(MztiTabRouter.TAB_LEARNING)
                        true
                    }

                    R.id.menu_profile -> {
                        model.setTabRouter(MztiTabRouter.TAB_PROFILE)
                        true
                    }

                    else -> {
                        DLog.e(TAG, "Unrecognized menuItem, ${menuItem.itemId}")
                        false
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        val tempTime: Long = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
            super.onBackPressed()
        } else {
            backPressedTime = tempTime
            val msg: String = "테스트를 종료하시려면 뒤로 버튼을 한 번 더 누루세요."
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val FINISH_INTERVAL_TIME: Long = 2000
        private var backPressedTime: Long = 0
    }

}