package com.codan.cinder.data.local.domain.remotekey

import kotlin.reflect.KClass

class ShowRemoteKeyFactory {

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T : ShowRemoteKey<*>> getRemoteKey(
            remoteKeyClass: KClass<T>,
            id: Long,
            prevPage: Int?,
            nextPage: Int?
        ): T =
            when (remoteKeyClass) {
                MovieRemoteKey::class -> MovieRemoteKey(id, prevPage, nextPage) as T
                TvShowRemoteKey::class -> TvShowRemoteKey(id, prevPage, nextPage) as T
                else -> throw Exception("Invalid type!")
            }
    }
}