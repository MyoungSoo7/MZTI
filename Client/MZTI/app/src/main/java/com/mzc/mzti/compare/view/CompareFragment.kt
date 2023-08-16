package com.mzc.mzti.compare.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mzc.mzti.R
import com.mzc.mzti.applyTextColor
import com.mzc.mzti.base.BaseFragment
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.databinding.FragmentCompareBinding
import com.mzc.mzti.main.viewmodel.MainViewModel
import com.mzc.mzti.model.data.compare.CompareMbtiData
import com.mzc.mzti.model.data.compare.CompareMbtiType
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.model.data.mbti.getProfileImgResId

private const val TAG: String = "CompareFragment"

/**
 * MBTI 비교 화면
 */
class CompareFragment : BaseFragment() {

    private val model: MainViewModel by activityViewModels()
    private var _binding: FragmentCompareBinding? = null
    private val binding: FragmentCompareBinding get() = _binding!!

    // region Fragment LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompareBinding.inflate(inflater, container, false)

        setObserver()
        init()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // endregion Fragment LifeCycle

    private fun setObserver() {
        model.leftMbti.observe(viewLifecycleOwner, Observer { mbti ->
            DLog.d(TAG, "leftMbti=${mbti.name}")
            binding.apply {
                tvCompareLeftMbti.text = mbti.name
                tvCompareUpperLeftMbti.text = mbti.name
                ivCompareLeftProfileImg.setImageResource(getProfileImgResId(mbti))
            }
        })

        model.rightMbti.observe(viewLifecycleOwner, Observer { mbti ->
            DLog.d(TAG, "rightMbti=${mbti.name}")
            binding.apply {
                tvCompareRightMbti.text = mbti.name
                tvCompareUpperRightMbti.text = mbti.name
                ivCompareRightProfileImg.setImageResource(getProfileImgResId(mbti))
            }
        })

        model.mbtiCompareResult.observe(viewLifecycleOwner, Observer { result ->
            if (result != null) {
                model.setProgressFlag(false)
                binding.svCompare.scrollTo(0, 0)
                updateLayout(result.leftMbtiData, result.rightMbtiData)
            }
        })
    }

    private fun init() {
        binding.apply {
            // Left MBTI
            clCompareLeftMbti.setOnClickListener {
                onLeftProfileImgClicked()
            }
            ivCompareLeftProfileImg.setOnClickListener {
                onLeftProfileImgClicked()
            }
            clCompareUpperLeftMbti.setOnClickListener {
                onLeftProfileImgClicked()
            }
            // Right MBTI
            clCompareRightMbti.setOnClickListener {
                onRightProfileImgClicked()
            }
            ivCompareRightProfileImg.setOnClickListener {
                onRightProfileImgClicked()
            }
            clCompareUpperRightMbti.setOnClickListener {
                onRightProfileImgClicked()
            }

            tvCompareWarningTitle.text = getString(R.string.compare_warningTitle)
                .applyTextColor(R.color.cancel_red, 9, 11, requireContext())

            svCompare.setOnScrollChangeListener { sv, x, y, oldX, oldY ->
                val yPos = clCompareMbtiProfile.top + clCompareLeftMbti.top
                DLog.d(TAG, "y=$y, yPos=$yPos")

                clCompareUpperMbti.visibility = if (y >= yPos) View.VISIBLE else View.GONE
            }
        }

        model.requestMbtiCompare()
    }

    private fun onLeftProfileImgClicked() {
        showSelectMbtiDialog(
            onMbtiSelected = { mbti: MBTI ->
                model.setLeftMbti(mbti)
            },
            onDismissListener = {
            }
        )
    }

    private fun onRightProfileImgClicked() {
        showSelectMbtiDialog(
            onMbtiSelected = { mbti: MBTI ->
                model.setRightMbti(mbti)
            },
            onDismissListener = {
            }
        )
    }

    private fun updateLayout(pLeftMbti: List<CompareMbtiData>, pRightMbti: List<CompareMbtiData>) {
        val leftMbti = model.leftMbti.value ?: MBTI.MZTI
        val rightMbti = model.rightMbti.value ?: MBTI.MZTI

        for (data in pLeftMbti) {
            DLog.d(TAG, "leftMbti=$data")
            when (data.type) {
                CompareMbtiType.DESCRIPTION -> {
                    binding.cvCompareLeftDescription.setCompareMbtiData(data, leftMbti)
                }

                CompareMbtiType.KEYWORD -> {
                    binding.cvCompareLeftKeyword.setKeywordList(data.content, leftMbti)
                }

                CompareMbtiType.GOOD_JOB -> {
                    binding.cvCompareLeftGoodJob.setCompareMbtiData(data, leftMbti)
                }

                CompareMbtiType.LOVE_STYLE -> {
                    binding.cvCompareLeftLoveStyle.setCompareMbtiData(data, leftMbti)
                }

                CompareMbtiType.GOOD_PEOPLE -> {
                    binding.cvCompareLeftGoodPeople.setCompareMbtiData(data, leftMbti)
                }

                CompareMbtiType.GOOD_MBTI -> {
                    binding.cvCompareLeftBestMbti.setCompareMbtiData(data, leftMbti)
                }

                CompareMbtiType.WORST_MBTI -> {
                    binding.cvCompareLeftWorstMbti.setCompareMbtiData(data, leftMbti)
                }

                CompareMbtiType.TALKING_HABIT -> {
                    val resId = when (leftMbti) {
                        MBTI.INTJ,
                        MBTI.INTP,
                        MBTI.ENTJ,
                        MBTI.ENTP -> R.drawable.icon_balloon_purple

                        MBTI.INFJ,
                        MBTI.INFP,
                        MBTI.ENFJ,
                        MBTI.ENFP -> R.drawable.icon_balloon_green

                        MBTI.ISTJ,
                        MBTI.ISFJ,
                        MBTI.ESTJ,
                        MBTI.ESFJ -> R.drawable.icon_balloon_blue

                        MBTI.ISTP,
                        MBTI.ISFP,
                        MBTI.ESTP,
                        MBTI.ESFP -> R.drawable.icon_balloon_orange

                        else -> R.drawable.icon_balloon_orange
                    }
                    binding.tvCompareLeftTalkHabit.setBackgroundResource(resId)
                    binding.tvCompareLeftTalkHabit.text = data.content.firstOrNull() ?: ""
                }

                CompareMbtiType.VIRTUAL_PEOPLE -> {
                    binding.cvCompareLeftVirtualPeople.setCompareMbtiData(data, leftMbti)
                }
            }
        }

        for (data in pRightMbti) {
            when (data.type) {
                CompareMbtiType.DESCRIPTION -> {
                    binding.cvCompareRightDescription.setCompareMbtiData(data, rightMbti)
                }

                CompareMbtiType.KEYWORD -> {
                    binding.cvCompareRightKeyword.setKeywordList(data.content, rightMbti)
                }

                CompareMbtiType.GOOD_JOB -> {
                    binding.cvCompareRightGoodJob.setCompareMbtiData(data, rightMbti)
                }

                CompareMbtiType.LOVE_STYLE -> {
                    binding.cvCompareRightLoveStyle.setCompareMbtiData(data, rightMbti)
                }

                CompareMbtiType.GOOD_PEOPLE -> {
                    binding.cvCompareRightGoodPeople.setCompareMbtiData(data, rightMbti)
                }

                CompareMbtiType.GOOD_MBTI -> {
                    binding.cvCompareRightBestMbti.setCompareMbtiData(data, rightMbti)
                }

                CompareMbtiType.WORST_MBTI -> {
                    binding.cvCompareRightWorstMbti.setCompareMbtiData(data, rightMbti)
                }

                CompareMbtiType.TALKING_HABIT -> {
                    val resId = when (rightMbti) {
                        MBTI.INTJ,
                        MBTI.INTP,
                        MBTI.ENTJ,
                        MBTI.ENTP -> R.drawable.icon_balloon_purple

                        MBTI.INFJ,
                        MBTI.INFP,
                        MBTI.ENFJ,
                        MBTI.ENFP -> R.drawable.icon_balloon_green

                        MBTI.ISTJ,
                        MBTI.ISFJ,
                        MBTI.ESTJ,
                        MBTI.ESFJ -> R.drawable.icon_balloon_blue

                        MBTI.ISTP,
                        MBTI.ISFP,
                        MBTI.ESTP,
                        MBTI.ESFP -> R.drawable.icon_balloon_orange

                        else -> R.drawable.icon_balloon_orange
                    }
                    binding.tvCompareRightTalkHabit.setBackgroundResource(resId)
                    binding.tvCompareRightTalkHabit.text = data.content.firstOrNull() ?: ""
                }

                CompareMbtiType.VIRTUAL_PEOPLE -> {
                    binding.cvCompareRightVirtualPeople.setCompareMbtiData(data, rightMbti)
                }
            }
        }
    }

}