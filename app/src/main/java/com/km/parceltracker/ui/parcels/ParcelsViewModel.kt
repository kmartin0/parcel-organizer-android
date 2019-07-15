package com.km.parceltracker.ui.parcels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.database.ParcelRepository
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.model.ParcelStatus
import java.util.*
import kotlin.collections.ArrayList

class ParcelsViewModel(application: Application) : BaseViewModel(application) {

    private val parcelRepository = ParcelRepository(application.applicationContext) { println("API ERROR: $it") }
    private val dbParcels = parcelRepository.getParcels()

    var parcels = MediatorLiveData<List<Parcel>>()
    val sortBy = MutableLiveData<ParcelSortingEnum>().apply { value = ParcelSortingEnum.TITLE }
    val ascending = MutableLiveData<Boolean>().apply { value = true }
    val query = MutableLiveData<String>()

    init {
        parcels.addSource(dbParcels) {
            parcels.value = sortAndFilterParcels(it, query.value, ascending.value, sortBy.value)
        }

        parcels.addSource(query) { query ->
            parcels.value = sortAndFilterParcels(
                dbParcels.value,
                query,
                ascending.value,
                sortBy.value
            )
        }

        parcels.addSource(ascending) { ascending ->
            parcels.value = sortParcels(parcels.value, ascending, sortBy.value)
        }

        parcels.addSource(sortBy) {
            parcels.value = sortParcels(parcels.value, ascending.value, it)
        }
    }

    private fun sortAndFilterParcels(
        parcels: List<Parcel>?,
        query: String?,
        ascending: Boolean?,
        sortBy: ParcelSortingEnum?
    ): List<Parcel>? {
        return sortParcels(
            filterParcels(parcels, query),
            ascending,
            sortBy
        )
    }

    private fun sortParcels(parcels: List<Parcel>?, ascending: Boolean?, sortBy: ParcelSortingEnum?): List<Parcel>? {
        if (parcels == null || ascending == null || sortBy == null) return parcels

        return when (sortBy) {
            ParcelSortingEnum.TITLE -> {
                if (ascending) parcels.sortedBy { it.title }
                else parcels.sortedByDescending { it.title }
            }
            ParcelSortingEnum.SENDER -> {
                if (ascending) parcels.sortedBy { it.sender }
                else parcels.sortedByDescending { it.sender }
            }
            ParcelSortingEnum.COURIER -> {
                if (ascending) parcels.sortedBy { it.courier }
                else parcels.sortedByDescending { it.courier }
            }
            ParcelSortingEnum.DATE -> {
                if (ascending) parcels.sortedBy { it.lastUpdated }
                else parcels.sortedByDescending { it.lastUpdated }
            }
            ParcelSortingEnum.STATUS -> {
                if (ascending) parcels.sortedBy { it.parcelStatus.status }
                else parcels.sortedByDescending { it.parcelStatus.status }
            }
        }
    }

    private fun filterParcels(parcels: List<Parcel>?, query: String?): List<Parcel>? {
        return if (parcels == null || query.isNullOrBlank()) parcels
        else parcels.filter { parcel ->
            parcel.title.toLowerCase().contains(query.toLowerCase())
        }
    }
}