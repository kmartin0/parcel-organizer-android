package com.km.parceltracker.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.km.parceltracker.api.ParcelTrackerApi
import com.km.parceltracker.database.ParcelTrackerRoomDatabase
import com.km.parceltracker.database.dao.ParcelDao
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.util.Resource
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

class ParcelRepository(context: Context) {

    private val parcelDao: ParcelDao
    private val api = ParcelTrackerApi.createApi()

    init {
        val database = ParcelTrackerRoomDatabase.getDatabase(context)
        parcelDao = database!!.parcelDao()
    }

    fun insert(parcel: Parcel) {
        doAsync {
            parcelDao.insertParcel(parcel)
        }
    }

    fun getParcels(): MutableLiveData<Resource<List<Parcel>>> {
        val state = MutableLiveData<Resource<List<Parcel>>>()

        api.getParcels()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<Parcel>> {
                override fun onSuccess(t: List<Parcel>) {
                    state.value = Resource.Success(t)
                    Log.i("TAGZ", "Success: ${state.value}")
                }

                override fun onSubscribe(d: Disposable) {
                    state.value = Resource.Loading()
                    Log.i("TAGZ", "Subscribing ${state.value}")
                }

                override fun onError(e: Throwable) {
                    state.value = Resource.Failure(e)
                    Log.i("TAGZ", "Error: ${state.value}")
                }
            })

        return state
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