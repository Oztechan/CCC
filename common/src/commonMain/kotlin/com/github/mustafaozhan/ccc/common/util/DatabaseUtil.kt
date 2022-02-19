package com.github.mustafaozhan.ccc.common.util

fun Boolean.toDatabaseBoolean() = if (this) 1L else 0L
