package com.codan.cinder.data.local.domain.remotekey

import com.codan.cinder.data.local.domain.show.Show

abstract class ShowRemoteKey<T : Show> {
    abstract val id: Long
    abstract val prevPage: Int?
    abstract val nextPage: Int?
}