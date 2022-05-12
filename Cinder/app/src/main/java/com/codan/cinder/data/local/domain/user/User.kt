package com.codan.cinder.data.local.domain.user

import com.codan.cinder.data.local.domain.followedshow.FollowedShow

data class User(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val followedShows: List<FollowedShow> = emptyList()
)
