package com.km.parcelorganizer.repository

import android.content.Context
import com.km.parcelorganizer.api.ParcelTrackerApi
import com.km.parcelorganizer.api.request.RegisterParcelRequestBody
import com.km.parcelorganizer.api.request.UpdateParcelRequestBody
import com.km.parcelorganizer.enums.ParcelStatusEnum
import com.km.parcelorganizer.model.Parcel
import io.reactivex.Completable
import io.reactivex.Single

class ParcelRepository(context: Context) {

    private val parcelStatusRepository = ParcelStatusRepository(context)
    private val api = ParcelTrackerApi.createApi(context)

    fun saveParcel(
        title: String,
        sender: String?,
        courier: String?,
        trackingUrl: String?,
        additionalInformation: String?,
        parcelStatusEnum: ParcelStatusEnum
    ): Single<Parcel> {
        return parcelStatusRepository.getParcelStatusByParcelStatusEnum(parcelStatusEnum)
            .flatMap { parcelStatus ->
                api.saveParcel(
                    RegisterParcelRequestBody(
                        title,
                        sender,
                        courier,
                        trackingUrl,
                        additionalInformation,
                        parcelStatus
                    )
                )
            }
    }

    fun getParcels(): Single<List<Parcel>> {
        return api.getParcels()
    }

    fun updateParcel(
        id: Long,
        title: String,
        sender: String?,
        courier: String?,
        trackingUrl: String?,
        additionalInformation: String?,
        parcelStatusEnum: ParcelStatusEnum
    ): Single<Parcel> {
        return parcelStatusRepository.getParcelStatusByParcelStatusEnum(parcelStatusEnum)
            .flatMap { parcelStatus ->
                api.updateParcel(
                    UpdateParcelRequestBody(
                        id,
                        title,
                        sender,
                        courier,
                        trackingUrl,
                        additionalInformation,
                        parcelStatus
                    )
                )
            }
    }

    fun deleteParcel(id: Long): Completable {
        return api.deleteParcel(id)
    }

}