package com.km.parceltracker.model

import com.km.parceltracker.enums.ParcelSearchingEnum
import com.km.parceltracker.enums.ParcelSortingEnum
import com.km.parceltracker.enums.ParcelStatusEnum
import com.km.parceltracker.enums.SortOrderEnum

data class ParcelsSortAndFilterConfig(
    // Sorting options
    var sortBy: ParcelSortingEnum,
    var sortOrder: SortOrderEnum,

    // Filter options
    var searchQuery: String?,
    var searchBy: ParcelSearchingEnum,
    var ordered: Boolean,
    var sent: Boolean,
    var delivered: Boolean
) {
    /**
     * @return [Boolean] value of the filter option ([ordered], [sent], [delivered]) for the parcel status of the [parcel]
     */
    fun isParcelStatusSelected(parcel: Parcel): Boolean {
        return when (parcel.parcelStatus.status) {
            ParcelStatusEnum.SENT -> sent
            ParcelStatusEnum.ORDERED -> ordered
            ParcelStatusEnum.DELIVERED -> delivered
        }
    }
}