package com.mzc.mzti.common.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.mzc.mzti.R
import com.mzc.mzti.dp2px
import com.mzc.mzti.model.data.MBTI
import kotlin.math.max

private const val TAG: String = "MztiBadgeView"

class MztiBadgeView : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupAttributes(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        setupAttributes(context, attrs, defStyle)
    }

    private fun setupAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.MztiBadgeView,
                defStyleAttr,
                0
            )

        _mbti = MBTI.values()[typedArray.getInt(R.styleable.MztiBadgeView_mbti, 0)]
        _badgeSize = BadgeSize.values()[typedArray.getInt(R.styleable.MztiBadgeView_badgeSize, 0)]

        _mbtiSize0 = MbtiSize.values()[typedArray.getInt(R.styleable.MztiBadgeView_mbtiSize0, 0)]
        _mbtiSize1 = MbtiSize.values()[typedArray.getInt(R.styleable.MztiBadgeView_mbtiSize1, 2)]
        _mbtiSize2 = MbtiSize.values()[typedArray.getInt(R.styleable.MztiBadgeView_mbtiSize2, 1)]
        _mbtiSize3 = MbtiSize.values()[typedArray.getInt(R.styleable.MztiBadgeView_mbtiSize3, 3)]

        typedArray.recycle()
    }

    // region Custom Attributes
    private var _mbti: MBTI = MBTI.ENFJ
        set(value) {
            field = value

            badgeBackground = when (value) {
                MBTI.ENFJ -> R.drawable.background_mbti_badge_enfj
                MBTI.ENFP -> R.drawable.background_mbti_badge_enfp
                MBTI.ENTJ -> R.drawable.background_mbti_badge_entj
                MBTI.ENTP -> R.drawable.background_mbti_badge_entp
                MBTI.ESFJ -> R.drawable.background_mbti_badge_esfj
                MBTI.ESFP -> R.drawable.background_mbti_badge_esfp
                MBTI.ESTJ -> R.drawable.background_mbti_badge_estj
                MBTI.ESTP -> R.drawable.background_mbti_badge_estp
                MBTI.INFJ -> R.drawable.background_mbti_badge_infj
                MBTI.INFP -> R.drawable.background_mbti_badge_infp
                MBTI.INTJ -> R.drawable.background_mbti_badge_intj
                MBTI.INTP -> R.drawable.background_mbti_badge_intp
                MBTI.ISFJ -> R.drawable.background_mbti_badge_isfj
                MBTI.ISFP -> R.drawable.background_mbti_badge_isfp
                MBTI.ISTJ -> R.drawable.background_mbti_badge_istj
                MBTI.ISTP -> R.drawable.background_mbti_badge_istp
                MBTI.MZTI -> R.drawable.background_mbti_badge_mzti
            }
            invalidate()
        }
    private val mbti: MBTI get() = _mbti

    @DrawableRes
    private var badgeBackground: Int = R.drawable.background_mbti_badge_istj
        set(value) {
            field = value
            setBackgroundResource(value)
        }

    private var _badgeSize: BadgeSize = BadgeSize.SMALL
        set(value) {
            field = value
            requestLayout()
        }
    private val badgeSize: BadgeSize get() = _badgeSize

    private var _mbtiSize0: MbtiSize = MbtiSize.XL
        set(value) {
            field = value
            mPaint0.textSize = value.toPx()
        }
    private val mbtiSize0: MbtiSize get() = _mbtiSize0

    private var _mbtiSize1: MbtiSize = MbtiSize.L
        set(value) {
            field = value
            mPaint1.textSize = value.toPx()
        }
    private val mbtiSize1: MbtiSize get() = _mbtiSize1

    private var _mbtiSize2: MbtiSize = MbtiSize.M
        set(value) {
            field = value
            mPaint2.textSize = value.toPx()
        }
    private val mbtiSize2: MbtiSize get() = _mbtiSize2

    private var _mbtiSize3: MbtiSize = MbtiSize.S
        set(value) {
            field = value
            mPaint3.textSize = value.toPx()
        }
    private val mbtiSize3: MbtiSize get() = _mbtiSize3
    // endregion Custom Attributes

    private val righteous: Typeface? = ResourcesCompat.getFont(context, R.font.righteous_regular)

    private val mPaint0: Paint = getDefaultPaint(textSizeInPx = mbtiSize0.toPx())
    private val mPaint1: Paint = getDefaultPaint(textSizeInPx = mbtiSize1.toPx())
    private val mPaint2: Paint = getDefaultPaint(textSizeInPx = mbtiSize2.toPx())
    private val mPaint3: Paint = getDefaultPaint(textSizeInPx = mbtiSize3.toPx())

    private val mRect: Rect = Rect()

    private fun getDefaultPaint(textSizeInPx: Float) = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        isDither = false
        style = Paint.Style.FILL
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        textSize = textSizeInPx

        typeface = righteous
    }

    private fun MbtiSize.toPx(): Float = when (badgeSize) {
        BadgeSize.SMALL -> {
            when (this) {
                MbtiSize.XL -> 26f.dp2px(context)
                MbtiSize.L -> 22f.dp2px(context)
                MbtiSize.M -> 18f.dp2px(context)
                MbtiSize.S -> 14f.dp2px(context)
            }
        }

        BadgeSize.LARGE -> {
            when (this) {
                MbtiSize.XL -> 110f.dp2px(context)
                MbtiSize.L -> 86f.dp2px(context)
                MbtiSize.M -> 62f.dp2px(context)
                MbtiSize.S -> 38f.dp2px(context)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (badgeSize) {
            BadgeSize.SMALL -> {
                val width = 80.dp2px(context)
                val height = 48.dp2px(context)
                setMeasuredDimension(width, height)
            }

            BadgeSize.LARGE -> {
                val width = 300.dp2px(context)
                val height = 180.dp2px(context)
                setMeasuredDimension(width, height)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {
            drawMbti(canvas)
        }
    }

    private fun drawMbti(pCanvas: Canvas) {
        if (mbti != MBTI.MZTI) {
            val strMbti = mbti.name
            mPaint0.getTextBounds(strMbti, 0, 1, mRect)
            val width0 = mRect.width()
            val height0 = mRect.height()

            mPaint1.getTextBounds(strMbti, 1, 2, mRect)
            val width1 = mRect.width()
            val height1 = mRect.height()

            mPaint2.getTextBounds(strMbti, 2, 3, mRect)
            val width2 = mRect.width()
            val height2 = mRect.height()

            mPaint3.getTextBounds(strMbti, 3, 4, mRect)
            val width3 = mRect.width()
            val height3 = mRect.height()

            val totalWidth = width0 + width1 + width2 + width3 + 15
            val maxHeight = max(max(height0, height1), max(height2, height3))

            val centerX = (measuredWidth - paddingLeft - paddingRight) / 2
            val centerY = (measuredHeight - paddingTop - paddingBottom) / 2

            var startX = centerX - totalWidth / 2
            val baselineY = centerY + maxHeight.div(2f)

            pCanvas.drawText(strMbti[0].toString(), startX.toFloat(), baselineY, mPaint0)

            startX += width0 + 5
            pCanvas.drawText(strMbti[1].toString(), startX.toFloat(), baselineY, mPaint1)

            startX += width1 + 5
            pCanvas.drawText(strMbti[2].toString(), startX.toFloat(), baselineY, mPaint2)

            startX += width2 + 5
            pCanvas.drawText(strMbti[3].toString(), startX.toFloat(), baselineY, mPaint3)
        }
    }

    fun updateBadge(
        pMbti: MBTI,
        pMbtiSize0: MbtiSize,
        pMbtiSize1: MbtiSize,
        pMbtiSize2: MbtiSize,
        pMbtiSize3: MbtiSize
    ) {
        _mbtiSize0 = pMbtiSize0
        _mbtiSize1 = pMbtiSize1
        _mbtiSize2 = pMbtiSize2
        _mbtiSize3 = pMbtiSize3

        _mbti = pMbti
    }

    private enum class BadgeSize {
        SMALL, LARGE
    }

    enum class MbtiSize {
        XL, L, M, S
    }

}