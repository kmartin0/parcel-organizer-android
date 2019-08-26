package com.km.parcelorganizer.enums

import com.km.parcelorganizer.R

enum class ParcelStatusEnum(val stringResId: Int) {
    ORDERED(R.string.ordered),
    SENT(R.string.sent),
    DELIVERED(R.string.delivered);
}