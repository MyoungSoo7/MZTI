package com.mzc.mzti.test.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mzc.mzti.R
import com.mzc.mzti.applyTextSize
import com.mzc.mzti.base.BaseActivity
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.custom.MztiTestLayout
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.databinding.ActivityTestBinding
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.question.MbtiAnswerData
import com.mzc.mzti.model.data.question.MbtiTestResultData
import com.mzc.mzti.test.viewmodel.TestViewModel

private const val TAG: String = "TestActivity"
const val KEY_MBTI: String = "mbti"

class TestActivity : BaseActivity() {

    private val model: TestViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(application)
        )[TestViewModel::class.java]
    }
    private val binding: ActivityTestBinding by lazy {
        ActivityTestBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mbti: MBTI
        // 백그라운드 종료되었다가 다시 실행된 경우
        if (savedInstanceState != null) {
            savedInstanceState.apply {
                mbti = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSerializable(KEY_MBTI, MBTI::class.java)!!
                } else {
                    getSerializable(KEY_MBTI) as MBTI
                }
            }
        }
        // 그 외의 일반적인 경우
        else {
            intent.apply {
                mbti = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    getSerializableExtra(KEY_MBTI, MBTI::class.java)!!
                } else {
                    getSerializableExtra(KEY_MBTI) as MBTI
                }
            }
        }

        model.init(mbti)
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

        model.exceptionData.observe(this, Observer { exception ->
            exception.message?.let { msg ->
                model.setProgressFlag(false)
                showErrorMsg()
            }
        })

        model.apiFailMsg.observe(this, Observer { msg ->
            model.setProgressFlag(false)
            showToastMsg(msg)
        })

        model.toastMsg.observe(this, Observer { msg ->
            showToastMsg(msg)
        })

        model.questionDataWrapper.observe(this, Observer { questionDataWrapper ->
            model.setProgressFlag(false)
            binding.cvTestMztiTestLayout.setMbtiQuestion(questionDataWrapper)
        })

        model.mbtiTestResultData.observe(this, Observer { testResult ->
            model.setProgressFlag(false)
            setResultLayout(testResult)
        })
    }

    private fun init() {
        binding.apply {
            cvTestMztiTestLayout.mztiTestLayoutListener =
                object : MztiTestLayout.OnMztiTestLayoutListener {
                    override fun onTestFinish(pAnswerList: List<MbtiAnswerData>) {
                        DLog.d(TAG, "Test Finished!")
                        model.requestQuestionResult(pAnswerList)
                    }
                }

            ivTestClose.setOnClickListener {
                setResult(if (model.mbtiTestResultData.value != null) RESULT_OK else RESULT_CANCELED)
                finish()
            }

            tvTestResultConfirm.setOnClickListener {
                setResult(if (model.mbtiTestResultData.value != null) RESULT_OK else RESULT_CANCELED)
                finish()
            }
        }

        model.requestQuestion()
    }

    private fun setResultLayout(pTestResultData: MbtiTestResultData) {
        val badgeData = pTestResultData.mbtiBadgeData
        val totalScore = pTestResultData.totalScore

        binding.apply {
            cvTestMztiBadge.updateBadge(
                badgeData.mbti,
                badgeData.mbtiSize0,
                badgeData.mbtiSize1,
                badgeData.mbtiSize2,
                badgeData.mbtiSize3
            )

            tvTestTotalScore.text = getString(R.string.test_totalScore, totalScore)
                .applyTextSize(48, 0, totalScore.toString().length - 1, this@TestActivity)

            val width = cvTestMztiTestLayout.measuredWidth
            val valueAnimator = ValueAnimator
                .ofFloat(0f, width.toFloat())
                .setDuration(300)
            valueAnimator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                cvTestMztiTestLayout.translationX = -animatedValue
                clTestResultLayout.translationX = width - animatedValue
            }
            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    clTestResultLayout.visibility = View.VISIBLE
                    clTestResultLayout.translationX = width.toFloat()
                }

                override fun onAnimationEnd(animation: Animator) {
                    cvTestMztiTestLayout.visibility = View.GONE
                }
            })

            AnimatorSet().let { animatorSet ->
                animatorSet.interpolator = AccelerateDecelerateInterpolator()
                animatorSet.play(valueAnimator)
                animatorSet.start()
            }
        }
    }

}