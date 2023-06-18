package com.example.enebronotes.network

import com.example.enebronotes.data.tenor_api.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TenorApiService {
    @GET("search")
    suspend fun getSearch(
        @Query("q") query: String,
        @Query("key") apiKey: String,
        @Query("client_key") clientKey: String,
        @Query("limit") limit: Int,
        @Query("media_filter") mediaFilter: String = "gif"
    ): ApiResponse

    @GET("featured")
    suspend fun getFeatured(
        @Query("key") apiKey: String,
        @Query("client_key") clientKey: String,
        @Query("limit") limit: Int,
        @Query("media_filter") mediaFilter: String = "gif"
    ): ApiResponse
}