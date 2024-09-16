package com.oztechan.ccc.common.core.model

data class Watcher(
    val id: Long,
    val source: String,
    val target: String,
    val isGreater: Boolean,
    val rate: Double,
)
