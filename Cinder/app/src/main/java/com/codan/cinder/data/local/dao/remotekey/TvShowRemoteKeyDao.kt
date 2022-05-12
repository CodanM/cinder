package com.codan.cinder.data.local.dao.remotekey

import androidx.room.Dao
import androidx.room.Query
import com.codan.cinder.data.local.domain.remotekey.TvShowRemoteKey
import com.codan.cinder.data.local.domain.show.TvShow

@Dao
interface TvShowRemoteKeyDao : ShowRemoteKeyDao<TvShow, TvShowRemoteKey> {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    override suspend fun insertAll(items: List<TvShowRemoteKey>)

    @Query("SELECT * FROM tv_show_remote_keys WHERE id = :id")
    override suspend fun find(id: Long): TvShowRemoteKey

    @Query("DELETE FROM tv_show_remote_keys")
    override suspend fun deleteAll()
}