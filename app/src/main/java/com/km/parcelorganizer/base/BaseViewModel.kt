package com.km.parcelorganizer.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.km.parcelorganizer.api.error.ApiError
import com.km.parcelorganizer.repository.UserRepository
import com.km.parcelorganizer.util.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val disposables = CompositeDisposable()
    val isLoading = MutableLiveData<Boolean>().apply { value = false }
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
                    ApiError.TOKEN_EXPIRED -> logout.call()  // Refresh Token Expired or Bad Credentials.
                    ApiError.INVALID_GRANT -> if (userRepository.isUserLoggedIn()) logout.call()
                    ApiError.INTERNAL_SERVER_ERROR -> internalServerError.call()
                }

                // Return the api error object.
                apiError
            }
            is SocketTimeoutException, is ConnectException -> { // Server can't be reached.
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

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}
