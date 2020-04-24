package com.km.parcelorganizer.ui.updateparcel

import android.app.Application
import com.km.parcelorganizer.base.BaseViewModel
import com.km.parcelorganizer.form.ParcelForm
import com.km.parcelorganizer.model.Parcel
import com.km.parcelorganizer.repository.ParcelRepository
import com.km.parcelorganizer.util.SingleLiveEvent
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UpdateParcelViewModel(application: Application) : BaseViewModel(application) {
    private val parcelRepository = ParcelRepository(application.applicationContext)

    private lateinit var parcelToUpdate: Parcel
    val parcelForm = ParcelForm()
    val parcelUpdateSuccess = SingleLiveEvent<Unit>()

    /**
     * Populate the [parcelForm] using [parcel]
     */
    fun populateParcelForm(parcel: Parcel) {
        parcelToUpdate = parcel
        parcelForm.apply {
            title.value = parcel.title
            sender.value = parcel.sender
            courier.value = parcel.courier
            trackingUrl.value = parcel.trackingUrl
            additionalInformation.value = parcel.additionalInformation
            parcelStatusEnum.value = parcel.parcelStatus.status
        }
    }

    /**
     * If the input is valid then [parcelRepository] is used to update the [parcelToUpdate]
     */
    fun updateParcel() {
        if (parcelForm.validateInput()) {
            parcelForm.apply {
                parcelRepository.updateParcel(
                    parcelToUpdate.id,
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
                            parcelUpdateSuccess.call()
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