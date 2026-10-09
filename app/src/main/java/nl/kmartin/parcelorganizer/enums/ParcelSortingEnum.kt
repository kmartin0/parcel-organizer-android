package nl.kmartin.parcelorganizer.enums

import nl.kmartin.parcelorganizer.R

enum class ParcelSortingEnum(val stringResId: Int) {
    TITLE(R.string.title),
    SENDER(R.string.sender),
    COURIER(R.string.courier),
    DATE(R.string.last_updated),
    STATUS(R.string.status)
}