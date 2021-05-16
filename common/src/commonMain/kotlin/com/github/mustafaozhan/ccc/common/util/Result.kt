/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.util

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()

    fun execute(
        success: (T) -> Unit = {},
        error: (Throwable) -> Unit = {},
        complete: () -> Unit = {}
    ) {
        when (this) {
            is Success -> success(data)
            is Error -> error(exception)
        }
        complete()
    }
}
