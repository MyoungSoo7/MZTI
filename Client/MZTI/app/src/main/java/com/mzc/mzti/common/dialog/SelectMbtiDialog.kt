package com.mzc.mzti.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mzc.mzti.databinding.DialogSelectMbtiBinding
import com.mzc.mzti.model.data.mbti.MBTI

class SelectMbtiDialog(
    private val onMbtiSelected: (mbti: MBTI) -> Unit,
    private val title: String = "",
    onDismissListener: () -> Unit = {}
) : BasePopupDialog(onDismissListener) {

    private var _binding: DialogSelectMbtiBinding? = null
    private val binding: DialogSelectMbtiBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSelectMbtiBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() {
        binding.apply {
            if (title.isNotEmpty()) {
                tvSelectMbtiDialogTitle.text = title
                tvSelectMbtiDialogTitle.visibility = View.VISIBLE
            } else {
                tvSelectMbtiDialogTitle.visibility = View.GONE
            }

            // INTJ
            cvSelectMbtiDialogIntj.setOnClickListener {
                onMbtiSelected(MBTI.INTJ)
                dismiss()
            }
            // INTP
            cvSelectMbtiDialogIntp.setOnClickListener {
                onMbtiSelected(MBTI.INTP)
                dismiss()
            }
            // ENTJ
            cvSelectMbtiDialogEntj.setOnClickListener {
                onMbtiSelected(MBTI.ENTJ)
                dismiss()
            }
            // ENTP
            cvSelectMbtiDialogEntp.setOnClickListener {
                onMbtiSelected(MBTI.ENTP)
                dismiss()
            }
            // INFJ
            cvSelectMbtiDialogInfj.setOnClickListener {
                onMbtiSelected(MBTI.INFJ)
                dismiss()
            }
            // INFP
            cvSelectMbtiDialogInfp.setOnClickListener {
                onMbtiSelected(MBTI.INFP)
                dismiss()
            }
            // ENFJ
            cvSelectMbtiDialogEnfj.setOnClickListener {
                onMbtiSelected(MBTI.ENFJ)
                dismiss()
            }
            // ENFP
            cvSelectMbtiDialogEnfp.setOnClickListener {
                onMbtiSelected(MBTI.ENFP)
                dismiss()
            }
            // ISTJ
            cvSelectMbtiDialogIstj.setOnClickListener {
                onMbtiSelected(MBTI.ISTJ)
                dismiss()
            }
            // ISFJ
            cvSelectMbtiDialogIsfj.setOnClickListener {
                onMbtiSelected(MBTI.ISFJ)
                dismiss()
            }
            // ESTJ
            cvSelectMbtiDialogEstj.setOnClickListener {
                onMbtiSelected(MBTI.ESTJ)
                dismiss()
            }
            // ESFJ
            cvSelectMbtiDialogEsfj.setOnClickListener {
                onMbtiSelected(MBTI.ESFJ)
                dismiss()
            }
            // ISTP
            cvSelectMbtiDialogIstp.setOnClickListener {
                onMbtiSelected(MBTI.ISTP)
                dismiss()
            }
            // ISFP
            cvSelectMbtiDialogIsfp.setOnClickListener {
                onMbtiSelected(MBTI.ISFP)
                dismiss()
            }
            // ESTP
            cvSelectMbtiDialogEstp.setOnClickListener {
                onMbtiSelected(MBTI.ESTP)
                dismiss()
            }
            // ESFP
            cvSelectMbtiDialogEsfp.setOnClickListener {
                onMbtiSelected(MBTI.ESFP)
                dismiss()
            }
        }
    }

}