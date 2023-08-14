package com.mzc.mzti.sign.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseFragment
import com.mzc.mzti.clearFocus
import com.mzc.mzti.databinding.FragmentSignUpBinding
import com.mzc.mzti.model.data.router.SignUpState
import com.mzc.mzti.sign.viewmodel.SignViewModel

private const val TAG: String = "SignUpFragment"

/**
 * 회원가입 화면
 */
class SignUpFragment : BaseFragment() {

    private val model: SignViewModel by activityViewModels()
    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding get() = _binding!!

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
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        setObserver()
        init()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        model.clearSignUpData()
        _binding = null
    }
    // endregion Fragment LifeCycle

    private fun setObserver() {
        model.signUpState.observe(viewLifecycleOwner, Observer { signUpState ->
            updateLayout(signUpState)
        })

        model.enableCheckId.observe(viewLifecycleOwner, Observer { enable ->
            binding.btnSignUpIdNext.isEnabled = enable
        })

        model.enableCheckPw.observe(viewLifecycleOwner, Observer { enable ->
            binding.btnSignUpPwNext.isEnabled = enable
        })

        model.enableCheckNickname.observe(viewLifecycleOwner, Observer { enable ->
            binding.btnSignUpNicknameNext.isEnabled = enable
        })

        model.enableCheckMbti.observe(viewLifecycleOwner, Observer { enable ->
            binding.btnSignUpMbtiConfirm.isEnabled = enable
        })
    }

    private fun init() {
        binding.apply {
            ibSignUpBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            btnSignUpIdNext.setOnClickListener {
                model.checkSignUpId()
            }

            btnSignUpPwNext.setOnClickListener {
                model.checkSignUpPw()
            }

            btnSignUpNicknameNext.setOnClickListener {
                model.checkSignUpNickname()
            }

            btnSignUpMbtiConfirm.setOnClickListener {
                model.checkSignUpMbti()
            }

            etSignUpId.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = s?.toString() ?: ""
                    model.setSignUpId(input)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
            etSignUpId.setOnEditorActionListener(onEditorActionListener)

            etSignUpPw.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = s?.toString() ?: ""
                    model.setSignUpPw(input)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
            etSignUpPw.setOnEditorActionListener(onEditorActionListener)

            etSignUpPwAgain.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = s?.toString() ?: ""
                    model.setSignUpPwAgain(input)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
            etSignUpPwAgain.setOnEditorActionListener(onEditorActionListener)

            etSignUpNickname.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = s?.toString() ?: ""
                    model.setSignUpNickname(input)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
            etSignUpNickname.setOnEditorActionListener(onEditorActionListener)
        }
    }

    private fun updateLayout(pSignUpState: SignUpState) {
        when (pSignUpState) {
            SignUpState.ID -> {
                binding.apply {
                    tvSignUpTitle.text = getString(R.string.signUp_title_id)
                    clSignUpId.visible()
                    clSignUpPw.gone()
                    clSignUpNickname.gone()
                    clSignUpMbti.gone()
                }
            }

            SignUpState.PW -> {
                binding.apply {
                    tvSignUpTitle.text = getString(R.string.signUp_title_pw)
                    clSignUpId.slideOut()
                    clSignUpPw.slideIn()
                    clSignUpNickname.gone()
                    clSignUpMbti.gone()
                }
            }

            SignUpState.NICKNAME -> {
                binding.apply {
                    tvSignUpTitle.text = getString(R.string.signUp_title_nickname)
                    clSignUpId.gone()
                    clSignUpPw.slideOut()
                    clSignUpNickname.slideIn()
                    clSignUpMbti.gone()
                }
            }

            SignUpState.MBTI -> {
                binding.apply {
                    tvSignUpTitle.text = getString(R.string.signUp_title_mbti)
                    clSignUpId.gone()
                    clSignUpPw.gone()
                    clSignUpNickname.slideOut()
                    clSignUpMbti.slideIn()
                }
            }
        }
    }

    private fun View.gone() {
        this.visibility = View.GONE
    }

    private fun View.visible() {
        this.visibility = View.VISIBLE
    }

    private fun View.slideOut() {
        val width = measuredWidth.toFloat()
        animate().setDuration(250)
            .translationX(-width)
            .withStartAction {
                translationX = 0f
            }
            .withEndAction {
                visibility = View.GONE
            }
            .start()
    }

    private fun View.slideIn() {
        val width = measuredWidth.toFloat()
        animate().setDuration(250)
            .translationX(0f)
            .withStartAction {
                visibility = View.VISIBLE
                translationX = width
            }
            .start()
    }

    private val onEditorActionListener: TextView.OnEditorActionListener =
        object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (v is EditText) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        when (v.id) {
                            R.id.et_signUp_id -> {
                                if (model.checkSignUpId()) {
                                    v.clearFocus(pIsHideKeyboard = true)
                                }
                            }

                            R.id.et_signUp_pw -> {
                                binding.etSignUpPwAgain.requestFocus()
                            }

                            R.id.et_signUp_pwAgain -> {
                                if (model.checkSignUpPw()) {
                                    v.clearFocus(pIsHideKeyboard = true)
                                }
                            }

                            R.id.et_signUp_nickname -> {
                                if (model.checkSignUpNickname()) {
                                    v.clearFocus(pIsHideKeyboard = true)
                                }
                            }
                        }
                    }
                    return true
                }
                return false
            }
        }

}