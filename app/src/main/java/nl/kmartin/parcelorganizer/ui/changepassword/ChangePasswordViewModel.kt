package nl.kmartin.parcelorganizer.ui.changepassword

import android.app.Application
import nl.kmartin.parcelorganizer.R
import nl.kmartin.parcelorganizer.api.error.ApiError
import nl.kmartin.parcelorganizer.base.BaseViewModel
import nl.kmartin.parcelorganizer.enums.ApiErrorEnum
import nl.kmartin.parcelorganizer.form.ChangePasswordForm
import nl.kmartin.parcelorganizer.repository.UserRepository
import nl.kmartin.parcelorganizer.util.SingleLiveEvent
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ChangePasswordViewModel(application: Application) : BaseViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)
    val changePasswordForm = ChangePasswordForm()
    val changePasswordSuccess = SingleLiveEvent<Unit>()

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
                        changePasswordSuccess.value = Unit
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
                apiError.details?.get("oldPassword")?.let {
                    changePasswordForm.currentPasswordError.value =
                        R.string.current_password_incorrect
                }
            }
            else -> {
            }
        }
    }
}