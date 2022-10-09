package com.oztechan.ccc.config.model

data class AppUpdate(
    val name: String,
    val updateLatestVersion: Int,
    val updateForceVersion: Int
)
