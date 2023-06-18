package com.example.enebronotes.data.tenor_api

import com.example.enebronotes.network.TenorApiService

interface TenorApiRepository {
    suspend fun getSearch(
        query: String,
        apiKey: String,
        clientKey: String,
        limit: Int = 50,
        mediaFilter: String = "gif"
    ): ApiResponse

    suspend fun getFeatured(
        apiKey: String,
        clientKey: String,
        limit: Int = 50,
        mediaFilter: String = "gif"
    ): ApiResponse
}

class NetworkTenorApiRepository(
    private val tenorApiService: TenorApiService
) : TenorApiRepository {
    override suspend fun getSearch(
        query: String,
        apiKey: String,
        clientKey: String,
        limit: Int,
        mediaFilter: String
    ): ApiResponse = tenorApiService.getSearch(query, apiKey, clientKey, limit, mediaFilter)

    override suspend fun getFeatured(
        apiKey: String,
        clientKey: String,
        limit: Int,
        mediaFilter: String
    ): ApiResponse = tenorApiService.getFeatured(apiKey, clientKey, limit, mediaFilter)
}
