package nl.kmartin.parcelorganizer.ui.login

import android.app.Application
import nl.kmartin.parcelorganizer.R
import nl.kmartin.parcelorganizer.base.BaseViewModel
import nl.kmartin.parcelorganizer.enums.ApiErrorEnum
import nl.kmartin.parcelorganizer.form.LoginForm
import nl.kmartin.parcelorganizer.model.User
import nl.kmartin.parcelorganizer.repository.UserRepository
import nl.kmartin.parcelorganizer.util.SingleLiveEvent
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val userRepository = UserRepository(application.applicationContext)
    val loginForm = LoginForm()
    val loginSuccess = SingleLiveEvent<Unit>()

    init {
        checkUserAlreadyLoggedIn()
    }

    private fun checkUserAlreadyLoggedIn() {
        if (userRepository.isUserLoggedIn()) {
            loginSuccess.value = Unit
        }
    }

    /**
     * Login the user using [userRepository] using [loginForm] input.
     */
    fun login() {
        if (isLoading.value == false && loginForm.validateInput()) {
            userRepository.loginUser(loginForm.email.value!!, loginForm.password.value!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleObserver<User> {
                    override fun onSuccess(t: User) {
                        loginSuccess.value = Unit
                        stopLoading()
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                        startLoading()
                    }

                    override fun onError(e: Throwable) {
                        stopLoading()
                        handleApiError(e) {
                            when (it?.error) {
                                ApiErrorEnum.invalid_grant -> {
                                    loginForm.emailError.value = R.string.error_login_credentials
                                    loginForm.passwordError.value = R.string.error_login_credentials
                                }
                                else -> {}
                            }
                        }
                    }
                })
        }
    }

}