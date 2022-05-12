package com.codan.cinder.data.local.dao.remotekey

import androidx.room.Dao
import androidx.room.Query
import com.codan.cinder.data.local.domain.remotekey.MovieRemoteKey
import com.codan.cinder.data.local.domain.show.Movie

@Dao
interface MovieRemoteKeyDao : ShowRemoteKeyDao<Movie, MovieRemoteKey> {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    override suspend fun insertAll(items: List<MovieRemoteKey>)

    @Query("SELECT * FROM movie_remote_keys WHERE id = :id")
    override suspend fun find(id: Long): MovieRemoteKey

    @Query("DELETE FROM movie_remote_keys")
    override suspend fun deleteAll()
}