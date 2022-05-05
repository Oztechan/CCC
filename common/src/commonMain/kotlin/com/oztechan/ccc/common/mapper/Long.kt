package com.oztechan.ccc.common.mapper

fun Long.toBoolean() = when (this) {
    1L -> true
    0L -> false
    else -> throw IllegalStateException("Value can not be boolean")
}
