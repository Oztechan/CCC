package com.github.mustafaozhan.ccc.client.di.module

import org.koin.dsl.module
import org.w3c.dom.Storage

fun getJsModule(storage: Storage) = module {
    single { storage }
}
