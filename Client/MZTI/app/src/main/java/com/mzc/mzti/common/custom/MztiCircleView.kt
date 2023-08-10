package com.mzc.mzti.common.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.mzc.mzti.R

private const val TAG: String = "MztiCircleView"

class MztiCircleView : View {

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

    private val minWidth: Float = convertDpToPx(MIN_WIDTH_IN_DP)
    private val minHeight: Float = convertDpToPx(MIN_HEIGHT_IN_DP)

    @ColorInt
    private var _mztiColor: Int = ContextCompat.getColor(context, R.color.enfj)
        set(value) {
            field = value
            mPaint.color = value
            invalidate()
        }
    val mztiColor: Int get() = _mztiColor

    private val mPaint: Paint = getDefaultPaint()
    private val mRect: RectF = RectF()

    private fun setupAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.MztiCircleView,
                defStyleAttr,
                0
            )
        _mztiColor =
            typedArray.getColor(R.styleable.MztiCircleView_mztiColor, _mztiColor)
        typedArray.recycle()
    }

    private fun convertDpToPx(dp: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

    private fun getDefaultPaint() = Paint().apply {
        color = mztiColor
        isAntiAlias = true
        isDither = false
        style = Paint.Style.FILL
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private fun getRect(): RectF {
        val mWidth = measuredWidth - paddingLeft - paddingRight
        val mHeight = measuredHeight - paddingTop - paddingBottom
        return mRect.apply {
            left = paddingLeft.toFloat()
            top = paddingTop.toFloat()
            right = left + mWidth
            bottom = top + mHeight
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(widthMeasureSpec).let { width ->
                    if (width >= minWidth) width else minWidth.toInt()
                } + paddingLeft + paddingRight
            }

            else -> {
                minWidth.toInt() + paddingLeft + paddingRight
            }
        }
        val mHeight = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> {
                MeasureSpec.getSize(heightMeasureSpec).let { height ->
                    if (height >= minHeight) height else minHeight.toInt()
                } + paddingTop + paddingBottom
            }

            else -> {
                minHeight.toInt() + paddingTop + paddingBottom
            }
        }

        val maxLength = if (mWidth > mHeight) mWidth else mHeight
        setMeasuredDimension(maxLength, maxLength)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {
            canvas.drawOval(getRect(), mPaint)
        }
    }

    fun setDiningColor(@ColorInt pColorInt: Int) {
        this._mztiColor = pColorInt
    }

    companion object {
        /**
         * 최소 너비 (dp 단위)
         */
        private const val MIN_WIDTH_IN_DP: Float = 150f

        /**
         * 최소 높이 (dp 단위)
         */
        private const val MIN_HEIGHT_IN_DP: Float = 150f
    }

}