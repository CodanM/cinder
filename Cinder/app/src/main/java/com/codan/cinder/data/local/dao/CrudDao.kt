package com.codan.cinder.data.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

interface CrudDao<I, T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<T>)

    fun getAll(): Flow<List<T>>

    suspend fun deleteAll()
}