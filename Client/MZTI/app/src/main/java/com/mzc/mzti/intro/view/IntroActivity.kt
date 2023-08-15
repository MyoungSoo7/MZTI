package com.mzc.mzti.intro.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mzc.mzti.base.BaseActivity
import com.mzc.mzti.base.BaseViewModel
import com.mzc.mzti.common.session.MztiSession
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.databinding.ActivityIntroBinding
import com.mzc.mzti.intro.viewmodel.IntroViewModel
import com.mzc.mzti.main.view.MainActivity
import com.mzc.mzti.model.data.network.NetworkResult
import com.mzc.mzti.model.data.user.UserInfoData
import com.mzc.mzti.sign.view.SignActivity

private const val TAG: String = "IntroActivity"

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
                            model.plusAnimationCycle()
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
        init()
    }

    private fun setObserver() {
        model.userInfoData.observe(this, Observer { result ->
            when (result) {
                is NetworkResult.Success<UserInfoData> -> {
                    val userInfoData = result.data
                    MztiSession.login(
                        pUserId = userInfoData.id,
                        pGenerateType = userInfoData.generateType,
                        pUserToken = userInfoData.token,
                        pUserNickname = userInfoData.nickname,
                        pUserMBTI = userInfoData.mbti,
                        pUserProfileImg = userInfoData.profileImg
                    )
                    requestRequiredPermission()
                }

                is NetworkResult.Fail,
                is NetworkResult.Error -> {
                    MztiSession.logout()
                    requestRequiredPermission()
                }

                else -> {
                }
            }
        })

        model.isAllRequiredPermissionGranted.observe(this, Observer { isGranted ->
            if (isGranted) {
                if (MztiSession.isLogin) {
                    val mainIntent = Intent(this@IntroActivity, MainActivity::class.java)
                    startActivity(mainIntent)
                    finish()
                } else {
                    val signIntent = Intent(this@IntroActivity, SignActivity::class.java)
                    startActivity(signIntent)
                    finish()
                }
            }
        })
    }

    private fun init() {
        startAnimation()

        DLog.d(TAG, "isLogin=${MztiSession.isLogin}")
        if (MztiSession.isLogin) {
            val token = MztiSession.userToken
            val generateType = MztiSession.generateType
            DLog.d(TAG, "token=$token, generateType=$generateType")
            model.requestUserInfo(token, generateType)
        } else {
            requestRequiredPermission()
        }
    }

    private fun startAnimation() {
        val sendMsg = Message.obtain().apply {
            obj = IntroAnim.I_DOWN
        }
        animHandler.sendMessage(sendMsg)
    }

    // region 권한 요청 관리
    private fun requestRequiredPermission() {
        // 저장공간 권한이 허용되지 않은 경우
        if (!checkStoragePermission()) {
        }
        // 카메라 권한이 허용되지 않은 경우
        else if (!checkCameraPermission()) {
        }
    }

    /**
     * 저장공간 권한이 있는지 여부를 확인하는 함수
     *
     * 만약 저장공간 권한이 없다면, 사용자에게 저장공간 권한을 요청함
     *
     * @return 저장공간 권한이 있다면 true, 없다면 false
     */
    private fun checkStoragePermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionReadResult =
                checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)

            return if (permissionReadResult == PackageManager.PERMISSION_GRANTED) {
                model.setStoragePermissionState(true)
                true
            } else {
                model.setStoragePermissionState(false)
                requestPermissions(
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    PERMISSION_READ_MEDIA
                )
                false
            }
        } else {
            val permissionReadResult =
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            val permissionWriteResult =
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

            return if (permissionReadResult == PackageManager.PERMISSION_GRANTED
                && permissionWriteResult == PackageManager.PERMISSION_GRANTED
            ) {
                model.setStoragePermissionState(true)
                true
            } else {
                model.setStoragePermissionState(false)
                requestPermissions(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    PERMISSION_STORAGE
                )
                false
            }
        }
    }

    /**
     * 카메라 권한이 있는지 여부를 확인하는 함수
     *
     * 만약 카메라 권한이 없다면, 사용자에게 카메라 권한을 요청함
     *
     * @return 카메라 권한이 있다면 true, 없다면 false
     */
    private fun checkCameraPermission(): Boolean {
        val permissionResult = checkSelfPermission(Manifest.permission.CAMERA)

        return if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            model.setCameraPermissionState(true)
            true
        } else {
            model.setCameraPermissionState(false)
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_CAMERA
            )
            false
        }
    }
    // endregion 권한 요청 관리

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
        private const val PERMISSION_STORAGE: Int = 100
        private const val PERMISSION_READ_MEDIA: Int = 101
        private const val PERMISSION_CAMERA: Int = 102

        private const val DURATION: Long = 300
    }

    private enum class IntroAnim {
        I_DOWN, M_UP, T_RIGHT, M_RIGHT, T_LEFT, M_LEFT, I_UP, M_DOWN
    }

}