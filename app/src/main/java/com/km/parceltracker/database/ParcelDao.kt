package com.km.parceltracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.km.parceltracker.model.Parcel

@Dao
interface ParcelDao {

    @Query("SELECT * FROM Parcel")
    fun getAllParcels(): LiveData<List<Parcel>>

    @Insert
    fun insertParcels(parcels: List<Parcel>)

    @Query("DELETE FROM Parcel")
    fun clearParcelTable()

}