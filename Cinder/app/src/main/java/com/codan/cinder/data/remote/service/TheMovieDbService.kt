package com.codan.cinder.data.remote.service

import com.codan.cinder.data.local.domain.show.Movie
import com.codan.cinder.data.local.domain.show.TvShow
import com.codan.cinder.data.remote.response.GenreListResponse
import com.codan.cinder.data.remote.response.ShowListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbService {

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "d928b0d9aa3618d79b8b94bc28921886"
    }

    @GET("movie/{movie_id}?api_key=${API_KEY}")
    suspend fun findMovie(@Path("movie_id") id: Long): Movie

    @GET("discover/movie?api_key=${API_KEY}&sort_by=popularity.desc")
    suspend fun discoverMovies(
        @Query("page") page: Int
    ): ShowListResponse<Movie>

    @GET("search/movie?api_key=${API_KEY}")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): ShowListResponse<Movie>

    @GET("tv/{tv_id}?api_key=$API_KEY")
    suspend fun findTvShow(@Path("tv_id") id: Long): TvShow

    @GET("discover/tv?api_key=$API_KEY&sort_by=popularity.desc")
    suspend fun discoverTvShows(
        @Query("page") page: Int
    ): ShowListResponse<TvShow>

    @GET("search/tv?api_key=$API_KEY")
    suspend fun searchTvShows(
        @Query("query") query: String,
        @Query("page") page: Int,
    ): ShowListResponse<TvShow>

    @GET("genre/movie/list?api_key=$API_KEY")
    suspend fun fetchAllMovieGenres(): GenreListResponse

    @GET("genre/tv/list?api_key=$API_KEY")
    suspend fun fetchAllTvShowGenres(): GenreListResponse
}