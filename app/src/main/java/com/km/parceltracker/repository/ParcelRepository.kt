package com.km.parceltracker.repository

import android.content.Context
import com.km.parceltracker.api.ParcelTrackerApi
import com.km.parceltracker.database.ParcelTrackerRoomDatabase
import com.km.parceltracker.database.dao.ParcelDao
import com.km.parceltracker.model.Parcel
import io.reactivex.Single
import org.jetbrains.anko.doAsync

class ParcelRepository(context: Context) {

    private val parcelDao: ParcelDao
    private val api = ParcelTrackerApi.createApi(context)

    init {
        val database = ParcelTrackerRoomDatabase.getDatabase(context)
        parcelDao = database!!.parcelDao()
    }

    fun insert(parcel: Parcel) {
        doAsync {
            parcelDao.insertParcel(parcel)
        }
    }

    fun getParcels(): Single<List<Parcel>> {
        return api.getParcels()
    }

    fun updateParcel(parcel: Parcel) {
        doAsync {
            parcelDao.updateParcel(parcel)
        }
    }

    fun deleteParcel(parcel: Parcel) {
        doAsync {
            parcelDao.deleteParcel(parcel)
        }
    }

}