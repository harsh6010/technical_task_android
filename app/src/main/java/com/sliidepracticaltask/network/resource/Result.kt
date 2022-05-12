package com.sliidepracticaltask.network.resource


sealed class Result<T, F>

data class Success<T, F> internal constructor(val data: T?) :
    Result<T, F>()

data class Failure<T, F> internal constructor(val throwable: Throwable?, val error: F?) :
    Result<T, F>()
