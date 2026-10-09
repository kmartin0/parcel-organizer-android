package nl.kmartin.parcelorganizer.enums

import nl.kmartin.parcelorganizer.R

enum class ParcelStatusEnum(val stringResId: Int) {
    ORDERED(R.string.ordered),
    SENT(R.string.sent),
    DELIVERED(R.string.delivered);
}