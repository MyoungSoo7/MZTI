package com.mzc.mzti.model.repository.download

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.mzc.mzti.common.util.FileUtil
import com.mzc.mzti.model.data.download.DownloadResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG: String = "DownloadRepository"

class DownloadRepository(
    private val context: Context
) {

    private val fileUtil = FileUtil(context)

    suspend fun saveBitmap(pBitmap: Bitmap): DownloadResult<Uri> {
        return withContext(Dispatchers.IO) {
            val imgUri = fileUtil.saveBitmapToPictureFolder(pBitmap)

            if (imgUri != null) {
                DownloadResult.Success(imgUri)
            } else {
                DownloadResult.Fail("이미지 저장 실패")
            }
        }
    }

}