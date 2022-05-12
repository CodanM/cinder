package com.codan.cinder.data.local.domain.show

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter

abstract class Show {

    abstract val id: Long
    abstract val remoteId: Long
    abstract val name: String
    abstract val overview: String
    abstract val genreIds: List<Long>?
    abstract val popularity: Float
    abstract val voteAverage: Float
    abstract val releaseDate: LocalDate?
    abstract val posterPath: String?
    abstract val backdropPath: String?

    class GenreIdsConverter {

        companion object {

            private val gson = Gson()

            @JvmStatic
            @TypeConverter
            fun stringToNullableList(data: String?): List<Long>? {
                if (data == null)
                    return emptyList()

                val listType = object : TypeToken<List<Long>?>() {}.type

                return gson.fromJson(data, listType)
            }

            @JvmStatic
            @TypeConverter
            fun nullableListToString(objects: List<Long>?): String {
                return gson.toJson(objects)
            }
        }
    }

    class ReleaseDateConverter {

        companion object {

            val ReleaseDateDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            @JvmStatic
            @TypeConverter
            fun stringToNullableDate(dateStr: String?): LocalDate? {
                if (dateStr == null)
                    return null

                return LocalDate.parse(dateStr, ReleaseDateDateFormatter)
            }

            @JvmStatic
            @TypeConverter
            fun nullableDateToString(date: LocalDate?): String? {
                if (date == null)
                    return null

                return ReleaseDateDateFormatter.format(date)
            }
        }
    }
}
