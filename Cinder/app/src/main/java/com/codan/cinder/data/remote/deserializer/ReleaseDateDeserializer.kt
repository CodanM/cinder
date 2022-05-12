package com.codan.cinder.data.remote.deserializer

import com.codan.cinder.data.local.domain.show.Show
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate

class ReleaseDateDeserializer : JsonDeserializer<LocalDate?> {

    private val gson = Gson()

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ): LocalDate? {
        if (gson.fromJson(json, String::class.java).isNullOrBlank())
            return null

        return LocalDate.parse(
            gson.fromJson(json, String::class.java),
            Show.ReleaseDateConverter.ReleaseDateDateFormatter
        )
    }

}