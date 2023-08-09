package com.mzc.mzti.intro.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.mzc.mzti.R
import com.mzc.mzti.base.BaseActivity
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.databinding.ActivityIntroBinding
import com.mzc.mzti.intro.viewmodel.IntroViewModel

class IntroActivity : BaseActivity() {

    private val model: IntroViewModel by lazy {
        ViewModelProvider(
            this,
            BaseViewModel.Factory(application)
        )[IntroViewModel::class.java]
    }
    private val binding: ActivityIntroBinding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }

    private val animHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.obj) {
                IntroAnim.I_DOWN -> {
                    binding.ivIntroI.post {
                        binding.ivIntroI.animateDown {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.M_UP
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.M_UP -> {
                    binding.ivIntroM.post {
                        binding.ivIntroM.animateUp {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.T_RIGHT
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.T_RIGHT -> {
                    binding.ivIntroT.post {
                        binding.ivIntroT.animateRight {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.M_RIGHT
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.M_RIGHT -> {
                    binding.ivIntroM.post {
                        binding.ivIntroM.animateRight {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.T_LEFT
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.T_LEFT -> {
                    binding.ivIntroT.post {
                        binding.ivIntroT.animateLeft {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.M_LEFT
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.M_LEFT -> {
                    binding.ivIntroM.post {
                        binding.ivIntroM.animateLeft {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.I_UP
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.I_UP -> {
                    binding.ivIntroI.post {
                        binding.ivIntroI.animateUp {
                            val sendMsg = Message.obtain().apply {
                                obj = IntroAnim.M_DOWN
                            }
                            sendMessage(sendMsg)
                        }
                    }
                }

                IntroAnim.M_DOWN -> {
                    binding.ivIntroM.post {
                        binding.ivIntroM.animateDown {
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

        setObserver()
        startAnimation()
    }

    private fun setObserver() {

    }

    private fun startAnimation() {
        val sendMsg = Message.obtain().apply {
            obj = IntroAnim.I_DOWN
        }
        animHandler.sendMessage(sendMsg)
    }

    private fun View.animateDown(endListener: () -> Unit) {
        val height = measuredHeight.toFloat()
        animate().setDuration(DURATION)
            .translationYBy(height)
            .withEndAction(endListener)
            .start()
    }

    private fun View.animateUp(endListener: () -> Unit) {
        val height = measuredHeight.toFloat()
        animate().setDuration(DURATION)
            .translationYBy(-height)
            .withEndAction(endListener)
            .start()
    }

    private fun View.animateLeft(endListener: () -> Unit) {
        val width = measuredWidth.toFloat()
        animate().setDuration(DURATION)
            .translationXBy(-width)
            .withEndAction(endListener)
            .start()
    }

    private fun View.animateRight(endListener: () -> Unit) {
        val width = measuredWidth.toFloat()
        animate().setDuration(DURATION)
            .translationXBy(width)
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