/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.oztechan.ccc.backend.app

import co.touchlab.kermit.Logger
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    Logger.i { "ApplicationKt main" }
    EngineMain.main(args)
}
