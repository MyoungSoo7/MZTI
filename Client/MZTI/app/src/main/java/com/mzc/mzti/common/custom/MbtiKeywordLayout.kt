package com.mzc.mzti.common.custom

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.updateMargins
import com.mzc.mzti.R
import com.mzc.mzti.dp2px

class MbtiKeywordLayout : ViewGroup {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

//    init {
//        addView(
//            createKeywordTextView(
//                ReviewWriteKeywordData(
//                    "테스트 키워드",
//                    isSelected = false,
//                    inputFlag = false
//                )
//            )
//        )
//    }
//
//    private val mKeywordDataList: MutableList<ReviewWriteKeywordData> = mutableListOf()

    // region Resource
    /**
     * 키워드 폰트
     */
    private val keywordFontFamily: Typeface? =
        ResourcesCompat.getFont(context, R.font.noto_sans_kr_regular)

    @ColorInt
    private val keywordTextColor: Int = ContextCompat.getColor(context, R.color.white)

    private var _keywordBackgroundResId: Int = R.drawable.background_logo_i
    private val keywordBackgroundResId: Int get() = _keywordBackgroundResId
    // endregion Resource

    /**
     * 키워드 레이아웃에 표시할 키워드 리스트 설정하는 함수
     *
     * 기존에 표시되던 키워드는 모두 제거됨
     *
     * @param pKeywordDataList  키워드 데이터 리스트
     */
//    fun setKeywordList(pKeywordDataList: List<ReviewWriteKeywordData>) {
//        removeAllViews()
//        mKeywordDataList.clear()
//        for (keywordData in pKeywordDataList) {
//            addKeywordData(keywordData)
//        }
//    }

    /**
     * 키워드 레이아웃에 단일 키워드를 추가하는 함수
     *
     * 기존에 표시되던 키워드는 유지되며, 새로운 키워드는 맨 뒤에 추가됨
     *
     * @param pKeywordData  추가할 키워드 데이터
     * @param pFromUser     유저의 액션으로부터 추가된 키워드인지 여부
     */
//    fun addKeywordData(
//        pKeywordData: ReviewWriteKeywordData,
//        pFromUser: Boolean = false
//    ) {
//        val findIdx = mKeywordDataList.indexOfFirst { it.keyword == pKeywordData.keyword }
//        // 새로운 키워드는 추가
//        if (findIdx == -1) {
//            mKeywordDataList.add(pKeywordData)
//            addView(createKeywordTextView(pKeywordData))
//
//            if (pFromUser) {
//                val addIdx = mKeywordDataList.size - 1
//                onKeywordClicked(mKeywordDataList[addIdx], true)
//            }
//        }
//        // 이미 추가된 키워드라면, 선택 처리
//        else {
//            if (!mKeywordDataList[findIdx].isSelected) {
//                mKeywordDataList[findIdx].isSelected = true
//
//                if (findIdx in 0 until childCount) {
//                    val child = getChildAt(findIdx)
//                    child.updateKeywordItem(mKeywordDataList[findIdx])
//                }
//
//                if (pFromUser) {
//                    onKeywordClicked(mKeywordDataList[findIdx], true)
//                }
//            }
//        }
//    }

//    private fun createKeywordTextView(pData: ReviewWriteKeywordData): TextView {
//        return TextView(context).apply {
//            id = generateViewId()
//            layoutParams = ConstraintLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
//            ).apply {
//                updateMargins(left = 10.dp2px(context), right = 10.dp2px(context))
//            }
//
//            text = pData.keyword
//            typeface = keywordFontFamily
//            includeFontPadding = false
//            letterSpacing = -0.05f
//            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12f)
//            setTextColor(keywordTextColor)
//        }
//    }

    private fun initConstraintSet(
        constraintLayout: ConstraintLayout,
        imageView: ImageView,
        textView: TextView
    ) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.apply {
            // region ImageView
            // left to parent's left
            connect(
                imageView.id,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT
            )
            // top to parent's top
            connect(
                imageView.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
            )
            // right to textView's left
            connect(
                imageView.id,
                ConstraintSet.RIGHT,
                textView.id,
                ConstraintSet.LEFT
            )
            // bottom to parent's bottom
            connect(
                imageView.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            // endregion ImageView

            // region TextView
            // left to imageView's right
            connect(
                textView.id,
                ConstraintSet.LEFT,
                imageView.id,
                ConstraintSet.RIGHT
            )
            // top to parent's top
            connect(
                textView.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP
            )
            // right to parent's right
            connect(
                textView.id,
                ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.RIGHT
            )
            // bottom to parent's bottom
            connect(
                textView.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            // endregion TextView
        }
        constraintSet.applyTo(constraintLayout)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        throw UnsupportedOperationException("You can't not set OnClickListener! You should use OnKeywordClickListener.")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        var height = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom

        val itemHeight = 30.dp2px(context)
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY)

        var xPos: Int = width
        var newHeight = 0
        for (idx in 0 until childCount) {
            val child = getChildAt(idx)
            val childLp = child.layoutParams as LayoutParams
            child.measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                childHeightMeasureSpec
            )

            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            val sum = xPos + childWidth
            // 다음 줄로 이동해야 하는 경우
            if (sum > width) {
                xPos = paddingLeft
                newHeight += childHeight + childLp.verticalItemSpacing
            }
            xPos += childWidth + childLp.horizontalItemSpacing

            if (idx == childCount - 1) {
                newHeight -= childLp.verticalItemSpacing
            }
        }

        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY,
            MeasureSpec.AT_MOST -> {
                if (newHeight < height)
                    height = newHeight
            }

            else -> {
                height = newHeight
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val width = right - left
        var xPos = paddingLeft
        var yPos = paddingTop

        for (idx in 0 until childCount) {
            val child = getChildAt(idx)
            val childLp = child.layoutParams as LayoutParams

            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            val sum = xPos + childWidth
            // 다음 줄로 이동해야 하는 경우
            if (sum > width) {
                xPos = paddingLeft
                yPos += childHeight + childLp.verticalItemSpacing
            }
            child.layout(xPos, yPos, xPos + childWidth, yPos + childHeight)
            xPos += childWidth + childLp.horizontalItemSpacing
        }
    }

    override fun generateDefaultLayoutParams() = LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        5.dp2px(context),
        6.dp2px(context)
    )

    override fun checkLayoutParams(lp: ViewGroup.LayoutParams?) = lp is LayoutParams

    class LayoutParams(
        width: Int,
        height: Int,
        val horizontalItemSpacing: Int,
        val verticalItemSpacing: Int
    ) : ViewGroup.LayoutParams(width, height)

}