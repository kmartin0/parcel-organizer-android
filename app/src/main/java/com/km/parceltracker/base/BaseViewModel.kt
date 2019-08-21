package com.km.parceltracker.base

import android.app.Application
import android.util.Log
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

    protected fun handleApiError(error: Throwable) {
        when (error) {
            is HttpException -> {
                val body = error.response()?.errorBody()?.string()
                val apiError = Gson().fromJson(body, ApiError::class.java)
                when (apiError.error) {
                    "invalid_token" -> logout.call() // Refresh Token Expired.
                }
            }
            is SocketTimeoutException -> noInternetConnection.call()
        }
    }

}
