package com.km.parceltracker.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.km.parceltracker.api.ParcelTrackerApi
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.model.ParcelStatus
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ParcelRepository(context: Context, val errorCallback: (Throwable) -> Unit) {

    private val parcelDao: ParcelDao
    private val parcelTrackerApiService = ParcelTrackerApi.createApi()

    init {
        val database = ParcelTrackerRoomDatabase.getDatabase(context)
        parcelDao = database!!.parcelDao()
    }

    @Throws(Throwable::class)
    fun getParcels(): LiveData<List<Parcel>> {
        parcelTrackerApiService.getParcels().enqueue(object : Callback<List<Parcel>> {
            override fun onFailure(call: Call<List<Parcel>>, t: Throwable) {
                println("ERROR: ${t.message}")
                errorCallback(t)
            }

            override fun onResponse(call: Call<List<Parcel>>, response: Response<List<Parcel>>) {
                if (response.isSuccessful) {
                    response.body()?.let { deleteAndInsert(it) }
                } else {
                    println("RESPONSE UNSUCCESSFUL: ${response.errorBody()?.string()}")
                }
            }

        })
        return parcelDao.getAllParcels()
    }

    fun deleteAndInsert(parcels: List<Parcel>) {
        doAsync {
            parcelDao.deleteAndInsert(parcels)
        }
    }

}