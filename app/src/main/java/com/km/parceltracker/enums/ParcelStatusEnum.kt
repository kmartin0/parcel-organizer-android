package com.km.parceltracker.enums

import com.km.parceltracker.R

enum class ParcelStatusEnum(val statusResId: Int) {
    ORDERED(R.string.ordered),
    SENT(R.string.sent),
    DELIVERED(R.string.delivered);

}