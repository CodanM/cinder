package com.codan.cinder.data.local.domain.remotekey

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codan.cinder.data.local.domain.show.TvShow

@Entity(tableName = "tv_show_remote_keys")
data class TvShowRemoteKey(
    @PrimaryKey
    override val id: Long,
    override val prevPage: Int?,
    override val nextPage: Int?
) : ShowRemoteKey<TvShow>()
