package com.codan.cinder.data.local.dao.show.genre

import androidx.room.Dao
import androidx.room.Query
import com.codan.cinder.data.local.dao.CrudDao
import com.codan.cinder.data.local.domain.show.Genre
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao : CrudDao<Long, Genre> {

    @Query("SELECT * FROM genres")
    override fun getAll(): Flow<List<Genre>>

    @Query("DELETE FROM genres")
    override suspend fun deleteAll()
}