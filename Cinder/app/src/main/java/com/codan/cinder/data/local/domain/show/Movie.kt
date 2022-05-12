package com.codan.cinder.data.local.domain.show

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

@Entity(tableName = "movies")
@TypeConverters(
    Show.GenreIdsConverter::class,
    Show.ReleaseDateConverter::class
)
data class Movie(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("localId")
    override val id: Long = 0,
    @SerializedName("id")
    override val remoteId: Long = 0,
    @SerializedName("title")
    override val name: String = "",
    @SerializedName("overview")
    override val overview: String = "",
    @SerializedName("genre_ids")
    override val genreIds: List<Long>? = emptyList(),
    @SerializedName("popularity")
    override val popularity: Float = 0f,
    @SerializedName("vote_average")
    override val voteAverage: Float = 0f,
    @SerializedName("release_date")
    override val releaseDate: LocalDate? = null,
    @SerializedName("poster_path")
    override val posterPath: String? = null,
    @SerializedName("backdrop_path")
    override val backdropPath: String? = null,
    @SerializedName("adult")
    val adult: Boolean = false,
) : Show()