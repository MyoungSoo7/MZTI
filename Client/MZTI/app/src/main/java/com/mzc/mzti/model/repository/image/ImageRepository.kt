package com.mzc.mzti.model.repository.image

import android.content.Context
import android.net.Uri
import com.mzc.mzti.common.util.FileUtil

class ImageRepository(
    private val context: Context
) {

    private val fileUtil: FileUtil = FileUtil(context)

    fun copyImageToCacheDir(pImgUri: Uri): String? {
        val imgPath = fileUtil.copyFileToCacheFolder(pImgUri)
        return imgPath
    }

}