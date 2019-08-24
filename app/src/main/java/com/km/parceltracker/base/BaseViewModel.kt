package com.km.parceltracker.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.km.parceltracker.api.ApiError
import com.km.parceltracker.repository.UserRepository
import com.km.parceltracker.util.SingleLiveEvent
import retrofit2.HttpException
import java.net.SocketTimeoutException

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val isLoading = MutableLiveData<Boolean>()
    val noInternetConnection = SingleLiveEvent<Unit>()
    val logout = SingleLiveEvent<Unit>()
    val internalServerError = SingleLiveEvent<Unit>()
    private val userRepository = UserRepository(application.applicationContext)

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
    protected fun handleApiError(error: Throwable, apiErrorHandler: ((ApiError?) -> Unit)? = null): ApiError? {
        return when (error) {
            is HttpException -> {
                // Parse the HttpException into an ApiError object.
                val apiError = parseApiError(error)

                // If an error handler is supplied use it to handle the api error.
                if (apiErrorHandler != null) {
                    apiErrorHandler(apiError)
                }

                // Handle these errors globally.
                when (apiError?.error) {
                    ApiError.TOKEN_EXPIRED, ApiError.INVALID_GRANT -> logout.call()  // Refresh Token Expired or Bad Credentials.
                    ApiError.INTERNAL_SERVER_ERROR -> internalServerError.call()
                }

                // Return the api error object.
                apiError
            }
            is SocketTimeoutException -> { // Server can't be reached.
                noInternetConnection.call()
                null
            }
            else -> null
        }
    }

    fun parseApiError(error: Throwable): ApiError? {
        return when (error) {
            is HttpException -> {
                val body = error.response()?.errorBody()?.string()
                Gson().fromJson(body, ApiError::class.java)
            }
            else -> null
        }
    }

    fun logout() {
        userRepository.logoutUser()
    }

}
