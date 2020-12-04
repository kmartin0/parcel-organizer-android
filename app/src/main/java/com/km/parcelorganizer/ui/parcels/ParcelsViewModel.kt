package com.km.parcelorganizer.ui.parcels

import android.app.Application
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.km.parcelorganizer.base.BaseViewModel
import com.km.parcelorganizer.enums.ParcelSearchingEnum
import com.km.parcelorganizer.enums.ParcelSortingEnum
import com.km.parcelorganizer.enums.SortOrderEnum
import com.km.parcelorganizer.model.Parcel
import com.km.parcelorganizer.model.ParcelsSortAndFilterConfig
import com.km.parcelorganizer.repository.ParcelRepository
import com.km.parcelorganizer.repository.SettingsRepository
import com.km.parcelorganizer.repository.UserRepository
import com.km.parcelorganizer.util.SingleLiveEvent
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class ParcelsViewModel(application: Application) : BaseViewModel(application) {

    private val parcelRepository = ParcelRepository(application.applicationContext)
    private val settingsRepository = SettingsRepository(application.applicationContext)
    private val userRepository = UserRepository(application.applicationContext)
    var loggedInUser = userRepository.getLoggedInUser()
    private val locale: Locale by lazy {
        ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)
    }
    private var repoParcels = MutableLiveData<List<Parcel>>()
    var parcels = MediatorLiveData<List<Parcel>>()
    var sortAndFilterConfig = MutableLiveData<ParcelsSortAndFilterConfig>().apply {
        value = settingsRepository.getSortAndFilterSettings()
    }
    private var sortAndFilterDisposable: Disposable? = null
    val startLoadingParcels = SingleLiveEvent<Any>()

    private fun setupParcelSources() {
        // Retrieve the parcels from the repository and add the value in repoParcels
        getRepoParcels()

        // When the value of repoParcels is changed then sort and filter the list and set the value of parcels to it
        parcels.addSource(repoParcels) {
            sortAndFilterParcels()
        }

        // When the value of sortAndFilterConfig is changed then sort and filter repoParcels and set the value of parcels to it
        parcels.addSource(sortAndFilterConfig) {
            sortAndFilterParcels()
        }
    }

    private fun getRepoParcels() {
        parcelRepository.getParcels()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Parcel>> {
                override fun onSuccess(t: List<Parcel>) {
                    stopLoading()
                    repoParcels.value = t
                }

                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                    startLoading()
                    startLoadingParcels.call()
                }

                override fun onError(e: Throwable) {
                    stopLoading()
                    handleApiError(e)
                }
            })
    }

    /**
     * Delete the [parcel] from the [parcelRepository]
     */
    fun deleteParcel(parcel: Parcel) {
        parcelRepository.deleteParcel(parcel.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                disposables.add(it)
                startLoading()
            }
            .doOnComplete {
                stopLoading()
                refreshParcels()
            }
            .doOnError {
                stopLoading()
                handleApiError(it)
            }
            .subscribe()
    }

    /**
     * Sorts and filters the [repoParcels] list and stores the result in [parcels]
     */
    private fun sortAndFilterParcels() {
        Single.fromCallable {
            sortParcels(
                filterParcels(
                    repoParcels.value,
                    sortAndFilterConfig.value
                ), sortAndFilterConfig.value
            )
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Parcel>?> {
                override fun onSuccess(t: List<Parcel>) {
                    stopLoading()
                    this@ParcelsViewModel.parcels.value = t
                }

                override fun onSubscribe(d: Disposable) {
                    sortAndFilterDisposable?.dispose()
                    sortAndFilterDisposable = d
                    startLoading()
                }

                override fun onError(e: Throwable) {
                    stopLoading()
                    e.printStackTrace()
                }
            })
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
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.title.toLowerCase(locale) }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending {
                        it.title.toLowerCase(
                            locale
                        )
                    }
                }
            }
            ParcelSortingEnum.SENDER -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.sender?.toLowerCase(locale) }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending {
                        it.sender?.toLowerCase(
                            locale
                        )
                    }
                }
            }
            ParcelSortingEnum.COURIER -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING -> parcels.sortedBy { it.courier?.toLowerCase(locale) }
                    SortOrderEnum.DESCENDING -> parcels.sortedByDescending {
                        it.courier?.toLowerCase(
                            locale
                        )
                    }
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
        else if (sortAndFilterConfig.searchQuery.isNullOrBlank()) filterParcelStatus(
            parcels,
            sortAndFilterConfig
        )
        else {
            parcels.filter { parcel ->
                when (sortAndFilterConfig.searchBy) { // Find the attribute to filter by. Then use the searchQuery and parcel status to filter.
                    ParcelSearchingEnum.TITLE -> {
                        sortAndFilterConfig.isParcelStatusSelected(parcel) &&
                                parcel.title.toLowerCase(locale).contains(
                                    sortAndFilterConfig.searchQuery!!.toLowerCase(
                                        locale
                                    )
                                )
                    }
                    ParcelSearchingEnum.SENDER -> {
                        if (parcel.sender.isNullOrBlank()) false
                        else sortAndFilterConfig.isParcelStatusSelected(parcel) &&
                                parcel.sender!!.toLowerCase(locale).contains(
                                    sortAndFilterConfig.searchQuery!!.toLowerCase(
                                        locale
                                    )
                                )
                    }
                    ParcelSearchingEnum.COURIER -> {
                        if (parcel.courier.isNullOrBlank()) false
                        else sortAndFilterConfig.isParcelStatusSelected(parcel) &&
                                parcel.courier!!.toLowerCase(locale).contains(
                                    sortAndFilterConfig.searchQuery!!.toLowerCase(
                                        locale
                                    )
                                )
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

    fun refreshParcels() {
        if (isLoading.value == false) {
            parcels.removeSource(repoParcels)
            parcels.removeSource(sortAndFilterConfig)
            setupParcelSources()
            loggedInUser = userRepository.getLoggedInUser()
        }
    }

}