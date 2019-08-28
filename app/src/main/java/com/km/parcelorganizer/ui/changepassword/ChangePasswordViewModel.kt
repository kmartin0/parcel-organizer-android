package com.km.parcelorganizer.ui.changepassword

import android.app.Application
import com.km.parcelorganizer.R
import com.km.parcelorganizer.api.error.ApiError
import com.km.parcelorganizer.base.BaseViewModel
import com.km.parcelorganizer.enums.ApiErrorEnum
import com.km.parcelorganizer.form.ChangePasswordForm
import com.km.parcelorganizer.repository.UserRepository
import com.km.parcelorganizer.util.SingleLiveEvent
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ChangePasswordViewModel(application: Application) : BaseViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)
    val changePasswordForm = ChangePasswordForm()
    val changePasswordSuccess = SingleLiveEvent<Any>()

    fun changePassword() {
        if (isLoading.value == false && changePasswordForm.validateInput()) {
            userRepository.changePassword(
                changePasswordForm.currentPassword.value!!,
                changePasswordForm.newPassword.value!!
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        stopLoading()
                        changePasswordSuccess.call()
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                        startLoading()
                    }

                    override fun onError(e: Throwable) {
                        stopLoading()
                        handleApiError(e) { apiError ->
                            apiError?.let {
                                handleUpdateUserApiError(
                                    it
                                )
                            }
                        }
                    }
                })
        }
    }

    private fun handleUpdateUserApiError(apiError: ApiError) {
        when (apiError.error) {
            ApiErrorEnum.PERMISSION_DENIED -> { // Check if filled in current password is correct.
                apiError.details?.forEach { targetError ->
                    if (targetError.target == "oldPassword") changePasswordForm.currentPasswordError.value =
                        R.string.current_password_incorrect
                }
            }
            else -> {}
        }
    }
}