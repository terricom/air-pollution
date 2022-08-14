package com.example.airpollution.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

suspend fun <T : Any?> safeApiCall(call: suspend () -> Result<T>): Result<T> {
    return try {
        withContext(Dispatchers.IO) {
            call()
        }
    } catch (e: Throwable) {
        Result.Error(Exception(e.message))
    }
}

fun <T : Any> Response<T>.getResult() = getResult { it }

fun <T : Any, R> Response<T>.getResult(mapping: (T) -> R): Result<R> {
    return if (isSuccessful) {
        val body = body()
        if (body != null) {
            Result.Success(mapping(body))
        } else {
            Result.Error(Exception(body.toString()))
        }
    } else {
        Result.Error(Exception())
    }
}