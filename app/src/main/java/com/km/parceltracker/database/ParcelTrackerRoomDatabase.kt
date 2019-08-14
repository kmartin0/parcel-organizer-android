package com.km.parceltracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.km.parceltracker.database.dao.ParcelDao
import com.km.parceltracker.enums.ParcelStatusEnum
import com.km.parceltracker.model.Parcel
import com.km.parceltracker.model.ParcelStatus
import org.jetbrains.anko.doAsync
import java.util.*
import kotlin.collections.ArrayList

@Database(entities = [Parcel::class], version = 2, exportSchema = false)
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
                            .addCallback(callback())
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        private fun callback(): Callback {
            return object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
//                    INSTANCE?.let { database ->
//                        doAsync {
//                            database.parcelDao().deleteAndInsert(
//                                arrayListOf(
//                                    Parcel(
//                                        "OnePlus 7",
//                                        "One Plus",
//                                        "PostNL",
//                                        null,
//                                        ParcelStatus(1L, ParcelStatusEnum.SENT),
//                                        Date(1556020800),
//                                        1L
//                                    ),
//                                    Parcel(
//                                        "Clothes",
//                                        "Zalando",
//                                        "PostNL",
//                                        null,
//                                        ParcelStatus(1L, ParcelStatusEnum.SENT),
//                                        Date(1563641640),
//                                        2L
//                                    ),
//                                    Parcel(
//                                        "Charging Cables",
//                                        "Cables.nl",
//                                        "DPD",
//                                        null,
//                                        ParcelStatus(1L, ParcelStatusEnum.ORDERED),
//                                        Date(1559415000),
//                                        3L
//                                    ),
//                                    Parcel(
//                                        "Garmin Vivomove HR",
//                                        "Bol.com",
//                                        "DHL",
//                                        null,
//                                        ParcelStatus(1L, ParcelStatusEnum.DELIVERED),
//                                        Date(1559315000),
//                                        4L
//                                    )
//                                )
//                            )
//                        }
//                    }
                }
            }
        }
    }

}