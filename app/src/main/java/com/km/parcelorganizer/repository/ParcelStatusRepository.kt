package com.km.parcelorganizer.repository

import android.content.Context
import com.km.parcelorganizer.api.ParcelTrackerApi
import com.km.parcelorganizer.enums.ParcelStatusEnum
import com.km.parcelorganizer.model.ParcelStatus
import io.reactivex.Single

class ParcelStatusRepository(context: Context) {

    private val api = ParcelTrackerApi.createApi(context)

    fun getParcelStatusByParcelStatusEnum(parcelStatusEnum: ParcelStatusEnum): Single<ParcelStatus> {
        return api.getParcelStatusByStatus(parcelStatusEnum)
    }
}