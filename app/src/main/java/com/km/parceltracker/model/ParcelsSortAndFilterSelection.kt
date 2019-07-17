package com.km.parceltracker.model

import com.km.parceltracker.enums.ParcelSearchingEnum
import com.km.parceltracker.enums.ParcelSortingEnum
import com.km.parceltracker.enums.SortOrderEnum

data class ParcelsSortAndFilterSelection(
    // Sorting options
    var sortBy: ParcelSortingEnum,
    var sortOrder: SortOrderEnum,

    // Filter options
    var searchQuery: String?,
    var searchBy: ParcelSearchingEnum,
    var ordered: Boolean,
    var sent: Boolean,
    var delivered: Boolean
)