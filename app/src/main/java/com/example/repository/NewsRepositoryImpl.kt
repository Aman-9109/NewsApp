package com.example.repository

import com.example.network.ApiRequest
import com.example.network.model.NewsResponse
import com.example.repositoryInterface.NewsRepository
import retrofit2.Response
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(val api :ApiRequest ): NewsRepository {
    override suspend fun getTopHeadLines(country :String? , category: String?): Response<NewsResponse> {
        return api.getTopHeadLines(country,category)
    }

    override suspend fun searchNews(search:String?): Response<NewsResponse> {
        return api.searchNews(search)
    }
}