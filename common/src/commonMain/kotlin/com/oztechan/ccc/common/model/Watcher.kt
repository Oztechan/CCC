package com.oztechan.ccc.common.model

data class Watcher(
    val id: Long,
    val base: String,
    val target: String,
    val isGreater: Boolean,
    val rate: Double,
)
