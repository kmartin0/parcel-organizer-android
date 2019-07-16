package com.km.parceltracker.ui.parcels

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.database.ParcelRepository
import com.km.parceltracker.enums.ParcelSearchingEnum
import com.km.parceltracker.enums.ParcelSortingEnum
import com.km.parceltracker.enums.SortOrderEnum
import com.km.parceltracker.model.Parcel

class ParcelsViewModel(application: Application) : BaseViewModel(application) {

    private val parcelRepository = ParcelRepository(application.applicationContext) { println("API ERROR: $it") }
    private val dbParcels = parcelRepository.getParcels()

    var parcels = MediatorLiveData<List<Parcel>>()

    val sortBy = MutableLiveData<ParcelSortingEnum>().apply { value = ParcelSortingEnum.TITLE }
    val sortOrder = MutableLiveData<SortOrderEnum>().apply { value = SortOrderEnum.ASCENDING }

    val searchQuery = MutableLiveData<String>()
    val searchBy = MutableLiveData<ParcelSearchingEnum>().apply { value = ParcelSearchingEnum.TITLE }

    init {
        parcels.addSource(dbParcels) {
            parcels.value = sortAndFilterParcels(it, searchQuery.value, searchBy.value, sortOrder.value, sortBy.value)
        }

        parcels.addSource(searchQuery) { query ->
            parcels.value = sortAndFilterParcels(
                dbParcels.value,
                query,
                searchBy.value,
                sortOrder.value,
                sortBy.value
            )
        }

        parcels.addSource(searchBy) {
            parcels.value = sortAndFilterParcels(
                dbParcels.value,
                searchQuery.value,
                it,
                sortOrder.value,
                sortBy.value
            )
        }

        parcels.addSource(sortOrder) { ascending ->
            parcels.value = sortParcels(parcels.value, ascending, sortBy.value)
        }

        parcels.addSource(sortBy) {
            parcels.value = sortParcels(parcels.value, sortOrder.value, it)
        }
    }

    private fun sortAndFilterParcels(
        parcels: List<Parcel>?,
        query: String?,
        filterBy: ParcelSearchingEnum?,
        sortOrder: SortOrderEnum?,
        sortBy: ParcelSortingEnum?
    ): List<Parcel>? {
        return sortParcels(
            filterParcels(parcels, query, filterBy),
            sortOrder,
            sortBy
        )
    }

    private fun sortParcels(
        parcels: List<Parcel>?,
        sortOrder: SortOrderEnum?,
        sortBy: ParcelSortingEnum?
    ): List<Parcel>? {
        if (parcels == null || sortOrder == null || sortBy == null) return parcels

        return when (sortBy) {
            ParcelSortingEnum.TITLE -> {
                when (sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.title }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.title }
                }
            }
            ParcelSortingEnum.SENDER -> {
                when (sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.sender }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.sender }
                }
            }
            ParcelSortingEnum.COURIER -> {
                when (sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.courier }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.courier }
                }
            }
            ParcelSortingEnum.DATE -> {
                when (sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.lastUpdated }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.lastUpdated }
                }
            }
            ParcelSortingEnum.STATUS -> {
                when (sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.parcelStatus.status }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.parcelStatus.status }
                }
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