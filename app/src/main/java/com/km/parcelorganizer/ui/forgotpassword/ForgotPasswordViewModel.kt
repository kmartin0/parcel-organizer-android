package com.km.parcelorganizer.ui.forgotpassword

import android.app.Application
import com.km.parcelorganizer.base.BaseViewModel
import com.km.parcelorganizer.form.ForgotPasswordForm
import com.km.parcelorganizer.repository.UserRepository
import com.km.parcelorganizer.util.SingleLiveEvent
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ForgotPasswordViewModel(application: Application) : BaseViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext);
    val forgotPasswordForm = ForgotPasswordForm()
    val passwordResetRequestSent = SingleLiveEvent<Any>()

    fun sendResetRequest() {
        if (forgotPasswordForm.validateInput()) {
            userRepository.forgotPassword(forgotPasswordForm.email.value!!).observeOn(
                AndroidSchedulers.mainThread()
            )
                .subscribeOn(Schedulers.io())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        stopLoading()
                        passwordResetRequestSent.call();
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                        startLoading()
                    }

                    override fun onError(e: Throwable) {
                        stopLoading()
                        handleApiError(e)
                    }

                })
        }
    }

}