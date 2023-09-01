package com.mukhaled.weatherforecastapp.core.data.cash.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Weather_Image")
data class CashedItemData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val imgPath: String
)