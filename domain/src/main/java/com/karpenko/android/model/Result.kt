@file:Suppress("unused", "IntroduceWhenSubject")

package com.karpenko.android.model

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()

    data class Error<out T : Any>(val error: ErrorResult, val data: T? = null) : Result<T>()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[exception=${error.throwable}"
        }
    }

    fun isFinished() = this is Success || this is Error

    fun isSuccess() = this is Success

    fun isError() = this is Error

    /**
     * Returns the encapsulated value if this instance represents [Success] state
     */
    fun getOrNull() = when {
        this is Success -> data
        this is Error -> data
        else -> null
    }

    /**
     * Returns the encapsulated error if this instance represents [Error]
     */
    fun errorOrNull() = when {
        this is Error -> error
        else -> null
    }
}

open class ErrorResult(open var message: String, open var throwable: Throwable? = null)

/**
 * Wrap a suspending [call] in try/catch. In case an exception is thrown, a [Result.Error] is
 * created based on the [errorMessage].
 */
suspend fun <T : Any> safeCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> {
    return try {
        call()
    } catch (e: Throwable) {
        Result.Error(ErrorResult(errorMessage, e))
    }
}