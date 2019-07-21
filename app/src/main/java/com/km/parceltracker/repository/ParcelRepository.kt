package com.km.parceltracker.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.km.parceltracker.api.ParcelTrackerApi
import com.km.parceltracker.database.dao.ParcelDao
import com.km.parceltracker.database.ParcelTrackerRoomDatabase
import com.km.parceltracker.model.Parcel
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParcelRepository(context: Context) {

    private val parcelDao: ParcelDao

    init {
        val database = ParcelTrackerRoomDatabase.getDatabase(context)
        parcelDao = database!!.parcelDao()
    }

    @Throws(Throwable::class)
    fun getParcels(): LiveData<List<Parcel>> {
        return parcelDao.getAllParcels()
    }

    fun deleteAndInsert(parcels: List<Parcel>) {
        doAsync {
            parcelDao.deleteAndInsert(parcels)
        }
    }

    fun insert(parcel: Parcel) {
        doAsync {
            parcelDao.insertParcel(parcel)
        }
    }

}