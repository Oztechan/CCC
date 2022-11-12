package com.oztechan.ccc.common.di.module.submodule

import kotlinx.coroutines.GlobalScope
import org.koin.dsl.module

internal val scopeModule = module {
    @Suppress("OPT_IN_USAGE")
    single { GlobalScope }
}
