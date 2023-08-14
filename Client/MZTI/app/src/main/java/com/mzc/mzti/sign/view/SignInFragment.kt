package com.mzc.mzti.sign.view

import android.content.Intent
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
import com.mzc.mzti.databinding.FragmentSignInBinding
import com.mzc.mzti.model.data.router.SignRouter
import com.mzc.mzti.sign.viewmodel.SignViewModel

private const val TAG: String = "SignInFragment"

/**
 * 로그인 화면
 */
class SignInFragment : BaseFragment() {

    private val model: SignViewModel by activityViewModels()
    private var _binding: FragmentSignInBinding? = null
    private val binding: FragmentSignInBinding get() = _binding!!

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
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

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
        model.enableLogin.observe(viewLifecycleOwner, Observer { enable ->
            binding.btnSignIn.isEnabled = enable
        })
    }

    private fun init() {
        binding.apply {
            tvSignInLetsGoSignUp.setOnClickListener {
                model.setSignRouter(SignRouter.SIGN_UP)
            }

            etSignInId.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = s?.toString() ?: ""
                    model.setLoginId(input)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
            etSignInId.setOnEditorActionListener(onEditorActionListener)

            etSignInPw.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val input = s?.toString() ?: ""
                    model.setLoginPw(input)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
            etSignInPw.setOnEditorActionListener(onEditorActionListener)
        }
    }

    private val onEditorActionListener: TextView.OnEditorActionListener =
        object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (v is EditText) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        when (v.id) {
                            R.id.et_signIn_id -> {
                                binding.etSignInPw.requestFocus()
                            }
                        }
                    } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                        v.clearFocus(pIsHideKeyboard = true)
                        when (v.id) {
                            R.id.et_signIn_pw -> {
                                val input = v.text.toString()
                                if (input.isNotEmpty()) {
                                    model.requestLogin()
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