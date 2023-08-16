package com.mzc.mzti.common.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import java.io.EOFException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar

private const val TAG: String = "FileUtil"

class FileUtil(
    private val context: Context
) {

    private val strCachePath: String = context.cacheDir.absolutePath

    @SuppressLint("SimpleDateFormat")
    private fun getStrDate(): String =
        SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().time)

    private fun getFileName(): String = "mzti_${getStrDate()}"

    private fun getAlbumDir(): File? {
        var storageDir: File? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            storageDir = getAlbumStorageDir("MZTI")
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        return null
                    }
                }
            }
        } else {
            DLog.d(javaClass.simpleName, "External storage is not mounted READ/WRITE.")
        }
        return storageDir
    }

    /**
     * 폴더 생성 한다.
     */
    private fun getAlbumStorageDir(albumName: String): File {
        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            albumName
        )
    }

    /**
     * 매개변수로 넘겨받은 비트맵을 외부 저장소 공용 공간 Pictures/MZTI 디렉토리에 이미지 파일로 저장하는 함수
     *
     * @param bitmap 이미지 파일로 저장할 비트맵 객체
     * @return 저장한 이미지 파일의 uri
     */
    fun saveBitmapToPictureFolder(bitmap: Bitmap): Uri? {
        val fileName: String = getFileName()
        val uri: Uri?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = saveImageOnAboveAndroidQ(bitmap, fileName)
        } else {
            uri = saveImageOnUnderAndroidQ(bitmap, fileName)
        }
        return uri
    }

    /**
     * Android 10(Q) 이상인 기기에서 비트맵을 외부 저장소 공용 공간 Pictures/MZTI 디렉토리에 이미지 파일로 저장하는 함수
     *
     * @param bitmap   이미지 파일로 저장할 비트맵 객체
     * @param fileName 이미지 파일명
     * @return 저장한 이미지 파일의 uri
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageOnAboveAndroidQ(bitmap: Bitmap, fileName: String): Uri? {
        val contentValues = ContentValues()
        contentValues.put(
            MediaStore.Images.Media.RELATIVE_PATH,
            Environment.DIRECTORY_PICTURES + "/MZTI"
        )
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 1)
        val contentResolver: ContentResolver = context.getContentResolver()
        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        try {
            if (uri == null) {
                throw IOException("Failed to create new MediaStore record")
            }
            val image = contentResolver.openFileDescriptor(uri, "w", null)
            if (image != null) {
                val fos = FileOutputStream(image.fileDescriptor)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.close()
                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            }
        } catch (e: IOException) {
            DLog.e("${TAG}_saveImageOnAboveAndroidQ", e.toString())
            if (uri != null) {
                contentResolver.delete(uri, null, null)
            }
            return null
        }
        return uri
    }

    /**
     * Android 10(Q) 미만인 기기에서 비트맵을 외부 저장소 공용 공간 Pictures/MZTI 디렉토리에 이미지 파일로 저장하는 함수
     *
     * @param bitmap   이미지 파일로 저장할 비트맵 객체
     * @param fileName 이미지 파일명
     * @return 저장한 이미지 파일의 uri
     */
    private fun saveImageOnUnderAndroidQ(bitmap: Bitmap, fileName: String): Uri? {
        val albumF: File? = getAlbumDir()
        var uri: Uri? = null

        try {
            val fileItem = File("$albumF/$fileName.png")
            fileItem.createNewFile()
            val fos = FileOutputStream(fileItem)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()
            uri = Uri.fromFile(fileItem)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
        } catch (e: IOException) {
            DLog.e("${TAG}_saveImageOnUnderAndroidQ", e.toString())
        }

        return uri
    }

    /**
     * File을 임시폴더로 Copy 한다.
     */
    @Throws(IOException::class)
    fun copyFileToCacheFolder(strOriginPath: String?): String? {
        val strOriginFileName = strOriginPath!!.substring(strOriginPath.lastIndexOf("/") + 1)
        val strFilePath: String = strCachePath + "/" + getFileName()
        val fileOrigin = File(strOriginPath)
        val fileCopy = File(strFilePath)
        val inOrigin = FileInputStream(fileOrigin)
        val buf = ByteArray(inOrigin.available())
        var idx = 0
        while (idx < buf.size) {
            val read = inOrigin.read(buf, idx, buf.size - idx)
            if (read < 0) throw EOFException("File COPY ERROR!!")
            idx += read
        }
        inOrigin.close()
        val outCopy = FileOutputStream(fileCopy)
        outCopy.write(buf)
        outCopy.close()
        return fileCopy.absolutePath
    }

    /**
     * 이미지 파일을 캐시로 복사하는 함수
     *
     * @param uri      캐시로 복사할 이미지의 Content Uri
     *
     * @return 캐시로 복사한 이미지 파일의 절대 경로를 반환함
     */
    @Throws(IOException::class)
    fun copyFileToCacheFolder(uri: Uri): String? {
        if (uri.scheme != "content") return copyFileToCacheFolder(uri.path)
        val strFilePath: String = strCachePath + "/" + getFileName()
        val fileCopy = File(strFilePath)
        val inOrigin: InputStream? = context.contentResolver.openInputStream(uri)
        if (inOrigin != null) {
            val buf = ByteArray(inOrigin.available())
            var idx = 0
            while (idx < buf.size) {
                val read = inOrigin.read(buf, idx, buf.size - idx)
                if (read < 0) throw EOFException("File COPY ERROR!!")
                idx += read
            }
            inOrigin.close()
            val outCopy = FileOutputStream(fileCopy)
            outCopy.write(buf)
            outCopy.close()
        }
        return fileCopy.absolutePath
    }

}