package com.oztechan.ccc.client.model

data class Notification(
    val id: Long,
    val base: String,
    val target: String,
    val isGreater: Boolean,
    val rate: String
)
