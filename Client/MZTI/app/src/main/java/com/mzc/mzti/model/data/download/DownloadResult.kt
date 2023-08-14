package com.mzc.mzti.model.data.download

sealed class DownloadResult<out T> {
    /**
     * 다운로드를 성공한 경우에 사용
     */
    data class Success<out T>(val data: T) : DownloadResult<T>()

    /**
     * 다운로드에 실패한 경우에 사용
     */
    data class Fail(val msg: String) : DownloadResult<Nothing>()

    /**
     * 다운로드 과정에서 에러가 발생한 경우에 사용
     */
    data class Error(val exception: Exception) : DownloadResult<Nothing>()
}
