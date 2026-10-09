package nl.kmartin.parcelorganizer.ui.register

import android.app.Application
import nl.kmartin.parcelorganizer.base.BaseViewModel
import nl.kmartin.parcelorganizer.enums.ApiErrorEnum
import nl.kmartin.parcelorganizer.form.RegisterForm
import nl.kmartin.parcelorganizer.model.User
import nl.kmartin.parcelorganizer.repository.UserRepository
import nl.kmartin.parcelorganizer.util.SingleLiveEvent
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RegisterViewModel(application: Application) : BaseViewModel(application) {
    private val userRepository = UserRepository(application.applicationContext)
    val registerForm = RegisterForm()
    val registerSuccess = SingleLiveEvent<Unit>()
    val alreadyExists = SingleLiveEvent<Unit>()

    fun register() {
        if (isLoading.value == false && registerForm.validateInput()) {
            userRepository.registerUser(
                registerForm.email.value!!,
                registerForm.name.value!!,
                registerForm.password.value!!
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleObserver<User> {
                    override fun onSuccess(t: User) {
                        stopLoading()
                        registerSuccess.value = Unit
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                        startLoading()
                    }

                    override fun onError(e: Throwable) {
                        stopLoading()
                        handleApiError(e) {
                            when (it?.error) {
                                ApiErrorEnum.ALREADY_EXISTS -> alreadyExists.value = Unit
                                else -> {}
                            }
                        }
                    }

                })
        }
    }
}