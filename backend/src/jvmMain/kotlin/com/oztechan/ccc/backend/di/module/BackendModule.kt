package com.oztechan.ccc.backend.di.module

import com.oztechan.ccc.backend.di.module.submodule.repositoryModule

internal val backendModules = buildList {
    add(repositoryModule)
}
