package com.example.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.model.NewsResponse
import com.example.repositoryInterface.NewsRepository
import com.example.utils.StateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: NewsRepository) :
    ViewModel() {

    private val _newsResponse = MutableStateFlow<StateHolder<NewsResponse>>(StateHolder.Loading)
    val newsResponse = _newsResponse.asStateFlow()

    private val _searchNews = MutableStateFlow<StateHolder<NewsResponse>>(StateHolder.Loading)
    val searchNews = _searchNews.asStateFlow()


    init {
        viewModelScope.launch {
            getTopHeadLines("in", "business")

        }
    }


    private suspend fun getTopHeadLines(country: String?, category: String?) {
        try {
            _newsResponse.value = StateHolder.Loading
            viewModelScope.launch {
                val response = repository.getTopHeadLines(country, category)
                if (response.body().toString().isNotEmpty()) {
                    _newsResponse.value = StateHolder.Success(response.body()!!)
                } else {
                    _newsResponse.value = StateHolder.Error(error = "No Data Found")

                }
            }
        } catch (e: Exception) {
            _newsResponse.value = StateHolder.Error(e.toString())
        }
    }

    suspend fun searchNews(search: String?) {

        try {
            _newsResponse.value = StateHolder.Loading
            viewModelScope.launch {
                val response = repository.searchNews(search)
                if (response.body().toString().isNotEmpty()) {
                    _searchNews.value = StateHolder.Success(response.body()!!)
                } else {
                    _searchNews.value = StateHolder.Error(error = "No Data Found")

                }
            }
        } catch (e: Exception) {
            _searchNews.value = StateHolder.Error(e.toString())
        }
    }

}