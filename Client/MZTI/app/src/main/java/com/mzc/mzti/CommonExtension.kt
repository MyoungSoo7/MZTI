package com.mzc.mzti

import android.content.Context
import android.text.Spannable
import android.text.SpannedString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.buildSpannedString
import com.mzc.mzti.common.util.CustomTypefaceSpan
import com.mzc.mzti.common.util.FontStyle

fun Float.dp2px(pContext: Context): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, pContext.resources.displayMetrics)

fun Int.dp2px(pContext: Context): Int = this.toFloat().dp2px(pContext).toInt()

fun Float.px2dp(pContext: Context): Float =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this, pContext.resources.displayMetrics)

fun Int.px2dp(pContext: Context): Int = this.toFloat().px2dp(pContext).toInt()

/**
 * 문자열에 FontStyle을 적용하는 함수
 *
 * @param pFontStyle    적용할 FontStyle
 * @param pStart        시작 인덱스 (Inclusive)
 * @param pEnd          종료 인덱스 (Inclusive)
 * @param pContext      Context
 *
 * @see FontStyle
 */
fun String.applyFontStyle(
    pFontStyle: FontStyle,
    pStart: Int,
    pEnd: Int,
    pContext: Context
): SpannedString {
    return buildSpannedString {
        append(this@applyFontStyle)

        if (pStart < pEnd && pStart in indices && pEnd in indices) {
            val fontResId = when (pFontStyle) {
                FontStyle.THIN -> R.font.noto_sans_kr_thin
                FontStyle.LIGHT -> R.font.noto_sans_kr_light
                FontStyle.REGULAR -> R.font.noto_sans_kr_regular
                FontStyle.MEDIUM -> R.font.noto_sans_kr_medium
                FontStyle.BOLD -> R.font.noto_sans_kr_bold
            }

            setSpan(
                CustomTypefaceSpan("", ResourcesCompat.getFont(pContext, fontResId)!!),
                pStart,
                pEnd + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}

/**
 * 문자열에 색상을 적용하는 함수
 *
 * @param pColorResId   Color Resource Id
 * @param pStart        시작 인덱스 (Inclusive)
 * @param pEnd          종료 인덱스 (Inclusive)
 * @param pContext      Context
 */
fun String.applyTextColor(
    @ColorRes pColorResId: Int,
    pStart: Int,
    pEnd: Int,
    pContext: Context
): SpannedString {
    return buildSpannedString {
        append(this@applyTextColor)

        if (pStart < pEnd && pStart in indices && pEnd in indices) {
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(pContext, pColorResId)),
                pStart,
                pEnd + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}

/**
 * 문자열에 색상을 적용하는 함수
 *
 * @param pColorResId   Color Resource Id
 * @param pRangeList    색상을 적용할 범위 리스트
 * @param pContext      Context
 */
fun String.applyTextColor(
    @ColorRes pColorResId: Int,
    pRangeList: List<IntRange>,
    pContext: Context
): SpannedString {
    return buildSpannedString {
        append(this@applyTextColor)

        for (range in pRangeList) {
            val start = range.first
            val end = range.last

            if (start < end && start in indices && end in indices) {
                setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(pContext, pColorResId)),
                    start,
                    end + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }
}

/**
 * 문자열의 텍스트 사이즈를 변경하는 함수
 *
 * @param pTextSizeInDp DP 단위의 텍스트 사이즈
 * @param pStart        시작 인덱스 (Inclusive)
 * @param pEnd          종료 인덱스 (Inclusive)
 * @param pContext      Context
 */
fun String.applyTextSize(
    pTextSizeInDp: Int,
    pStart: Int,
    pEnd: Int,
    pContext: Context
): SpannedString {
    return buildSpannedString {
        append(this@applyTextSize)

        if (pStart < pEnd && pStart in indices && pEnd in indices) {
            setSpan(
                AbsoluteSizeSpan(pTextSizeInDp.dp2px(pContext)),
                pStart,
                pEnd + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}

