package com.mukhaled.weatherforecastapp.core.data.cash.daos

import androidx.room.*
import androidx.room.Dao
import com.mukhaled.weatherforecastapp.core.data.cash.model.CashedItemData
import io.reactivex.Flowable

@Dao
abstract class Dao {

    @Transaction
    @Query("SELECT * FROM Weather_Image")
    abstract fun getAll(): Flowable<List<CashedItemData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertItem(
        item: CashedItemData
    )
}
