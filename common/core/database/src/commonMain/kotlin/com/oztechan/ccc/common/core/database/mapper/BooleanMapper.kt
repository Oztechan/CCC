package com.oztechan.ccc.common.core.database.mapper

fun Boolean.toLong() = if (this) 1L else 0L
