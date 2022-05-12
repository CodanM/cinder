package com.codan.cinder.data.local.domain.show

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "genres")
data class Genre(
    @PrimaryKey
    @SerializedName("id")
    val localId: Long,
    val name: String?,
)