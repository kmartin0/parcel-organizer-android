package com.km.parceltracker.repository

import android.content.Context
import com.km.parceltracker.api.ParcelTrackerApi
import com.km.parceltracker.api.request.RegisterParcelRequestBody
import com.km.parceltracker.api.request.UpdateParcelRequestBody
import com.km.parceltracker.enums.ParcelStatusEnum
import com.km.parceltracker.model.Parcel
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
        parcelStatusEnum: ParcelStatusEnum
    ): Single<Parcel> {
        return parcelStatusRepository.getParcelStatusByParcelStatusEnum(parcelStatusEnum).flatMap { parcelStatus ->
            api.saveParcel(RegisterParcelRequestBody(title, sender, courier, trackingUrl, parcelStatus))
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
        parcelStatusEnum: ParcelStatusEnum
    ): Single<Parcel> {
        return parcelStatusRepository.getParcelStatusByParcelStatusEnum(parcelStatusEnum).flatMap { parcelStatus ->
            api.updateParcel(UpdateParcelRequestBody(id, title, sender, courier, trackingUrl, parcelStatus))
        }
    }

    fun deleteParcel(id: Long): Completable {
        return api.deleteParcel(id)
    }

}