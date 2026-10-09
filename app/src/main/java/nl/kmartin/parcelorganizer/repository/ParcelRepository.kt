package nl.kmartin.parcelorganizer.repository

import android.content.Context
import nl.kmartin.parcelorganizer.api.ParcelTrackerApi
import nl.kmartin.parcelorganizer.api.request.RegisterParcelRequestBody
import nl.kmartin.parcelorganizer.api.request.UpdateParcelRequestBody
import nl.kmartin.parcelorganizer.enums.ParcelStatusEnum
import nl.kmartin.parcelorganizer.model.Parcel
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