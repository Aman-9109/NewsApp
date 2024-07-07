package com.example.repositoryInterface

import com.example.network.model.NewsResponse
import retrofit2.Response

interface NewsRepository {


    suspend fun getTopHeadLines (country :String? , category: String?): Response<NewsResponse>

    suspend fun searchNews (search:String?): Response<NewsResponse>
}