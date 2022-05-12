package com.codan.cinder.data.local.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.codan.cinder.data.local.domain.show.Show
import com.codan.cinder.data.local.domain.remotekey.ShowRemoteKey
import com.codan.cinder.data.local.domain.remotekey.ShowRemoteKeyFactory
import com.codan.cinder.data.local.domain.show.Movie
import com.codan.cinder.data.local.domain.show.TvShow
import com.codan.cinder.data.local.ShowDatabase
import com.codan.cinder.data.local.dao.remotekey.ShowRemoteKeyDao
import com.codan.cinder.data.local.dao.show.ShowDao
import com.codan.cinder.data.remote.service.ServiceOperation
import com.codan.cinder.data.remote.response.ShowListResponse
import com.codan.cinder.data.remote.service.TheMovieDbService
import java.lang.Exception
import javax.inject.Inject
import kotlin.reflect.KClass

@OptIn(ExperimentalPagingApi::class)
class TheMovieDbRemoteMediator<S : Show, K : ShowRemoteKey<S>> @Inject constructor(
    private val database: ShowDatabase,
    private val showClass: KClass<S>,
    private val remoteKeyClass: KClass<K>,
    private val showDao: ShowDao<S>,
    private val showRemoteKeyDao: ShowRemoteKeyDao<S, K>,
    private val service: TheMovieDbService,
    private val operation: ServiceOperation,
    private val query: String? = null,
) : RemoteMediator<Int, S>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, S>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state, showRemoteKeyDao)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state, showRemoteKeyDao)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state, showRemoteKeyDao)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = when (operation) {
                ServiceOperation.Discover -> discover(currentPage)
                ServiceOperation.Search -> search(
                    query = query ?: throw Exception("Query cannot be null!"),
                    page = currentPage
                )
            }
            val results = response.results
            val endOfPaginationReached = results.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    showDao.deleteAll()
                    showRemoteKeyDao.deleteAll()
                }
                results.map { show ->
                    ShowRemoteKeyFactory.getRemoteKey(
                        remoteKeyClass = remoteKeyClass,
                        id = show.remoteId,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }.let { showRemoteKeyDao.insertAll(it) }
                showDao.insertAll(results)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun discover(page: Int): ShowListResponse<S> =
        when (showClass) {
            Movie::class -> service.discoverMovies(page)
            TvShow::class -> service.discoverTvShows(page)
            else -> throw Exception("Invalid type!")
        } as ShowListResponse<S>

    @Suppress("UNCHECKED_CAST")
    private suspend fun search(query:String, page: Int): ShowListResponse<S> =
        when (showClass) {
            Movie::class -> service.searchMovies(query, page)
            TvShow::class -> service.searchTvShows(query, page)
            else -> throw Exception("Invalid type!")
        } as ShowListResponse<S>

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, S>,
        remoteKeyDao: ShowRemoteKeyDao<S, K>
    ): K? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.remoteId?.let { id ->
                remoteKeyDao.find(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, S>,
        remoteKeyDao: ShowRemoteKeyDao<S, K>
    ): K? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { show ->
                remoteKeyDao.find(id = show.remoteId)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, S>,
        remoteKeyDao: ShowRemoteKeyDao<S, K>
    ): K? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { show ->
                remoteKeyDao.find(id = show.remoteId)
            }
    }
}