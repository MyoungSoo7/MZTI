package com.mzc.mzti.learning.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mzc.mzti.R
import com.mzc.mzti.applyFontStyle
import com.mzc.mzti.applyTextColor
import com.mzc.mzti.base.BaseFragment
import com.mzc.mzti.common.util.FontStyle
import com.mzc.mzti.databinding.FragmentLearningBinding
import com.mzc.mzti.main.viewmodel.MainViewModel
import com.mzc.mzti.model.data.mbti.MBTI
import com.mzc.mzti.test.view.KEY_MBTI
import com.mzc.mzti.test.view.TestActivity

private const val TAG: String = "LearningFragment"

/**
 * MBTI 학습 화면
 */
class LearningFragment : BaseFragment() {

    private val model: MainViewModel by activityViewModels()
    private var _binding: FragmentLearningBinding? = null
    private val binding: FragmentLearningBinding get() = _binding!!

    private lateinit var testIntentForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        testIntentForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {

                    }
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearningBinding.inflate(inflater, container, false)

        setObserver()
        init()

        return binding.root
    }

    private fun setObserver() {
        model.learningAgree.observe(viewLifecycleOwner, Observer { learningAgree ->
            if (learningAgree) {
                binding.ivLearningAgree.setImageResource(R.drawable.icon_checkbox_selected)
                binding.btnLearningStartTest.isEnabled = true
            } else {
                binding.ivLearningAgree.setImageResource(R.drawable.icon_checkbox_unselected)
                binding.btnLearningStartTest.isEnabled = false
            }
        })
    }

    private fun init() {
        binding.apply {
            tvLearningContent01.text = getString(R.string.learning_content_01)
                .applyTextColor(R.color.cancel_red, 0, 5, requireContext())
                .applyFontStyle(FontStyle.MEDIUM, 0, 5, requireContext())
            tvLearningContent02.text = getString(R.string.learning_content_02).let {
                it.applyTextColor(
                    R.color.cancel_red,
                    it.length - 15,
                    it.length - 2,
                    requireContext()
                ).applyFontStyle(
                    FontStyle.MEDIUM,
                    it.length - 15,
                    it.length - 2,
                    requireContext()
                )
            }

            clLearningAgree.setOnClickListener {
                model.setLearningAgreeClicked()
            }
            btnLearningStartTest.setOnClickListener {
                showSelectMbtiDialog(
                    onMbtiSelected = { mbti: MBTI ->
                        val testIntent = Intent(requireContext(), TestActivity::class.java).apply {
                            putExtra(KEY_MBTI, mbti)
                        }
                        testIntentForResult.launch(testIntent)
                    },
                    onDismissListener = {
                        model.setLearningAgree(false)
                    },
                    pTitle = getString(R.string.learning_selectMbti)
                )
            }
        }
    }

}