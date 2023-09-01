package com.mukhaled.weatherforecastapp.core.data.cash

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mukhaled.weatherforecastapp.core.data.cash.daos.Dao
import com.mukhaled.weatherforecastapp.core.data.cash.model.CashedItemData


@Database(
    entities = [
      CashedItemData::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun weatherDao(): Dao
}
