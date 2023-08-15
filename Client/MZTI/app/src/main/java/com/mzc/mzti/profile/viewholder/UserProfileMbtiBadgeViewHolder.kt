package com.mzc.mzti.profile.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.mzc.mzti.databinding.ItemUserProfileMbtiBadgeBinding
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.mbti.MbtiBadgeData

class UserProfileMbtiBadgeViewHolder(
    private val binding: ItemUserProfileMbtiBadgeBinding,
    private val onLetsGoTestBtnClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.clMbtiBadgeLetsGoTest.setOnClickListener {
            onLetsGoTestBtnClicked()
        }
    }

    fun bindData(pMbtiBadgeList: List<MbtiBadgeData>) {
        val pos = bindingAdapterPosition

        binding.apply {
            for (mbtiBadge in pMbtiBadgeList) {
                when (mbtiBadge.mbti) {
                    MBTI.ENFJ -> {
                        cvMbtiBadgeEnfj.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.ENFP -> {
                        cvMbtiBadgeEnfp.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.ENTJ -> {
                        cvMbtiBadgeEntj.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.ENTP -> {
                        cvMbtiBadgeEntp.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.ESFJ -> {
                        cvMbtiBadgeEsfj.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.ESFP -> {
                        cvMbtiBadgeEsfp.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.ESTJ -> {
                        cvMbtiBadgeEstj.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.ESTP -> {
                        cvMbtiBadgeEstp.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.INFJ -> {
                        cvMbtiBadgeInfj.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.INFP -> {
                        cvMbtiBadgeInfp.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.INTJ -> {
                        cvMbtiBadgeIntj.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.INTP -> {
                        cvMbtiBadgeIntp.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.ISFJ -> {
                        cvMbtiBadgeIsfj.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.ISFP -> {
                        cvMbtiBadgeIsfp.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.ISTJ -> {
                        cvMbtiBadgeIstj.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.ISTP -> {
                        cvMbtiBadgeIstp.updateBadge(
                            if (mbtiBadge.isFlag) mbtiBadge.mbti else MBTI.MZTI,
                            mbtiBadge.mbtiSize0,
                            mbtiBadge.mbtiSize1,
                            mbtiBadge.mbtiSize2,
                            mbtiBadge.mbtiSize3
                        )
                    }

                    MBTI.MZTI -> {

                    }
                }
            }
        }
    }

}