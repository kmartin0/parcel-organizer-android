package com.km.parceltracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.km.parceltracker.model.Parcel


@Dao
interface ParcelDao {

    @Query("SELECT * FROM Parcel")
    fun getAllParcels(): LiveData<List<Parcel>>

    @Insert
    fun insertParcels(parcels: List<Parcel>)

    @Insert
    fun insertParcel(parcels: Parcel)

    @Delete
    fun deleteParcel(parcel: Parcel)

    @Query("DELETE FROM Parcel")
    fun clearParcelTable()

    @Transaction
    fun deleteAndInsert(parcels: List<Parcel>) {
        clearParcelTable()
        insertParcels(parcels)
    }

}