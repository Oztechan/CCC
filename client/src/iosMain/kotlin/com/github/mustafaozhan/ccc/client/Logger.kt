package com.github.mustafaozhan.ccc.client

import com.github.mustafaozhan.logmob.initLogger

@Suppress("unused")
fun initLogger() = initLogger().also {
    it.d { "Logger initialized" }
}
