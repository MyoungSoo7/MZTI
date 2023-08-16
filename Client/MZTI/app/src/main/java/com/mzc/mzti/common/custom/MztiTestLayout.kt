package com.mzc.mzti.common.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import com.mzc.mzti.R
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.databinding.InflateTestLayoutBinding
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.question.MbtiAnswerData
import com.mzc.mzti.model.data.question.MbtiQuestionData
import com.mzc.mzti.model.data.question.MbtiQuestionDataWrapper

private const val TAG: String = "MztiTestLayout"

class MztiTestLayout : RelativeLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    var mztiTestLayoutListener: OnMztiTestLayoutListener? = null

    private var _curQuestionNum: Int = 0
    private val curQuestionNum: Int get() = _curQuestionNum

    private var _mbti: MBTI = MBTI.MZTI
    private val mbti: MBTI get() = _mbti

    private val mbtiQuestionList: ArrayList<MbtiQuestionData> = arrayListOf()
    private val testViewList: ArrayList<View> = arrayListOf()
    private val mbtiAnswerList: ArrayList<MbtiAnswerData> = arrayListOf()

    private fun createMbtiTestView(pMbtiQuestionData: MbtiQuestionData): View {
        val view =
            LayoutInflater.from(context).inflate(R.layout.inflate_test_layout, this, false).apply {
                LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                ).apply {
                    id = generateViewId()
                }
            }

        InflateTestLayoutBinding.bind(view).apply {
            tvTestLayoutTitle.text = context.getString(R.string.testLayout_title, mbti.name)
            tvTestLayoutContent.text = pMbtiQuestionData.questionContent
            tvTestLayoutAnswer01.text = pMbtiQuestionData.answerList[0]
            tvTestLayoutAnswer02.text = pMbtiQuestionData.answerList[1]
            tvTestLayoutAnswer03.text = pMbtiQuestionData.answerList[2]

            tvTestLayoutAnswer01.setOnClickListener(onClickListener)
            tvTestLayoutAnswer02.setOnClickListener(onClickListener)
            tvTestLayoutAnswer03.setOnClickListener(onClickListener)
        }

        return view
    }

    fun setMbtiQuestion(pMbtiQuestionDataWrapper: MbtiQuestionDataWrapper) {
        _mbti = pMbtiQuestionDataWrapper.mbti

        mbtiQuestionList.clear()
        mbtiQuestionList.addAll(pMbtiQuestionDataWrapper.mbtiQuestionList)

        removeAllViews()
        for (idx in mbtiQuestionList.indices) {
            val questionData = mbtiQuestionList[idx]
            val view = createMbtiTestView(questionData)
            view.visibility = if (idx == curQuestionNum) View.VISIBLE else View.GONE
            addView(view)

            testViewList.add(view)
        }
    }

    private fun animateNextQuestion() {
        DLog.d(TAG, "animateNextQuestion!")
        val width = measuredWidth

        val curIdx = curQuestionNum
        val curView = getChildAt(curIdx)

        val nextIdx = curIdx + 1
        val nextView = getChildAt(nextIdx)

        val valueAnimator = ValueAnimator
            .ofFloat(0f, width.toFloat())
            .setDuration(300)
        valueAnimator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            curView.translationX = -animatedValue
            nextView.translationX = width - animatedValue
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                nextView.visibility = View.VISIBLE
                nextView.translationX = width.toFloat()
            }

            override fun onAnimationEnd(animation: Animator) {
                curView.visibility = View.GONE
            }
        })

        AnimatorSet().let { animatorSet ->
            animatorSet.interpolator = AccelerateDecelerateInterpolator()
            animatorSet.play(valueAnimator)
            animatorSet.start()
        }

        _curQuestionNum += 1
    }

    private fun onAnswerClicked(pAnswerIdx: Int) {
        val idx = curQuestionNum
        if (idx in mbtiQuestionList.indices) {
            val mbtiQuestionData = mbtiQuestionList[idx]

            if (mbtiQuestionData.answerIdx == pAnswerIdx) {
                mbtiAnswerList.add(
                    MbtiAnswerData(
                        questionType = mbtiQuestionData.questionType,
                        answerFlag = true
                    )
                )
            } else {
                mbtiAnswerList.add(
                    MbtiAnswerData(
                        questionType = mbtiQuestionData.questionType,
                        answerFlag = false
                    )
                )
            }

            // 다음 문제로 이동
            if (idx < mbtiQuestionList.size - 1) {
                animateNextQuestion()
            }
            // 모든 문제를 다 푼 경우
            else {
                mztiTestLayoutListener?.onTestFinish(mbtiAnswerList)
            }
        } else {
            throw IndexOutOfBoundsException("questionSize=${mbtiQuestionList.size}, idx=$idx")
        }
    }

    private val onClickListener = OnClickListener { view ->
        when (view.id) {
            R.id.tv_testLayout_answer_01 -> {
                onAnswerClicked(0)
            }

            R.id.tv_testLayout_answer_02 -> {
                onAnswerClicked(1)
            }

            R.id.tv_testLayout_answer_03 -> {
                onAnswerClicked(2)
            }
        }
    }

    interface OnMztiTestLayoutListener {
        fun onTestFinish(pAnswerList: List<MbtiAnswerData>)
    }

}