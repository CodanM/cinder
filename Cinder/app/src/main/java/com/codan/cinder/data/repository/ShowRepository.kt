package com.codan.cinder.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.codan.cinder.data.local.domain.remotekey.MovieRemoteKey
import com.codan.cinder.data.local.domain.remotekey.TvShowRemoteKey
import com.codan.cinder.data.local.domain.show.Movie
import com.codan.cinder.data.local.domain.show.TvShow
import com.codan.cinder.data.remote.service.TheMovieDbService
import com.codan.cinder.data.local.ShowDatabase
import com.codan.cinder.data.local.paging.TheMovieDbRemoteMediator
import com.codan.cinder.data.remote.service.ServiceOperation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShowRepository @Inject constructor(
    private val database: ShowDatabase,
    private val service: TheMovieDbService
) {
    companion object {

        private const val PAGE_SIZE = 20

        private const val MAX_PAGES = 10

        private val ShowPagingConfig = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = 2 * PAGE_SIZE,
            prefetchDistance = PAGE_SIZE,
            maxSize = MAX_PAGES * PAGE_SIZE,
            jumpThreshold = MAX_PAGES * PAGE_SIZE,
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Suppress("UNCHECKED_CAST")
    fun getAllMovies(operation: ServiceOperation, query: String? = null): Flow<PagingData<Movie>> {
        return Pager(
            config = ShowPagingConfig,
            remoteMediator = TheMovieDbRemoteMediator(
                database = database,
                showClass = Movie::class,
                remoteKeyClass = MovieRemoteKey::class,
                showDao = database.movieDao(),
                showRemoteKeyDao = database.movieRemoteKeyDao(),
                service = service,
                operation = operation,
                query = query,
            ),
            pagingSourceFactory = { database.movieDao().getAll() }
        ).flow
    }


    @OptIn(ExperimentalPagingApi::class)
    @Suppress("UNCHECKED_CAST")
    fun getAllTvShows(operation: ServiceOperation, query: String? = null): Flow<PagingData<TvShow>> {
        return Pager(
            config = ShowPagingConfig,
            remoteMediator = TheMovieDbRemoteMediator(
                database = database,
                showClass = TvShow::class,
                remoteKeyClass = TvShowRemoteKey::class,
                showDao = database.tvShowDao(),
                showRemoteKeyDao = database.tvShowRemoteKeyDao(),
                service = service,
                operation = operation,
                query = query,
            ),
            pagingSourceFactory = { database.tvShowDao().getAll() }
        ).flow
    }
}