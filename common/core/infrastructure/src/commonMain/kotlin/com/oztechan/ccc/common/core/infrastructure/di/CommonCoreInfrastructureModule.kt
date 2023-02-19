package com.oztechan.ccc.common.core.infrastructure.di

import org.koin.core.module.Module

// names
const val DISPATCHER_IO = "DISPATCHER_IO"
const val DISPATCHER_MAIN = "DISPATCHER_MAIN"
const val DISPATCHER_UNCONFINED = "DISPATCHER_UNCONFINED"
const val DISPATCHER_DEFAULT = "DISPATCHER_DEFAULT"

expect val commonCoreInfrastructureModule: Module
