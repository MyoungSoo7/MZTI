package com.mzc.mzti.common.custom

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mzc.mzti.R
import com.mzc.mzti.common.dialog.ProgressDialog

/**
 * BottomSheetDialogFragment와 동일하게 하단에서 위로 올라오는 Activity를 구현하기 위해 사용됨
 *
 * BottomSheetActivity를 상속 받은 후, window.setLayout 함수로 window의 크기를 반드시 설정해야 함
 */
open class BottomSheetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.MztiBottomSheetDialogTheme)
        overridePendingTransition(R.anim.enter_from_bottom_200, R.anim.exit_to_bottom_200)

        window?.attributes?.gravity = Gravity.BOTTOM
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.enter_from_bottom_200, R.anim.exit_to_bottom_200)
    }

    private lateinit var mProgressDialog: ProgressDialog

    protected fun showProgress(pMsg: String = "") {
        if (!this::mProgressDialog.isInitialized) {
            mProgressDialog = ProgressDialog(this, "")
        }

        mProgressDialog.setMessage(pMsg)
        mProgressDialog.show()
    }

    protected fun dismissProgress() {
        if (this::mProgressDialog.isInitialized) {
            mProgressDialog.dismiss()
        }
    }

    protected fun showToastMsg(pMsg: String) {
        Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show()
    }

    protected fun showErrorMsg() {
        Toast.makeText(
            this,
            resources.getText(R.string.toast_msg_error_001),
            Toast.LENGTH_SHORT
        ).show()
    }

}