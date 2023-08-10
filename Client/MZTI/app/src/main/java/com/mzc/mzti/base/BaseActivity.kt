package com.mzc.mzti.base

import android.content.Context
import android.graphics.Rect
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mzc.mzti.R
import com.mzc.mzti.common.dialog.ProgressDialog
import com.mzc.mzti.common.util.VibrateManager

open class BaseActivity : AppCompatActivity() {

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

    protected fun vibrate() {
        VibrateManager.requestVibrate(this, VibrateManager.VibrationType.TICK)
    }

    // region Fragment 관리
    protected fun addFragment(frameLayoutID: Int, fragment: Fragment, tag: String? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(frameLayoutID, fragment, tag).commitAllowingStateLoss()
    }

    protected fun setFragment(frameLayoutID: Int, fragment: Fragment, tag: String? = null) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(frameLayoutID, fragment, tag).commit()
    }
    // endregion Fragment 관리

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus

            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)

                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    view.post {
                        view.clearFocus()
                        val imm: InputMethodManager =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}