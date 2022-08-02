package com.oztechan.ccc.client.di.module

import com.oztechan.ccc.client.di.module.submodules.repositoryModule
import com.oztechan.ccc.client.di.module.submodules.serviceModule
import com.oztechan.ccc.client.di.module.submodules.viewModelModule

val clientModules = listOf(
    viewModelModule,
    repositoryModule,
    serviceModule,
)
