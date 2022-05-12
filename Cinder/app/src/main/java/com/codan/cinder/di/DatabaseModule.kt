package com.codan.cinder.di

import android.app.Application
import androidx.room.Room
import com.codan.cinder.data.local.ShowDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): ShowDatabase =
        Room.databaseBuilder(app, ShowDatabase::class.java, "show_database")
            .build()
}