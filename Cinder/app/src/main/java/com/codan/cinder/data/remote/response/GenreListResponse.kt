package com.codan.cinder.data.remote.response

import com.codan.cinder.data.local.domain.show.Genre
import com.google.gson.annotations.SerializedName

data class GenreListResponse(
    @SerializedName("genres")
    val genres: List<Genre>
)