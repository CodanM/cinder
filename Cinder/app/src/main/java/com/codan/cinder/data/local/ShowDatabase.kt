package com.codan.cinder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codan.cinder.data.local.domain.remotekey.MovieRemoteKey
import com.codan.cinder.data.local.domain.remotekey.TvShowRemoteKey
import com.codan.cinder.data.local.domain.show.Genre
import com.codan.cinder.data.local.domain.show.Movie
import com.codan.cinder.data.local.domain.show.TvShow
import com.codan.cinder.data.local.dao.remotekey.MovieRemoteKeyDao
import com.codan.cinder.data.local.dao.remotekey.TvShowRemoteKeyDao
import com.codan.cinder.data.local.dao.show.genre.GenreDao
import com.codan.cinder.data.local.dao.show.MovieDao
import com.codan.cinder.data.local.dao.show.TvShowDao

@Database(
    entities = [
        Movie::class,
        TvShow::class,
        Genre::class,
        MovieRemoteKey::class,
        TvShowRemoteKey::class
    ],
    version = 1,
)
abstract class ShowDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun tvShowDao(): TvShowDao

    abstract fun genreDao(): GenreDao

    abstract fun movieRemoteKeyDao(): MovieRemoteKeyDao

    abstract fun tvShowRemoteKeyDao(): TvShowRemoteKeyDao
}