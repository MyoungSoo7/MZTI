package com.mzc.mzti.common.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.exifinterface.media.ExifInterface
import java.io.EOFException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
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
        val strFilePath: String = strCachePath + "/" + getFileName() + ".png"
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
        val strFilePath: String = strCachePath + "/" + getFileName() + ".png"
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

    /**
     * 이미지 파일의 크기가 maxPixel * maxPixel 보다 큰 경우, 비트맵을 리사이징해서 photoPath에 저장함
     *
     * @param photoPath     리사이징한 비트맵을 저장할 경로
     * @param maxPixel      픽셀 최대값
     * @param originImgSize 원본 이미지의 크기를 저장하는 버퍼, {(0, width), (1, height)}
     * @return 리사이징이 필요하다면 리사이징된 비트맵, 그렇지 않다면 원본 이미지의 비트맵
     */
    fun createResizeBitmap(photoPath: String, maxPixel: Int, originImgSize: IntArray): String? {
        if (maxPixel > 0) {
            // Get the dimensions of the bitmap
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(
                photoPath,
                options
            ) // inJustDecodeBounds 설정을 해주지 않으면 이부분에서 큰 이미지가 들어올 경우 outofmemory Exception이 발생한다.
            var resizedBitmap: Bitmap? = null
            var photoW = options.outWidth.toFloat()
            var photoH = options.outHeight.toFloat()
            originImgSize[0] = photoW.toInt()
            originImgSize[1] = photoH.toInt()
            val photoPixel = if (photoW > photoH) photoW else photoH
            options.inJustDecodeBounds = false
            if (photoPixel > maxPixel) {
                val bitmap = BitmapFactory.decodeFile(photoPath, options)
                val ratio = photoPixel.toFloat() / maxPixel
                photoW /= ratio
                photoH /= ratio
                DLog.d(TAG, "resized Bitmap!, ratio=$ratio")
                resizedBitmap =
                    Bitmap.createScaledBitmap(bitmap, photoW.toInt(), photoH.toInt(), true)
            } else {
                resizedBitmap = BitmapFactory.decodeFile(photoPath, options)
            }
            DLog.d(
                TAG,
                "after resizing, bmpWidth=${resizedBitmap.width}, bmpHeight=${resizedBitmap.height}"
            )
            return try {
                var rotateBitmap: Bitmap? = null
                val strFileName = photoPath.substring(photoPath.lastIndexOf("/") + 1)
                var exifDegree = 0f
                try {
                    val exif = ExifInterface(photoPath)
                    val exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED
                    )
                    exifDegree = exifOrientationToDegrees(exifOrientation)
                    if (exifDegree != 0f && resizedBitmap != null) {
                        val m = Matrix()
                        m.setRotate(
                            exifDegree,
                            resizedBitmap.width.toFloat() / 2,
                            resizedBitmap.height.toFloat() / 2
                        )
                        var intRotateWidth = 0
                        var intRotateHeight = 0
                        if (exifDegree == 90f ||
                            exifDegree == 270f
                        ) {
                            intRotateWidth = resizedBitmap.height
                            intRotateHeight = resizedBitmap.width
                        } else {
                            intRotateWidth = resizedBitmap.width
                            intRotateHeight = resizedBitmap.height
                        }
                        try {
                            var converted = Bitmap.createBitmap(
                                resizedBitmap,
                                0,
                                0,
                                resizedBitmap.width,
                                resizedBitmap.height,
                                m,
                                true
                            )
                            converted = Bitmap.createScaledBitmap(
                                converted!!,
                                intRotateWidth,
                                intRotateHeight,
                                true
                            )
                            rotateBitmap = converted
                        } catch (ex: OutOfMemoryError) {
                            DLog.e(TAG, ex.stackTraceToString())
                            // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
                        }
                    } else rotateBitmap = resizedBitmap
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (rotateBitmap != null) {
                    val newCacheFilePath = "$strCachePath/${getFileName()}.png"
                    saveBitmapToCacheFolder(rotateBitmap, newCacheFilePath)
                    newCacheFilePath
                } else null
            } catch (e: Exception) {
                DLog.e(TAG, e.stackTraceToString())
                null
            }
        }
        return null
    }

    private fun exifOrientationToDegrees(exifOrientation: Int): Float {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90f
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180f
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270f
        }
        return 0f
    }

    /**
     * 비트맵 이미지를 캐시 메모리에 저장하는 함수
     *
     * @param bitmap      이미지 파일로 저장할 비트맵 객체
     * @param strFilePath 이미지 절대경로
     */
    private fun saveBitmapToCacheFolder(bitmap: Bitmap, strFilePath: String) {
        DLog.d(
            TAG,
            "strFilePath=$strFilePath, bmpWidth=${bitmap.width}, bmpHeight=${bitmap.height}"
        )
        val fileCacheItem = File(strFilePath)
        if (fileCacheItem.exists()) deleteFile(strFilePath)
        try {
            fileCacheItem.createNewFile()
            val out: OutputStream = FileOutputStream(fileCacheItem)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 파일 삭제
     */
    fun deleteFile(path: String) {
        val file = File("$strCachePath/$path")
        if (file.exists()) file.delete()
    }

}