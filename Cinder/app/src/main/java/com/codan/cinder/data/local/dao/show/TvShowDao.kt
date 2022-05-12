package com.codan.cinder.data.local.dao.show

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.codan.cinder.data.local.domain.show.TvShow
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowDao : ShowDao<TvShow> {

    @Query("SELECT * FROM tv_shows")
    override fun getAll(): PagingSource<Int, TvShow>

    @Query("SELECT * FROM tv_shows WHERE remoteId = :id")
    override fun find(id: Long): Flow<TvShow>

    @Query("DELETE FROM tv_shows")
    override suspend fun deleteAll()
}