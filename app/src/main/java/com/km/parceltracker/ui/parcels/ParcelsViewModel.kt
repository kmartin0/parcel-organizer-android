package com.km.parceltracker.ui.parcels

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.database.ParcelRepository
import com.km.parceltracker.enums.ParcelSearchingEnum
import com.km.parceltracker.enums.ParcelSortingEnum
import com.km.parceltracker.model.Parcel

class ParcelsViewModel(application: Application) : BaseViewModel(application) {

    private val parcelRepository = ParcelRepository(application.applicationContext) { println("API ERROR: $it") }
    private val dbParcels = parcelRepository.getParcels()

    var parcels = MediatorLiveData<List<Parcel>>()
    val sortBy = MutableLiveData<ParcelSortingEnum>().apply { value = ParcelSortingEnum.TITLE }
    val ascending = MutableLiveData<Boolean>().apply { value = true }
    val searchQuery = MutableLiveData<String>()
    val searchBy = MutableLiveData<ParcelSearchingEnum>().apply { value = ParcelSearchingEnum.TITLE }

    init {
        parcels.addSource(dbParcels) {
            println("DBPARCELS")
            parcels.value = sortAndFilterParcels(it, searchQuery.value, searchBy.value, ascending.value, sortBy.value)
        }

        parcels.addSource(searchQuery) { query ->
            println("SEARCHQUERY")
            parcels.value = sortAndFilterParcels(
                dbParcels.value,
                query,
                searchBy.value,
                ascending.value,
                sortBy.value
            )
        }

        parcels.addSource(searchBy) {
            println("SEARCHBY")
            parcels.value = sortAndFilterParcels(
                dbParcels.value,
                searchQuery.value,
                it,
                ascending.value,
                sortBy.value
            )
        }

        parcels.addSource(ascending) { ascending ->
            println("ASCENDING")
            parcels.value = sortParcels(parcels.value, ascending, sortBy.value)
        }

        parcels.addSource(sortBy) {
            println("SORTBY")
            parcels.value = sortParcels(parcels.value, ascending.value, it)
        }


    }

    private fun sortAndFilterParcels(
        parcels: List<Parcel>?,
        query: String?,
        filterBy: ParcelSearchingEnum?,
        ascending: Boolean?,
        sortBy: ParcelSortingEnum?
    ): List<Parcel>? {
        return sortParcels(
            filterParcels(parcels, query, filterBy),
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

    private fun filterParcels(parcels: List<Parcel>?, query: String?, filterBy: ParcelSearchingEnum?): List<Parcel>? {
        return if (parcels == null || query.isNullOrBlank() || filterBy == null) parcels
        else parcels.filter { parcel ->
            when (filterBy) {
                ParcelSearchingEnum.TITLE -> {
                    parcel.title.toLowerCase().contains(query.toLowerCase())
                }
                ParcelSearchingEnum.SENDER -> {
                    if (parcel.sender.isNullOrBlank()) false
                    else parcel.sender.toLowerCase().contains(query.toLowerCase())
                }
                ParcelSearchingEnum.COURIER -> {
                    if (parcel.sender.isNullOrBlank()) false
                    else parcel.sender.toLowerCase().contains(query.toLowerCase())
                }
            }
        }
    }
}