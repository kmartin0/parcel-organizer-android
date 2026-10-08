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
import java.util.Locale

class ParcelsViewModel(application: Application) : BaseViewModel(application) {

    private val parcelRepository = ParcelRepository(application.applicationContext)
    private val settingsRepository = SettingsRepository(application.applicationContext)
    private val userRepository = UserRepository(application.applicationContext)

    var loggedInUser = userRepository.getLoggedInUser()

    private val locale: Locale by lazy {
        ConfigurationCompat
            .getLocales(Resources.getSystem().configuration)
            .get(0)
            ?: Locale.getDefault()
    }

    private val repoParcels = MutableLiveData<List<Parcel>>()

    val parcels = MediatorLiveData<List<Parcel>>()

    val sortAndFilterConfig = MutableLiveData<ParcelsSortAndFilterConfig>().apply {
        value = settingsRepository.getSortAndFilterSettings()
    }

    private var sortAndFilterDisposable: Disposable? = null

    val startLoadingParcels = SingleLiveEvent<Unit>()

    private fun setupParcelSources() {
        getRepoParcels()

        parcels.addSource(repoParcels) {
            sortAndFilterParcels()
        }

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
                    startLoadingParcels.value = Unit
                }

                override fun onError(e: Throwable) {
                    stopLoading()
                    handleApiError(e)
                }
            })
    }

    /**
     * Delete the [parcel] from the [parcelRepository].
     */
    fun deleteParcel(parcel: Parcel) {
        val disposable = parcelRepository.deleteParcel(parcel.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                startLoading()
            }
            .subscribe(
                {
                    stopLoading()
                    refreshParcels()
                },
                {
                    stopLoading()
                    handleApiError(it)
                }
            )

        disposables.add(disposable)
    }

    /**
     * Sorts and filters the [repoParcels] list and stores the result in [parcels].
     */
    private fun sortAndFilterParcels() {
        val repoParcels = repoParcels.value ?: return
        val config = sortAndFilterConfig.value ?: return

        Single.fromCallable {
            sortParcels(
                filterParcels(repoParcels, config),
                config
            )
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Parcel>> {

                override fun onSuccess(t: List<Parcel>) {
                    stopLoading()
                    parcels.value = t
                }

                override fun onSubscribe(d: Disposable) {
                    sortAndFilterDisposable?.dispose()
                    sortAndFilterDisposable = d
                    disposables.add(d)
                    startLoading()
                }

                override fun onError(e: Throwable) {
                    stopLoading()
                    e.printStackTrace()
                }
            })
    }

    /**
     * @return Sorted parcels list using [sortAndFilterConfig] for sorting options.
     */
    private fun sortParcels(
        parcels: List<Parcel>,
        sortAndFilterConfig: ParcelsSortAndFilterConfig
    ): List<Parcel> {
        return when (sortAndFilterConfig.sortBy) {
            ParcelSortingEnum.TITLE -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING ->
                        parcels.sortedBy { it.title.lowercase(locale) }

                    SortOrderEnum.DESCENDING ->
                        parcels.sortedByDescending { it.title.lowercase(locale) }
                }
            }

            ParcelSortingEnum.SENDER -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING ->
                        parcels.sortedBy { it.sender?.lowercase(locale) }

                    SortOrderEnum.DESCENDING ->
                        parcels.sortedByDescending { it.sender?.lowercase(locale) }
                }
            }

            ParcelSortingEnum.COURIER -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING ->
                        parcels.sortedBy { it.courier?.lowercase(locale) }

                    SortOrderEnum.DESCENDING ->
                        parcels.sortedByDescending { it.courier?.lowercase(locale) }
                }
            }

            ParcelSortingEnum.DATE -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING ->
                        parcels.sortedBy { it.lastUpdated }

                    SortOrderEnum.DESCENDING ->
                        parcels.sortedByDescending { it.lastUpdated }
                }
            }

            ParcelSortingEnum.STATUS -> {
                when (sortAndFilterConfig.sortOrder) {
                    SortOrderEnum.ASCENDING ->
                        parcels.sortedBy { it.parcelStatus.status }

                    SortOrderEnum.DESCENDING ->
                        parcels.sortedByDescending { it.parcelStatus.status }
                }
            }
        }
    }

    /**
     * @return Filtered parcels list using [sortAndFilterConfig] for filter options.
     */
    private fun filterParcels(
        parcels: List<Parcel>,
        sortAndFilterConfig: ParcelsSortAndFilterConfig
    ): List<Parcel> {
        val searchQuery = sortAndFilterConfig.searchQuery

        if (searchQuery.isNullOrBlank()) {
            return filterParcelStatus(
                parcels,
                sortAndFilterConfig
            )
        }

        val normalizedSearchQuery = searchQuery.lowercase(locale)

        return parcels.filter { parcel ->
            if (!sortAndFilterConfig.isParcelStatusSelected(parcel)) {
                return@filter false
            }

            when (sortAndFilterConfig.searchBy) {
                ParcelSearchingEnum.TITLE ->
                    parcel.title
                        .lowercase(locale)
                        .contains(normalizedSearchQuery)

                ParcelSearchingEnum.SENDER ->
                    parcel.sender
                        ?.lowercase(locale)
                        ?.contains(normalizedSearchQuery)
                        ?: false

                ParcelSearchingEnum.COURIER ->
                    parcel.courier
                        ?.lowercase(locale)
                        ?.contains(normalizedSearchQuery)
                        ?: false
            }
        }
    }

    /**
     * @return Parcels filtered by parcel status.
     */
    private fun filterParcelStatus(
        parcels: List<Parcel>,
        sortAndFilterConfig: ParcelsSortAndFilterConfig
    ): List<Parcel> {
        return parcels.filter { parcel ->
            sortAndFilterConfig.isParcelStatusSelected(parcel)
        }
    }

    fun setSortingAndFilterConfig(
        sortAndFilterConfig: ParcelsSortAndFilterConfig
    ) {
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