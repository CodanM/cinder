package com.codan.cinder.data.local.dao.remotekey

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.codan.cinder.data.local.domain.remotekey.ShowRemoteKey
import com.codan.cinder.data.local.domain.show.Show

interface ShowRemoteKeyDao<S : Show, T : ShowRemoteKey<S>> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<T>)

    suspend fun find(id: Long): T

    suspend fun deleteAll()
}