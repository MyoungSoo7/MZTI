package com.mzc.mzti.main.view

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseActivity
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.databinding.ActivityMainBinding
import com.mzc.mzti.intro.viewmodel.IntroViewModel
import com.mzc.mzti.main.viewmodel.MainViewModel
import com.mzc.mzti.model.data.router.MztiTabRouter

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

                }

                MztiTabRouter.TAB_COMPARE -> {

                }

                MztiTabRouter.TAB_LEARNING -> {

                }

                MztiTabRouter.TAB_PROFILE -> {

                }

                else -> {
                    DLog.e(TAG, "Unknown TabRouter!, $tabRouter")
                }
            }
        })
    }

    private fun init() {

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