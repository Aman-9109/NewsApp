package com.example.network

import com.example.network.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {

    @GET("top-headlines")
    suspend fun getTopHeadLines(
        @Query("country") country: String? = "in",
        @Query("category") category: String? = "business",
        @Query("apiKey") apiKey: String = "d7d5efbfa4204b94887e5d9d3caf02b0",
    ): Response<NewsResponse>




    //GET /everything?q=bitcoin&apiKey=d7d5efbfa4204b94887e5d9d3caf02b0

    @GET("everything")
    suspend fun searchNews(
        @Query("q") search:String? = null,
        @Query("apiKey") apiKey: String = "d7d5efbfa4204b94887e5d9d3caf02b0",
    ): Response<NewsResponse>

}