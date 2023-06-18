package com.example.enebronotes.data.tenor_api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val results: List<Result>
)

@Serializable
data class Result(
    val id: String,
    @SerialName(value = "media_formats")
    val mediaFormats: ResultFormats
) {
    fun toUrl(): String = mediaFormats.gif.url
}

@Serializable
data class ResultFormats(
    val gif: ResultGif
)

@Serializable
data class ResultGif(
    val url: String
)


