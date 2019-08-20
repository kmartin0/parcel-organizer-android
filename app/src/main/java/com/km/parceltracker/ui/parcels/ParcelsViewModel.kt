package com.km.parceltracker.ui.parcels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.repository.ParcelRepository
import com.km.parceltracker.enums.ParcelSearchingEnum
import com.km.parceltracker.enums.ParcelSortingEnum
import com.km.parceltracker.enums.SortOrderEnum
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.model.ParcelsSortAndFilterConfig
import com.km.parceltracker.repository.SettingsRepository
import com.km.parceltracker.util.Resource
import com.km.parceltracker.util.SingleLiveEvent
import org.jetbrains.anko.doAsync

class ParcelsViewModel(application: Application) : BaseViewModel(application) {

    private val parcelRepository = ParcelRepository(application.applicationContext)
    private val settingsRepository = SettingsRepository(application.applicationContext)

    private var dbParcels = parcelRepository.getParcels()

    var parcels = MediatorLiveData<List<Parcel>>()
    var sortAndFilterConfig = MutableLiveData<ParcelsSortAndFilterConfig>().apply {
        value = settingsRepository.getSortAndFilterSettings()
    }
    var error = SingleLiveEvent<String>()

    init {
        setupParcelSources()
    }

    private fun setupParcelSources() {
        // When the value of dbParcels is changed then sort and filter the list and set the value of parcels to it
        parcels.addSource(dbParcels) {
            when (it) {
                is Resource.Loading -> startLoading()
                is Resource.Success -> {
                    parcels.value = sortAndFilterParcels(it.data, sortAndFilterConfig.value)
                    stopLoading()
                }
                is Resource.Failure -> {
                    error.value = it.throwable.message
                    stopLoading()
                }
            }
        }

        // When the value of sortAndFilterConfig is changed then sort and filter dbParcels and set the value of parcels to it
        parcels.addSource(sortAndFilterConfig) {
            dbParcels.value?.let { resource ->
                if (resource is Resource.Success) parcels.value = sortAndFilterParcels(resource.data, it)
            }
        }
    }

    /**
     * Delete the [parcel] from the [parcelRepository]
     */
    fun deleteParcel(parcel: Parcel) {
        doAsync {
            parcelRepository.deleteParcel(parcel)
        }
    }

    /**
     * @return List<Parcel> sorted and filtered parcels list using [sortAndFilterConfig] for the sorting and filter options
     */
    private fun sortAndFilterParcels(
        parcels: List<Parcel>?,
        sortAndFilterConfig: ParcelsSortAndFilterConfig?
    ): List<Parcel>? {
        return sortParcels(filterParcels(parcels, sortAndFilterConfig), sortAndFilterConfig)
    }

    /**
     * @return List<Parcel> Sorted parcels list using [sortAndFilterConfig] for sorting options.
     */
    private fun sortParcels(
        parcels: List<Parcel>?,
        sortAndFilterConfig: ParcelsSortAndFilterConfig?
    ): List<Parcel>? {
        // Return the parcels list if no parcels or sorting configuration is provided.
        // Otherwise use the sortBy attribute of the sortAndFilterConfig to determine by which attribute the list
        // should be sorted. Then use the sortOrder attribute to determine the sort order.
        return if (parcels == null || sortAndFilterConfig == null) parcels
        else when (sortAndFilterConfig.sortBy) {
            ParcelSortingEnum.TITLE -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.title.toLowerCase() }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.title.toLowerCase() }
                }
            }
            ParcelSortingEnum.SENDER -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.sender?.toLowerCase() }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.sender?.toLowerCase() }
                }
            }
            ParcelSortingEnum.COURIER -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.courier?.toLowerCase() }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending { it.courier?.toLowerCase() }
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

    /**
     * @return List<Parcel> Filtered parcels list using [sortAndFilterConfig] for filter options.
     */
    private fun filterParcels(
        parcels: List<Parcel>?,
        sortAndFilterConfig: ParcelsSortAndFilterConfig?
    ): List<Parcel>? {
        // Return the parcels list if no parcels or sorting configuration is provided.
        // If no search query is provided only filter by parcel status.
        // Otherwise filter by search query and parcel status.
        return if (parcels == null || sortAndFilterConfig == null) parcels
        else if (sortAndFilterConfig.searchQuery.isNullOrBlank()) filterParcelStatus(parcels, sortAndFilterConfig)
        else {
            parcels.filter { parcel ->
                when (sortAndFilterConfig.searchBy) { // Find the attribute to filter by. Then use the searchQuery and parcel status to filter.
                    ParcelSearchingEnum.TITLE -> {
                        sortAndFilterConfig.isParcelStatusSelected(parcel) &&
                                parcel.title.toLowerCase().contains(sortAndFilterConfig.searchQuery!!.toLowerCase())
                    }
                    ParcelSearchingEnum.SENDER -> {
                        if (parcel.sender.isNullOrBlank()) false
                        else sortAndFilterConfig.isParcelStatusSelected(parcel) &&
                                parcel.sender!!.toLowerCase().contains(sortAndFilterConfig.searchQuery!!.toLowerCase())
                    }
                    ParcelSearchingEnum.COURIER -> {
                        if (parcel.sender.isNullOrBlank()) false
                        else sortAndFilterConfig.isParcelStatusSelected(parcel) &&
                                parcel.sender!!.toLowerCase().contains(sortAndFilterConfig.searchQuery!!.toLowerCase())
                    }
                }
            }
        }
    }

    /**
     * @return List<Parcel> Filtered parcels list by Parcel Status.
     */
    private fun filterParcelStatus(
        parcels: List<Parcel>?,
        sortAndFilterConfig: ParcelsSortAndFilterConfig?
    ): List<Parcel>? {
        return if (parcels == null || sortAndFilterConfig == null) parcels
        else {
            parcels.filter { parcel ->
                sortAndFilterConfig.isParcelStatusSelected(parcel)
            }
        }
    }

    fun setSortingAndFilterConfig(sortAndFilterConfig: ParcelsSortAndFilterConfig) {
        settingsRepository.setSortAndFilterSettings(sortAndFilterConfig)
        this.sortAndFilterConfig.value = sortAndFilterConfig
    }

    fun refresh() {
        if (isLoading.value == false) {
            dbParcels = parcelRepository.getParcels()
            parcels = MediatorLiveData()
            setupParcelSources()
        }
    }
}