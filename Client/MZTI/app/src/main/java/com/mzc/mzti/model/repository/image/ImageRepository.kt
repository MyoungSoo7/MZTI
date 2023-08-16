package com.mzc.mzti.model.repository.image

import android.content.Context
import android.net.Uri
import com.mzc.mzti.common.util.DLog
import com.mzc.mzti.common.util.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG: String = "ImageRepository"

class ImageRepository(
    private val context: Context
) {

    private val fileUtil: FileUtil = FileUtil(context)

    suspend fun copyImageToCacheDir(pImgUri: Uri): String? {
        return withContext(Dispatchers.IO) {
            val imgPath = fileUtil.copyFileToCacheFolder(pImgUri)
            if (imgPath != null) {
                DLog.d(TAG, "resize!")
                val newCacheImgPath = fileUtil.createResizeBitmap(imgPath, 2048, IntArray(2) { 0 })
                newCacheImgPath
            } else {
                imgPath
            }
        }
    }

}