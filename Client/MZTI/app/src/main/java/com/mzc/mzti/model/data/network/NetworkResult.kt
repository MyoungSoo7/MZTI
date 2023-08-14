package com.mzc.mzti.model.data.network

sealed class NetworkResult<out R> {
    /**
     * 네트워크 통신이 성공한 경우에 사용
     */
    data class Success<out T>(val data: T) : NetworkResult<T>()

    /**
     * 네트워크 통신이 실패한 경우에 사용
     */
    data class Fail(val msg: String) : NetworkResult<Nothing>()

    /**
     * 네트워크 통신 과정에서 에러가 발생한 경우에 사용
     */
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
}
