package com.oztechan.ccc.analytics.model

sealed class Param(val key: String) {
    data class Base(val value: String) : Param("base")
}
