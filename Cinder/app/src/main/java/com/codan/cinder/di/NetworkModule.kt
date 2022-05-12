package com.codan.cinder.di

import com.codan.cinder.data.remote.deserializer.ReleaseDateDeserializer
import com.codan.cinder.data.remote.service.TheMovieDbService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTheMovieDbRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(TheMovieDbService.BASE_URL)
            .addConverterFactory(buildGsonConverter())
            .build()

    private fun buildGsonConverter(): GsonConverterFactory =
        GsonBuilder().run {
            registerTypeAdapter(LocalDate::class.java, ReleaseDateDeserializer())
            create()
        }.let { GsonConverterFactory.create(it) }

    @Provides
    @Singleton
    fun provideTheMovieDbService(retrofit: Retrofit): TheMovieDbService =
        retrofit.create(TheMovieDbService::class.java)
}