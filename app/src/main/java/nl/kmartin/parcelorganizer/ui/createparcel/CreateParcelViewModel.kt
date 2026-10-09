package nl.kmartin.parcelorganizer.ui.createparcel

import android.app.Application
import nl.kmartin.parcelorganizer.base.BaseViewModel
import nl.kmartin.parcelorganizer.form.ParcelForm
import nl.kmartin.parcelorganizer.model.Parcel
import nl.kmartin.parcelorganizer.repository.ParcelRepository
import nl.kmartin.parcelorganizer.util.SingleLiveEvent
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CreateParcelViewModel(application: Application) : BaseViewModel(application) {
    private val parcelRepository = ParcelRepository(application.applicationContext)

    val parcelForm = ParcelForm()
    val parcelCreatedSuccess = SingleLiveEvent<Unit>()

    fun setTrackingUrl(url: String?) {
        parcelForm.trackingUrl.value = url
        parcelForm.validateTrackingUrl()
    }

    /**
     * If the input is correct then create a Parcel object and store it in the [parcelRepository]
     */
    fun createParcel() {
        if (isLoading.value == false && parcelForm.validateInput()) {
            parcelForm.apply {
                parcelRepository.saveParcel(
                    title.value!!,
                    sender.value,
                    courier.value,
                    trackingUrl.value,
                    additionalInformation.value,
                    parcelStatusEnum.value!!
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : SingleObserver<Parcel> {
                        override fun onSuccess(t: Parcel) {
                            stopLoading()
                            parcelCreatedSuccess.value = Unit
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

}