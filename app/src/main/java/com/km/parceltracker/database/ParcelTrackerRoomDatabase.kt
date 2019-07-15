package com.km.parceltracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.km.parceltracker.model.Parcel

@Database(entities = [Parcel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ParcelTrackerRoomDatabase : RoomDatabase() {

    abstract fun parcelDao(): ParcelDao

    companion object {
        private const val DATABASE_NAME = "PARCEL_TRACKER_ROOM_DATABASE"

        @Volatile
        private var INSTANCE: ParcelTrackerRoomDatabase? = null

        fun getDatabase(context: Context): ParcelTrackerRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(ParcelTrackerRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ParcelTrackerRoomDatabase::class.java, DATABASE_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }

}