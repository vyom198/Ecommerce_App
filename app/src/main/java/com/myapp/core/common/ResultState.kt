package com.myapp.core.common

sealed class ResultState<out T : Any> {
    data class Success<out R:Any>(val data:R) : ResultState<R>()
    data class Failure(val exception: Exception) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()

}
