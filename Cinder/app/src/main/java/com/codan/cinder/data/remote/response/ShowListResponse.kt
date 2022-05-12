package com.codan.cinder.data.remote.response

import com.codan.cinder.data.local.domain.show.Show

data class ShowListResponse<T : Show>(
    val results: List<T>,
    val page: Int?,
    val totalResults: Int?,
    val totalPages: Int?,
)