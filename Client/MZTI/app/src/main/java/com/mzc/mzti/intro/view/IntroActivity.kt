package com.mzc.mzti.intro.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseActivity
import com.mzc.mzti.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {

    private val binding: ActivityIntroBinding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }

    private val animHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.obj) {
                IntroAnim.I_DOWN -> {
                    binding.ivIntroI.post {
                        val deltaY = binding.ivIntroI.measuredHeight.toFloat()
                        binding.ivIntroI.animateDown(deltaY) {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.M_UP
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.M_UP -> {
                    binding.ivIntroM.post {
                        val deltaY = -binding.ivIntroM.measuredHeight.toFloat()
                        binding.ivIntroM.animateUp(deltaY) {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.T_RIGHT
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.T_RIGHT -> {
                    binding.ivIntroT.post {
                        val deltaX = binding.ivIntroT.measuredWidth.toFloat()
                        binding.ivIntroT.animateRight(deltaX) {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.M_RIGHT
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.M_RIGHT -> {
                    binding.ivIntroM.post {
                        val deltaX = binding.ivIntroM.measuredWidth.toFloat()
                        binding.ivIntroM.animateRight(deltaX) {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.T_LEFT
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.T_LEFT -> {
                    binding.ivIntroT.post {
                        val deltaX = 0f
                        binding.ivIntroT.animateLeft(deltaX) {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.M_LEFT
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.M_LEFT -> {
                    binding.ivIntroM.post {
                        val deltaX = 0f
                        binding.ivIntroM.animateLeft(deltaX) {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.I_UP
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.I_UP -> {
                    binding.ivIntroI.post {
                        val deltaY = 0f
                        binding.ivIntroI.animateUp(deltaY) {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.M_DOWN
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.M_DOWN -> {
                    binding.ivIntroM.post {
                        val deltaY = 0f
                        binding.ivIntroM.animateDown(deltaY) {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.I_DOWN
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        startAnimation()
    }

    private fun startAnimation() {
        val sendMsg = Message.obtain().apply {
            obj = IntroAnim.I_DOWN
        }
        animHandler.sendMessage(sendMsg)
    }

    private fun View.animateDown(deltaY: Float, endListener: () -> Unit) {
        animate().setDuration(DURATION)
            .translationY(deltaY)
            .withEndAction(endListener)
            .start()
    }

    private fun View.animateUp(deltaY: Float, endListener: () -> Unit) {
        animate().setDuration(DURATION)
            .translationY(deltaY)
            .withEndAction(endListener)
            .start()
    }

    private fun View.animateLeft(deltaX: Float, endListener: () -> Unit) {
        animate().setDuration(DURATION)
            .translationX(deltaX)
            .withEndAction(endListener)
            .start()
    }

    private fun View.animateRight(deltaX: Float, endListener: () -> Unit) {
        animate().setDuration(DURATION)
            .translationX(deltaX)
            .withEndAction(endListener)
            .start()
    }

    companion object {
        private const val TAG: String = "IntroActivity"

        private const val DURATION: Long = 300
    }

    private enum class IntroAnim {
        I_DOWN, M_UP, T_RIGHT, M_RIGHT, T_LEFT, M_LEFT, I_UP, M_DOWN
    }

}