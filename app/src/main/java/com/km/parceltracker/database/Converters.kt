package com.km.parceltracker.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.km.parceltracker.model.ParcelStatus
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromParcelStatus(value: ParcelStatus?): String? {
        return value?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toParcelStatus(value: String?): ParcelStatus? {
        return value?.let { Gson().fromJson(it, ParcelStatus::class.java) }
    }
}
