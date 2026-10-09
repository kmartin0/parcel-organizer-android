package nl.kmartin.parcelorganizer.ui.forgotpassword

import android.app.Application
import nl.kmartin.parcelorganizer.base.BaseViewModel
import nl.kmartin.parcelorganizer.form.ForgotPasswordForm
import nl.kmartin.parcelorganizer.repository.UserRepository
import nl.kmartin.parcelorganizer.util.SingleLiveEvent
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ForgotPasswordViewModel(application: Application) : BaseViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)
    val forgotPasswordForm = ForgotPasswordForm()
    val passwordResetRequestSent = SingleLiveEvent<Unit>()

    fun sendResetRequest() {
        if (forgotPasswordForm.validateInput()) {
            userRepository.forgotPassword(forgotPasswordForm.email.value!!).observeOn(
                AndroidSchedulers.mainThread()
            )
                .subscribeOn(Schedulers.io())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        stopLoading()
                        passwordResetRequestSent.value = Unit
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