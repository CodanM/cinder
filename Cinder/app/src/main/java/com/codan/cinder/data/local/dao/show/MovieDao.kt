package com.codan.cinder.data.local.dao.show

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.codan.cinder.data.local.domain.show.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao : ShowDao<Movie> {

    @Query("SELECT * FROM movies")
    override fun getAll(): PagingSource<Int, Movie>

    @Query("SELECT * FROM movies WHERE remoteId = :id")
    override fun find(id: Long): Flow<Movie>

    @Query("DELETE FROM movies")
    override suspend fun deleteAll()
}