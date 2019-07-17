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
import com.km.parceltracker.model.ParcelsSortAndFilterSelection

class ParcelsViewModel(application: Application) : BaseViewModel(application) {

    private val parcelRepository = ParcelRepository(application.applicationContext) { println("API ERROR: $it") }
    private val dbParcels = parcelRepository.getParcels()

    var parcels = MediatorLiveData<List<Parcel>>()
    var sortAndFilterSelection = MutableLiveData<ParcelsSortAndFilterSelection>().apply {
        value = ParcelsSortAndFilterSelection(
            sortBy = ParcelSortingEnum.TITLE,
            sortOrder = SortOrderEnum.ASCENDING,
            searchQuery = null,
            searchBy = ParcelSearchingEnum.TITLE,
            ordered = true,
            sent = true,
            delivered = true
        )
    }

    init {
        parcels.addSource(dbParcels) {
            parcels.value = sortAndFilterParcels(it, sortAndFilterSelection.value)
        }

        parcels.addSource(sortAndFilterSelection) {
            parcels.value = sortAndFilterParcels(dbParcels.value, it)
        }
    }

    private fun sortAndFilterParcels(
        parcels: List<Parcel>?,
        sortAndFilterConfig: ParcelsSortAndFilterSelection?
    ): List<Parcel>? {
        return sortParcels(filterParcels(parcels, sortAndFilterConfig), sortAndFilterConfig)
    }

    private fun sortParcels(
        parcels: List<Parcel>?,
        sortAndFilterConfig: ParcelsSortAndFilterSelection?
    ): List<Parcel>? {
        return if (parcels == null || sortAndFilterConfig == null) parcels
        else when (sortAndFilterConfig.sortBy) {
            ParcelSortingEnum.TITLE -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.title }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.title }
                }
            }
            ParcelSortingEnum.SENDER -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.sender }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.sender }
                }
            }
            ParcelSortingEnum.COURIER -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.courier }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.courier }
                }
            }
            ParcelSortingEnum.DATE -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.lastUpdated }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.lastUpdated }
                }
            }
            ParcelSortingEnum.STATUS -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.parcelStatus.status }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.parcelStatus.status }
                }
            }
        }
    }

    private fun filterParcels(
        parcels: List<Parcel>?,
        sortAndFilterConfig: ParcelsSortAndFilterSelection?
    ): List<Parcel>? {
        return if (parcels == null || sortAndFilterConfig == null) parcels
        else if (sortAndFilterConfig.searchQuery.isNullOrBlank()) filterParcelStatus(parcels, sortAndFilterConfig)
        else {
            parcels.filter { parcel ->
                when (sortAndFilterConfig.searchBy) {
                    ParcelSearchingEnum.TITLE -> {
                        isParcelStatus(
                            sortAndFilterConfig,
                            parcel
                        ) && parcel.title.toLowerCase().contains(sortAndFilterConfig.searchQuery!!.toLowerCase())
                    }
                    ParcelSearchingEnum.SENDER -> {
                        if (parcel.sender.isNullOrBlank()) false
                        else isParcelStatus(
                            sortAndFilterConfig,
                            parcel
                        ) && parcel.sender.toLowerCase().contains(sortAndFilterConfig.searchQuery!!.toLowerCase())
                    }
                    ParcelSearchingEnum.COURIER -> {
                        if (parcel.sender.isNullOrBlank()) false
                        else isParcelStatus(
                            sortAndFilterConfig,
                            parcel
                        ) && parcel.sender.toLowerCase().contains(sortAndFilterConfig.searchQuery!!.toLowerCase())
                    }
                }
            }
        }
    }

    private fun filterParcelStatus(
        parcels: List<Parcel>?,
        sortAndFilterConfig: ParcelsSortAndFilterSelection?
    ): List<Parcel>? {
        return if (parcels == null || sortAndFilterConfig == null) parcels
        else {
            parcels.filter { parcel ->
                isParcelStatus(sortAndFilterConfig, parcel)
            }
        }
    }

    private fun isParcelStatus(sortAndFilterConfig: ParcelsSortAndFilterSelection, parcel: Parcel): Boolean {
        return when (parcel.parcelStatus.status) {
            "SENT" -> sortAndFilterConfig.sent
            "ORDERED" -> sortAndFilterConfig.ordered
            "DELIVERED" -> sortAndFilterConfig.delivered
            else -> false
        }
    }
}