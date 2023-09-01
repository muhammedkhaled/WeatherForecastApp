package com.mukhaled.weatherforecastapp.core.data.di

import android.content.Context
import androidx.room.Room
import com.mukhaled.weatherforecastapp.core.data.cash.AppDatabase
import com.mukhaled.weatherforecastapp.core.data.cash.RoomCache
import com.mukhaled.weatherforecastapp.core.data.cash.Cache
import com.mukhaled.weatherforecastapp.core.data.cash.daos.Dao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    abstract fun bindCache(cache: RoomCache): Cache

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(
            @ApplicationContext context: Context,
        ): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "weather.db"
            )
                .build()
        }

        @Provides
        fun provideDao(
            petSaveDatabase: AppDatabase,
        ): Dao = petSaveDatabase.weatherDao()
    }
}
