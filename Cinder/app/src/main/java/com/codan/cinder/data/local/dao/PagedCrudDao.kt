package com.codan.cinder.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

interface PagedCrudDao<I, T : Any> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<T>)

    fun getAll(): PagingSource<Int, T>

    fun find(id: I): Flow<T>

    suspend fun deleteAll()
}