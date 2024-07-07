package com.example.utils

sealed class StateHolder<out T> {

    object Loading : StateHolder<Nothing>()
    class Error(val error: String) : StateHolder<Nothing>()
    class Success<T>( val data: T) : StateHolder<T>()


}