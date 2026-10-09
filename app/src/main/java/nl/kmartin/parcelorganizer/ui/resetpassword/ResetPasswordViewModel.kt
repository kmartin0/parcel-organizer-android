package nl.kmartin.parcelorganizer.ui.resetpassword

import android.app.Application
import nl.kmartin.parcelorganizer.base.BaseViewModel
import nl.kmartin.parcelorganizer.enums.ApiErrorEnum
import nl.kmartin.parcelorganizer.form.ResetPasswordForm
import nl.kmartin.parcelorganizer.repository.UserRepository
import nl.kmartin.parcelorganizer.util.SingleLiveEvent
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
                error.value = Unit
                return
            }

            if (resetPasswordForm.validateInput()) {
                userRepository.resetPassword(resetPasswordForm.password.value!!, token)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : CompletableObserver {
                        override fun onComplete() {
                            stopLoading()
                            success.value = Unit
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
                                    ApiErrorEnum.PERMISSION_DENIED -> error.value = Unit
                                    else -> {}
                                }
                            }
                        }

                    })
            }
        }


    }
}