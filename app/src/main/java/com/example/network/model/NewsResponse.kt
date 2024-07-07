package com.example.network.model

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)