package com.mzc.mzti.common.custom

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.imageview.ShapeableImageView
import com.mzc.mzti.R
import com.mzc.mzti.dp2px
import com.mzc.mzti.model.data.mbti.MBTI

class MbtiItemView : ViewGroup {

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
                R.styleable.MbtiItemView,
                defStyleAttr,
                0
            )

        _mbti = MBTI.values()[typedArray.getInt(R.styleable.MbtiItemView_mbti, 0)]

        typedArray.recycle()
    }

    private var _mbti: MBTI = MBTI.ENFJ
        set(value) {
            field = value

            tvMbti.text = value.name
            _mbtiImgResId = when (value) {
                MBTI.ENFJ -> R.drawable.icon_mbti_profile_dummy
                MBTI.ENFP -> R.drawable.icon_mbti_profile_dummy
                MBTI.ENTJ -> R.drawable.icon_mbti_profile_dummy
                MBTI.ENTP -> R.drawable.icon_mbti_profile_dummy
                MBTI.ESFJ -> R.drawable.icon_mbti_profile_dummy
                MBTI.ESFP -> R.drawable.icon_mbti_profile_dummy
                MBTI.ESTJ -> R.drawable.icon_mbti_profile_dummy
                MBTI.ESTP -> R.drawable.icon_mbti_profile_dummy
                MBTI.INFJ -> R.drawable.icon_mbti_profile_dummy
                MBTI.INFP -> R.drawable.icon_mbti_profile_dummy
                MBTI.INTJ -> R.drawable.icon_mbti_profile_dummy
                MBTI.INTP -> R.drawable.icon_mbti_profile_dummy
                MBTI.ISFJ -> R.drawable.icon_mbti_profile_dummy
                MBTI.ISFP -> R.drawable.icon_mbti_profile_dummy
                MBTI.ISTJ -> R.drawable.icon_mbti_profile_dummy
                MBTI.ISTP -> R.drawable.icon_mbti_profile_dummy
                MBTI.MZTI -> R.drawable.icon_mbti_profile_dummy
            }
        }
    private val mbti: MBTI get() = _mbti

    @DrawableRes
    private var _mbtiImgResId: Int = R.drawable.icon_mbti_profile_dummy
        set(value) {
            field = value

            Glide.with(ivProfileImg.context)
                .load(value)
                .transform(CircleCrop())
                .fallback(R.drawable.icon_mbti_profile_dummy)
                .placeholder(R.drawable.icon_mbti_profile_dummy)
                .error(R.drawable.icon_mbti_profile_dummy)
                .into(ivProfileImg)
        }
    private val mbtiImgResId: Int get() = _mbtiImgResId

    private val ivProfileImg: ShapeableImageView
    private val tvMbti: TextView

    init {
        ivProfileImg = createImageView()
        tvMbti = createTextView()

        addView(ivProfileImg)
        addView(tvMbti)
    }

    private fun createImageView(): ShapeableImageView = ShapeableImageView(context).apply {
        id = generateViewId()
        layoutParams = LayoutParams(
            140.dp2px(context),
            140.dp2px(context)
        )
        scaleType = ImageView.ScaleType.FIT_XY
    }

    private fun createTextView(): TextView = TextView(context).apply {
        id = generateViewId()
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        gravity = Gravity.CENTER
        typeface = ResourcesCompat.getFont(context, R.font.righteous_regular)
        includeFontPadding = false
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
        letterSpacing = -0.05f
        text = mbti.name
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val height = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom

        for (idx in 0 until childCount) {
            when (val child = getChildAt(idx)) {
                is ShapeableImageView -> {
                    child.measure(
                        MeasureSpec.makeMeasureSpec(140.dp2px(context), MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(140.dp2px(context), MeasureSpec.EXACTLY)
                    )
                }

                is TextView -> {
                    child.measure(
                        MeasureSpec.makeMeasureSpec(width, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED)
                    )
                }
            }
        }

        setMeasuredDimension(140.dp2px(context), 170.dp2px(context))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val centerX = (measuredWidth - paddingLeft - paddingRight) / 2
        var yPos = paddingTop

        for (idx in 0 until childCount) {
            when (val child = getChildAt(idx)) {
                is ShapeableImageView -> {
                    val width = child.measuredWidth
                    val height = child.measuredHeight
                    val xPos = centerX - width / 2
                    child.layout(xPos, yPos, xPos + width, yPos + height)

                    yPos += height + 10.dp2px(context)
                }

                is TextView -> {
                    val width = child.measuredWidth
                    val height = child.measuredHeight
                    val xPos = centerX - width / 2
                    child.layout(xPos, yPos, xPos + width, yPos + height)
                }
            }
        }
    }

    override fun generateDefaultLayoutParams() = LayoutParams()

    override fun checkLayoutParams(lp: ViewGroup.LayoutParams?) = lp is LayoutParams

    class LayoutParams : ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

}