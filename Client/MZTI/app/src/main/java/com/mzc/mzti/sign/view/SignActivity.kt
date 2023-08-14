package com.mzc.mzti.sign.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseActivity
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.databinding.ActivitySignBinding
import com.mzc.mzti.main.view.MainActivity
import com.mzc.mzti.model.data.router.SignRouter
import com.mzc.mzti.model.data.router.SignUpState
import com.mzc.mzti.sign.viewmodel.SignViewModel

private const val TAG: String = "SignActivity"

class SignActivity : BaseActivity() {

    private val model: SignViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(application)
        )[SignViewModel::class.java]
    }
    private val binding: ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }

    private val signInFragment: SignInFragment = SignInFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setObserver()
        init()
    }

    private fun setObserver() {
        model.progressFlag.observe(this, Observer { progressFlag ->
            if (progressFlag) {
                showProgress()
            } else {
                dismissProgress()
            }
        })

        model.apiFailMsg.observe(this, Observer { failMsg ->
            if (failMsg.isNotEmpty()) {
                model.setProgressFlag(false)
                showToastMsg(failMsg)
            }
        })

        model.exceptionData.observe(this, Observer { exception ->
            model.setProgressFlag(false)
            showErrorMsg()
        })

        model.signRouter.observe(this, Observer { signRouter ->
            when (signRouter) {
                SignRouter.SIGN_IN -> {
                    if (getTopFragment() !is SignInFragment) {
                        setFragment(R.id.fl_sign, signInFragment)
                    }
                }

                SignRouter.SIGN_UP -> {
                    model.clearSignUpData()
                    addFragment(R.id.fl_sign, SignUpFragment())
                }

                else -> {
                    DLog.e(TAG, "Unrecognized SignRouter, $signRouter")
                }
            }
        })

        model.loginResult.observe(this, Observer { userInfoData ->
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        })
    }

    private fun init() {
    }

    private fun getTopFragment(): Fragment? {
        return if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.findFragmentById(R.id.fl_sign)
        } else {
            null
        }
    }

    private fun popNaviStack() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            val currentFragment: Fragment? =
                supportFragmentManager.findFragmentById(R.id.fl_sign)

            if (currentFragment is SignUpFragment) {
                supportFragmentManager.beginTransaction()
                    .remove(currentFragment)
                    .commitAllowingStateLoss()
                supportFragmentManager.popBackStackImmediate()
            }
        }
    }

    override fun onBackPressed() {
        if (model.signRouter.value == SignRouter.SIGN_UP) {
            when (model.signUpState.value) {
                SignUpState.ID -> {
                    popNaviStack()
                    model.setSignRouter(SignRouter.SIGN_IN)
                }

                SignUpState.PW,
                SignUpState.NICKNAME,
                SignUpState.MBTI -> {
                    model.moveToPrevSignUpState()
                }

                else -> {
                }
            }
        } else {
            super.onBackPressed()
        }
    }

}