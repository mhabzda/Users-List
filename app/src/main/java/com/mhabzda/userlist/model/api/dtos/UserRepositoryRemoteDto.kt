package com.mhabzda.userlist.model.api.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserRepositoryRemoteDto(
    @Json(name = "name")
    val name: String,
)