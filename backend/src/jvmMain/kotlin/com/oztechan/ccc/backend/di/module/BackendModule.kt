package com.oztechan.ccc.backend.di.module

import com.oztechan.ccc.backend.di.module.submodule.controllerModule

internal val backendModules = buildList {
    add(controllerModule)
}
