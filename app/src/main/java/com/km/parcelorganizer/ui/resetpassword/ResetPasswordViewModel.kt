package com.km.parcelorganizer.ui.resetpassword

import android.app.Application
import com.km.parcelorganizer.base.BaseViewModel
import com.km.parcelorganizer.enums.ApiErrorEnum
import com.km.parcelorganizer.form.ResetPasswordForm
import com.km.parcelorganizer.repository.UserRepository
import com.km.parcelorganizer.util.SingleLiveEvent
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ResetPasswordViewModel(application: Application) : BaseViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)

    val resetPasswordForm = ResetPasswordForm()

    val success = SingleLiveEvent<Unit>()
    val error = SingleLiveEvent<Unit>()

    var token: String? = null


    fun resetPassword() {

        token.let { token ->
            if (token.isNullOrEmpty()) {
                error.call()
                return
            }

            if (resetPasswordForm.validateInput()) {
                userRepository.resetPassword(resetPasswordForm.password.value!!, token)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : CompletableObserver {
                        override fun onComplete() {
                            stopLoading()
                            success.call()
                            resetPasswordForm.resetForm()
                        }

                        override fun onSubscribe(d: Disposable) {
                            disposables.add(d)
                            startLoading()
                        }

                        override fun onError(e: Throwable) {
                            stopLoading()
                            handleApiError(e) {
                                when (it?.error) {
                                    ApiErrorEnum.PERMISSION_DENIED -> error.call()
                                    else -> {}
                                }
                            }
                        }

                    })
            }
        }


    }
}