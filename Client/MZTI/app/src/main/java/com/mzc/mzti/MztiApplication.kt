package com.mzc.mzti

import android.app.Application
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.common.util.VibrateManager

class MztiApplication : Application() {

    override fun onCreate() {
        DLog.d(TAG, "onCreate() is called")
        super.onCreate()
        VibrateManager.initVibrator(this)
    }

    companion object {
        private const val TAG: String = "MztiApplication"
    }

}