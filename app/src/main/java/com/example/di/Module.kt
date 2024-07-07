package com.example.di

import com.example.network.ApiRequest
import com.example.repository.NewsRepositoryImpl
import com.example.repositoryInterface.NewsRepository
import com.example.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun providesRetrofitApi(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

    }

    @Singleton
    @Provides
    fun providesNewsApi(retrofit: Retrofit): ApiRequest {
        return retrofit.create(ApiRequest::class.java)
    }


    @Singleton
    @Provides
    fun providesRepository(api: ApiRequest): NewsRepository {
        return NewsRepositoryImpl(api)
    }


}