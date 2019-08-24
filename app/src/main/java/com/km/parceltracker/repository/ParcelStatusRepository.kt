package com.km.parceltracker.repository

import android.content.Context
import com.km.parceltracker.api.ParcelTrackerApi
import com.km.parceltracker.enums.ParcelStatusEnum
import com.km.parceltracker.model.ParcelStatus
import io.reactivex.Single

class ParcelStatusRepository(context: Context) {

    private val api = ParcelTrackerApi.createApi(context)

    fun getParcelStatusByParcelStatusEnum(parcelStatusEnum: ParcelStatusEnum): Single<ParcelStatus> {
        return api.getParcelStatusByStatus(parcelStatusEnum)
    }
}