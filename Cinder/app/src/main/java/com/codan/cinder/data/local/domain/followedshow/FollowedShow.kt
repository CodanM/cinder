package com.codan.cinder.data.local.domain.followedshow

import com.codan.cinder.data.local.domain.show.Show
import com.codan.cinder.data.local.domain.user.User
import com.google.firebase.database.Exclude

data class FollowedShow(
    val id: String? = null,
    val user: User? = null,
    val show: Show? = null,
    val currentEpisode: Int? = null,
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "username" to user!!.username,
            "showId" to show!!.remoteId,
            "currentEpisode" to currentEpisode
        )
    }
}