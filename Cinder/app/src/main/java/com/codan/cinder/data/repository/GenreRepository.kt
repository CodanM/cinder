package com.codan.cinder.data.repository

import com.codan.cinder.data.local.domain.show.Genre
import com.codan.cinder.data.local.ShowDatabase
import com.codan.cinder.data.remote.service.TheMovieDbService
import javax.inject.Inject

class GenreRepository @Inject constructor(
    database: ShowDatabase,
    private val service: TheMovieDbService,
) {

    private val genreDao = database.genreDao()

    val genres = genreDao.getAll()

    suspend fun fetchAllGenres() {
        insertAll(service.fetchAllMovieGenres().genres)
        insertAll(service.fetchAllTvShowGenres().genres)
    }

    suspend fun insertAll(items: List<Genre>) {
        genreDao.insertAll(items)
    }

}