package com.oztechan.ccc.common.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

val scopeModule = module {
    @Suppress("OPT_IN_USAGE")
    single<CoroutineScope> { GlobalScope }
}
