package com.mzc.mzti.common.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.updateMargins
import androidx.core.view.updatePadding
import com.mzc.mzti.R
import com.mzc.mzti.applyFontStyle
import com.mzc.mzti.applyTextSize
import com.mzc.mzti.common.util.FontStyle
import com.mzc.mzti.databinding.InflateMztiCardViewBinding
import com.mzc.mzti.databinding.InflateMztiVirtualPeopleBinding
import com.mzc.mzti.dp2px
import com.mzc.mzti.model.data.compare.CompareMbtiData
import com.mzc.mzti.model.data.compare.CompareMbtiType
import com.mzc.mzti.model.data.mbti.MBTI

class MztiCardLayout : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    init {
        updatePadding(top = 3.dp2px(context), bottom = 20.dp2px(context))
        orientation = VERTICAL
    }

    fun setCompareMbtiData(pCompareMbtiData: CompareMbtiData, pMbti: MBTI) {
        removeAllViews()

        val backgroundResId = when (pMbti) {
            MBTI.INTJ,
            MBTI.INTP,
            MBTI.ENTJ,
            MBTI.ENTP -> R.drawable.background_mzti_card_purple

            MBTI.INFJ,
            MBTI.INFP,
            MBTI.ENFJ,
            MBTI.ENFP -> R.drawable.background_mzti_card_green

            MBTI.ISTJ,
            MBTI.ISFJ,
            MBTI.ESTJ,
            MBTI.ESFJ -> R.drawable.background_mzti_card_blue

            MBTI.ISTP,
            MBTI.ISFP,
            MBTI.ESTP,
            MBTI.ESFP -> R.drawable.background_mzti_card_orange

            else -> R.drawable.background_mzti_card_purple
        }
        if (pCompareMbtiData.type != CompareMbtiType.VIRTUAL_PEOPLE) {
            setBackgroundResource(backgroundResId)
        }

        for (idx in pCompareMbtiData.content.indices) {
            val content = pCompareMbtiData.content[idx]
            addContentView(
                pCompareMbtiData.type,
                content,
                (idx == 0 && pCompareMbtiData.type == CompareMbtiType.DESCRIPTION),
                backgroundResId
            )
        }
    }

    private fun addContentView(
        pType: CompareMbtiType,
        pContent: String,
        pIsHighlight: Boolean = true,
        pBackgroundResId: Int = 0
    ) {
        val inflater = LayoutInflater.from(context)
        when (pType) {
            CompareMbtiType.DESCRIPTION -> {
                val view = inflater.inflate(R.layout.inflate_mzti_card_view, this, false)
                InflateMztiCardViewBinding.bind(view).apply {
                    ivMztiCardView.setImageResource(R.drawable.icon_pin)

                    if (pIsHighlight) {
                        ivMztiCardView.visibility = View.GONE
                        tvMztiCardView.text = pContent
                            .applyTextSize(20, 0, pContent.length - 1, context)
                            .applyFontStyle(FontStyle.MEDIUM, 0, pContent.length - 1, context)
                        view.updatePadding(bottom = 8.dp2px(context))
                    } else {
                        ivMztiCardView.visibility = View.VISIBLE
                        tvMztiCardView.text = pContent
                    }
                }
                addView(view)
            }

            CompareMbtiType.GOOD_MBTI -> {
                val view = inflater.inflate(R.layout.inflate_mzti_card_view, this, false)
                InflateMztiCardViewBinding.bind(view).apply {
                    ivMztiCardView.setImageResource(R.drawable.icon_good)
                    tvMztiCardView.text = pContent
                }
                addView(view)
            }

            CompareMbtiType.WORST_MBTI -> {
                val view = inflater.inflate(R.layout.inflate_mzti_card_view, this, false)
                InflateMztiCardViewBinding.bind(view).apply {
                    ivMztiCardView.setImageResource(R.drawable.icon_bad)
                    tvMztiCardView.text = pContent
                }
                addView(view)
            }

            CompareMbtiType.VIRTUAL_PEOPLE -> {
                val view = inflater.inflate(R.layout.inflate_mzti_virtual_people, this, false)
                val data = pContent.split('-')
                InflateMztiVirtualPeopleBinding.bind(view).apply {
                    clVirtualPeople.setBackgroundResource(pBackgroundResId)
                    tvVirtualPeopleName.text = data.getOrNull(1)?.trim() ?: ""
                    tvVirtualPeopleProduct.text = data.getOrNull(0)?.trim() ?: ""
                }
                (view.layoutParams as MarginLayoutParams).updateMargins(top = 10.dp2px(context))
                addView(view)
            }

            else -> {
                val view = inflater.inflate(R.layout.inflate_mzti_card_view, this, false)
                InflateMztiCardViewBinding.bind(view).apply {
                    ivMztiCardView.setImageResource(R.drawable.icon_pin)
                    tvMztiCardView.text = pContent
                }
                addView(view)
            }
        }
    }

}