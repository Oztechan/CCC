package com.oztechan.ccc.config.di.module

import com.oztechan.ccc.config.di.module.submodule.remoteServiceModule
import org.koin.core.module.Module

val configModules: List<Module> = buildList {
    add(remoteServiceModule)
}
