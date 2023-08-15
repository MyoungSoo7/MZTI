package com.mzc.mzti.friends.view

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.custom.BottomSheetActivity
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.databinding.ActivityAddFriendBinding
import com.mzc.mzti.friends.viewmodel.AddFriendViewModel

private const val TAG: String = "AddFriendActivity"
const val KEY_OTHER_PROFILE_DATA: String = "other_profile_data"

class AddFriendActivity : BottomSheetActivity() {

    private val model: AddFriendViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(application)
        )[AddFriendViewModel::class.java]
    }
    private val binding: ActivityAddFriendBinding by lazy {
        ActivityAddFriendBinding.inflate(layoutInflater)
    }
    private val mOutRect: Rect = Rect()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        setObserver()
        init()
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        finish()
    }

    private fun setObserver() {
        model.progressFlag.observe(this, Observer { flag ->
            if (flag) {
                showProgress("")
            } else {
                dismissProgress()
            }
        })

        model.exceptionData.observe(this, Observer { exception ->
            exception.message?.let { msg ->
                model.setProgressFlag(false)
                showErrorMsg()
            }
        })

        model.apiFailMsg.observe(this, Observer { msg ->
            model.setProgressFlag(false)
            showToastMsg(msg)
        })

        model.friendId.observe(this, Observer { friendId ->
            binding.btnAddFriendConfirm.isEnabled = friendId.isNotEmpty()
        })

        model.addFriendResult.observe(this, Observer { otherProfileData ->
            val resultIntent = Intent().apply {
                putExtra(KEY_OTHER_PROFILE_DATA, otherProfileData)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        })
    }

    private fun init() {
        binding.apply {
            ivAddFriendClose.setOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }
            btnAddFriendConfirm.setOnClickListener {
                requestAddFriend()
            }
            tvAddFriendMyId.text = MztiSession.userId

            etAddFriendFriendId.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        val input = s?.toString()?.trim() ?: ""
                        model.setFriendId(input)
                    }

                    override fun afterTextChanged(s: Editable?) {
                    }
                }
            )
            etAddFriendFriendId.setOnEditorActionListener { textView: TextView, actionId: Int, event: KeyEvent ->
                if (textView is EditText) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        requestAddFriend()
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }

            etAddFriendFriendId.post {
                etAddFriendFriendId.requestFocus()
            }
        }
    }

    private fun requestAddFriend() {
        model.requestAddFriend()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            binding.clAddFriend.getGlobalVisibleRect(mOutRect)

            if (!mOutRect.contains(ev.x.toInt(), ev.y.toInt())) {
                DLog.d(
                    "${TAG}_dispatchTouchEvent",
                    "rect=$mOutRect, x=${ev.x}, y=${ev.y}"
                )
                setResult(RESULT_CANCELED)
                finish()
            }
        }

        return super.dispatchTouchEvent(ev)
    }

}