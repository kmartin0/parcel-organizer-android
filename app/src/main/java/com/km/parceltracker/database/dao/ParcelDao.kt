package com.km.parceltracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.km.parceltracker.model.Parcel
import androidx.room.Transaction


@Dao
interface ParcelDao {

    @Query("SELECT * FROM Parcel")
    fun getAllParcels(): LiveData<List<Parcel>>

    @Insert
    fun insertParcels(parcels: List<Parcel>)

    @Insert
    fun insertParcel(parcels: Parcel)

    @Query("DELETE FROM Parcel")
    fun clearParcelTable()

    @Transaction
    fun deleteAndInsert(parcels: List<Parcel>) {
        clearParcelTable()
        insertParcels(parcels)
    }

}