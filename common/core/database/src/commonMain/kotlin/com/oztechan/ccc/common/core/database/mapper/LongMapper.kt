package com.oztechan.ccc.common.core.database.mapper

fun Long.toBoolean() = when (this) {
    1L -> true
    0L -> false
    else -> error("Value can not be boolean")
}
