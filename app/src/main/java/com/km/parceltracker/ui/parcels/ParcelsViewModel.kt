package com.km.parceltracker.ui.parcels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.database.ParcelRepository
import com.km.parceltracker.model.Parcel

class ParcelsViewModel(application: Application) : BaseViewModel(application) {

    private val parcelRepository = ParcelRepository(application.applicationContext) { println("API ERROR: $it") }
    private val dbParcels = parcelRepository.getParcels()

    var parcels = MediatorLiveData<List<Parcel>>()
    val ascending = MutableLiveData<Boolean>().apply { value = true }
    val query = MutableLiveData<String>()

    init {
        parcels.addSource(dbParcels) {
            parcels.value = sortAndFilterParcels(it, query.value, ascending.value)
        }

        parcels.addSource(query) { query ->
            parcels.value = sortAndFilterParcels(
                dbParcels.value,
                query,
                ascending.value
            )
        }

        parcels.addSource(ascending) { ascending ->
            parcels.value = sortParcels(parcels.value, ascending)
        }
    }

    private fun sortAndFilterParcels(parcels: List<Parcel>?, query: String?, ascending: Boolean?): List<Parcel>? {
        return sortParcels(
            filterParcels(parcels, query),
            ascending
        )
    }

    private fun sortParcels(parcels: List<Parcel>?, ascending: Boolean?): List<Parcel>? {
        return if (parcels == null || ascending == null) parcels
        else when (ascending) {
            true -> parcels.sortedBy { parcel -> parcel.title }
            false -> parcels.sortedByDescending { parcel -> parcel.title }
        }
    }

    private fun filterParcels(parcels: List<Parcel>?, query: String?): List<Parcel>? {
        return if (parcels == null || query.isNullOrBlank()) parcels
        else parcels.filter { parcel ->
            parcel.title.toLowerCase().contains(query.toLowerCase())
        }
    }
}