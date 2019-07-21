package com.km.parceltracker.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.km.parceltracker.database.ParcelTrackerRoomDatabase
import com.km.parceltracker.database.dao.ParcelDao
import com.km.parceltracker.model.Parcel
import org.jetbrains.anko.doAsync

class ParcelRepository(context: Context) {

    private val parcelDao: ParcelDao

    init {
        val database = ParcelTrackerRoomDatabase.getDatabase(context)
        parcelDao = database!!.parcelDao()
    }

    fun insert(parcel: Parcel) {
        doAsync {
            parcelDao.insertParcel(parcel)
        }
    }

    fun getParcels(): LiveData<List<Parcel>> {
        return parcelDao.getAllParcels()
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