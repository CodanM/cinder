package com.codan.cinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codan.cinder.data.local.domain.show.Genre
import com.codan.cinder.data.repository.GenreRepository
import com.codan.cinder.data.repository.ShowRepository
import com.codan.cinder.data.remote.service.ServiceOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowViewModel @Inject constructor(
    private val showRepository: ShowRepository,
    genreRepository: GenreRepository,
) : ViewModel() {

    val genres: Flow<List<Genre>> = genreRepository.genres

    fun movies(operation: ServiceOperation, query: String? = null) =
        showRepository.getAllMovies(operation, query)

    fun tvShows(operation: ServiceOperation, query: String? = null) =
        showRepository.getAllTvShows(operation, query)

    init {
        viewModelScope.launch {
            genreRepository.fetchAllGenres()
        }
    }
}
