package com.codan.cinder.data.local.domain.remotekey

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codan.cinder.data.local.domain.show.Movie

@Entity(tableName = "movie_remote_keys")
data class MovieRemoteKey(
    @PrimaryKey
    override val id: Long,
    override val prevPage: Int?,
    override val nextPage: Int?
) : ShowRemoteKey<Movie>()
