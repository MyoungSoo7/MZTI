package com.mzc.mzti.common.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

object VibrateManager {

    private lateinit var mVibrator: Vibrator

    /**
     * Vibrator 객체 초기화 함수
     *
     * @param pContext  Context 객체
     */
    fun initVibrator(pContext: Context) {
        mVibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (pContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            pContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    /**
     * Vibrate 요청 함수
     *
     * @param pContext          Context 객체
     * @param pVibrationType    Vibration Type
     *
     * @see com.diningcode.android.common.util.DiningVibrator.VibrationType
     */
    fun requestVibrate(
        pContext: Context,
        pVibrationType: VibrationType
    ) {
        if (!this::mVibrator.isInitialized) {
            initVibrator(pContext)
        }

        when (pVibrationType) {
            VibrationType.TICK -> vibrateTick()
        }
    }

    private fun vibrateTick() {
        // API 29 이상
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mVibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        }
        // API 26 이상
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mVibrator.vibrate(VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        // 그 외의 경우
        else {
            mVibrator.vibrate(10)
        }
    }

    enum class VibrationType {
        TICK
    }

}