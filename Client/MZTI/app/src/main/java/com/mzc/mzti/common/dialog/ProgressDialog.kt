package com.mzc.mzti.common.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.mzc.mzti.R
import com.mzc.mzti.databinding.DialogMztiProgressBinding

class ProgressDialog(
    context: Context,
    private val message: String = ""
) : Dialog(context, R.style.ProgressDialogTheme) {

    private val binding: DialogMztiProgressBinding by lazy {
        DialogMztiProgressBinding.inflate(layoutInflater)
    }

    private var _animFlag: Boolean = false
    private val animFlag: Boolean get() = _animFlag

    private val rotate0to90: Animation =
        AnimationUtils.loadAnimation(context, R.anim.progress_dialog_rotate_0to90).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (animFlag) {
                        binding.ivProgressDialog.startAnimation(rotate90to180)
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
            })
        }

    private val rotate90to180: Animation =
        AnimationUtils.loadAnimation(context, R.anim.progress_dialog_rotate_90to180).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (animFlag) {
                        binding.ivProgressDialog.startAnimation(rotate180to270)
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
            })
        }


    private val rotate180to270: Animation =
        AnimationUtils.loadAnimation(context, R.anim.progress_dialog_rotate_180to270).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (animFlag) {
                        binding.ivProgressDialog.startAnimation(rotate270to0)
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
            })
        }

    private val rotate270to0: Animation =
        AnimationUtils.loadAnimation(context, R.anim.progress_dialog_rotate_270to0).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    if (animFlag) {
                        binding.ivProgressDialog.startAnimation(rotate0to90)
                    }
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
            })
        }

    private val animHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                // Dialog Message 변경
                HANDLER_WHAT_SET_MESSAGE -> {
                    binding.apply {
                        tvProgressDialog.text = message
                        tvProgressDialog.visibility =
                            if (message.isEmpty()) View.GONE else View.VISIBLE
                    }
                }
                // Dialog Animation
                HANDLER_WHAT_START_ANIM -> {
                    if (animFlag) {
                        when (msg.obj) {
                            ProgressAnim.ROTATE_0_TO_90 -> {
                                binding.ivProgressDialog.startAnimation(rotate0to90)
                            }

                            ProgressAnim.ROTATE_90_TO_180 -> {
                                binding.ivProgressDialog.startAnimation(rotate90to180)
                            }

                            ProgressAnim.ROTATE_180_TO_270 -> {
                                binding.ivProgressDialog.startAnimation(rotate180to270)
                            }

                            ProgressAnim.ROTATE_270_TO_0 -> {
                                binding.ivProgressDialog.startAnimation(rotate270to0)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.attributes = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
            dimAmount = 0.6f
        }

        setContentView(binding.root)
        init()
    }

    override fun show() {
        super.show()

        _animFlag = true
        startAnimation()
    }

    override fun dismiss() {
        super.dismiss()
        _animFlag = false
    }

    private fun init() {
        setMessage(message)
    }

    private fun startAnimation() {
        val msg = Message.obtain().apply {
            what = HANDLER_WHAT_START_ANIM
            obj = ProgressAnim.ROTATE_0_TO_90
        }
        animHandler.sendMessage(msg)
    }

    fun setMessage(pMessage: String) {
        val msg = Message.obtain().apply {
            what = HANDLER_WHAT_SET_MESSAGE
            obj = pMessage
        }
        animHandler.sendMessage(msg)
    }

    companion object {
        private const val TAG: String = "ProgressDialog"

        private const val HANDLER_WHAT_SET_MESSAGE: Int = 0
        private const val HANDLER_WHAT_START_ANIM: Int = 1
    }

    enum class ProgressAnim {
        ROTATE_0_TO_90,

        ROTATE_90_TO_180,

        ROTATE_180_TO_270,

        ROTATE_270_TO_0
    }

}