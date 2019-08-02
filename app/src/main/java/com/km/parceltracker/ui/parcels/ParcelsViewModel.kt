package com.km.parceltracker.ui.parcels

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.km.parceltracker.base.BaseViewModel
import com.km.parceltracker.repository.ParcelRepository
import com.km.parceltracker.enums.ParcelSearchingEnum
import com.km.parceltracker.enums.ParcelSortingEnum
import com.km.parceltracker.enums.ParcelStatusEnum
import com.km.parceltracker.enums.SortOrderEnum
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.model.ParcelsSortAndFilterConfig
import com.km.parceltracker.repository.SettingsRepository
import org.jetbrains.anko.doAsync

class ParcelsViewModel(application: Application) : BaseViewModel(application) {

    private val parcelRepository = ParcelRepository(application.applicationContext)
    private val settingsRepository = SettingsRepository(application.applicationContext)

    private val dbParcels = parcelRepository.getParcels()

    var parcels = MediatorLiveData<List<Parcel>>()
    var sortAndFilterConfig = MutableLiveData<ParcelsSortAndFilterConfig>().apply {
        value = settingsRepository.getSortAndFilterSettings()
    }

    init {
        parcels.addSource(dbParcels) {
            parcels.value = sortAndFilterParcels(it, sortAndFilterConfig.value)
        }

        parcels.addSource(sortAndFilterConfig) {
            parcels.value = sortAndFilterParcels(dbParcels.value, it)
        }
    }

    fun deleteParcel(parcel: Parcel) {
        doAsync {
            parcelRepository.deleteParcel(parcel)
        }
    }

    private fun sortAndFilterParcels(
        parcels: List<Parcel>?,
        sortAndFilterConfig: ParcelsSortAndFilterConfig?
    ): List<Parcel>? {
        return sortParcels(filterParcels(parcels, sortAndFilterConfig), sortAndFilterConfig)
    }

    private fun sortParcels(
        parcels: List<Parcel>?,
        sortAndFilterConfig: ParcelsSortAndFilterConfig?
    ): List<Parcel>? {
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

    private fun filterParcels(
        parcels: List<Parcel>?,
        sortAndFilterConfig: ParcelsSortAndFilterConfig?
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
                        ) && parcel.sender!!.toLowerCase().contains(sortAndFilterConfig.searchQuery!!.toLowerCase())
                    }
                    ParcelSearchingEnum.COURIER -> {
                        if (parcel.sender.isNullOrBlank()) false
                        else isParcelStatus(
                            sortAndFilterConfig,
                            parcel
                        ) && parcel.sender!!.toLowerCase().contains(sortAndFilterConfig.searchQuery!!.toLowerCase())
                    }
                }
            }
        }
    }

    private fun filterParcelStatus(
        parcels: List<Parcel>?,
        sortAndFilterConfig: ParcelsSortAndFilterConfig?
    ): List<Parcel>? {
        return if (parcels == null || sortAndFilterConfig == null) parcels
        else {
            parcels.filter { parcel ->
                isParcelStatus(sortAndFilterConfig, parcel)
            }
        }
    }

    private fun isParcelStatus(sortAndFilterConfig: ParcelsSortAndFilterConfig, parcel: Parcel): Boolean {
        return when (parcel.parcelStatus.status) {
            ParcelStatusEnum.SENT -> sortAndFilterConfig.sent
            ParcelStatusEnum.ORDERED -> sortAndFilterConfig.ordered
            ParcelStatusEnum.DELIVERED -> sortAndFilterConfig.delivered
        }
    }

    fun setSortingAndFilterConfig(sortAndFilterConfig: ParcelsSortAndFilterConfig) {
        settingsRepository.setSortAndFilterSettings(sortAndFilterConfig)
        this.sortAndFilterConfig.value = sortAndFilterConfig
    }
}