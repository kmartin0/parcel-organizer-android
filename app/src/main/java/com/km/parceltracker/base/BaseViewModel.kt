package com.km.parceltracker.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.km.parceltracker.api.ApiError
import com.km.parceltracker.util.SingleLiveEvent
import retrofit2.HttpException
import java.net.SocketTimeoutException

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val isLoading = MutableLiveData<Boolean>()
    val noInternetConnection = SingleLiveEvent<Any>()
    val logout = SingleLiveEvent<Any>()

    protected fun startLoading() {
        isLoading.value = true
    }

    protected fun stopLoading() {
        isLoading.value = false
    }

    /**
     * TODO: Look into moving this code someplace else.
     * Handles an [Throwable] thrown by retrofit.
     *
     * @return [ApiError?] If the error is not handled and is an instance of [ApiError] then
     * the [ApiError] is returned. If the error was consumed then null is returned.
     */
    protected fun handleGlobalApiError(error: Throwable) : ApiError? {
        return when (error) {
            is HttpException -> {
                val apiError = parseApiError(error)
                return when (apiError?.error) {
                    "invalid_token" -> { // Refresh Token Expired.
                        logout.call()
                        null
                    }
                    else -> apiError
                }
            }
            is SocketTimeoutException -> {
                noInternetConnection.call()
                null
            }
            else -> null
        }
    }

    private fun parseApiError(error: Throwable): ApiError? {
        return when (error) {
            is HttpException -> {
                val body = error.response()?.errorBody()?.string()
                Gson().fromJson(body, ApiError::class.java)
            }
            else -> null
        }
    }

}
