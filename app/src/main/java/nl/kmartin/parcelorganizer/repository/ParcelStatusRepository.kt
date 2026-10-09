package nl.kmartin.parcelorganizer.repository

import android.content.Context
import nl.kmartin.parcelorganizer.api.ParcelTrackerApi
import nl.kmartin.parcelorganizer.enums.ParcelStatusEnum
import nl.kmartin.parcelorganizer.model.ParcelStatus
import io.reactivex.Single

class ParcelStatusRepository(context: Context) {

    private val api = ParcelTrackerApi.createApi(context)

    fun getParcelStatusByParcelStatusEnum(parcelStatusEnum: ParcelStatusEnum): Single<ParcelStatus> {
        return api.getParcelStatusByStatus(parcelStatusEnum)
    }
}