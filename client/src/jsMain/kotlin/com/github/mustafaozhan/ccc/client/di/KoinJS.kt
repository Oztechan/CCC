/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.common.log.kermit
import org.koin.dsl.module
import org.w3c.dom.Storage

fun initJS(storage: Storage) = initClient(
    module { single { storage } }
).also {
    kermit.d { "KoinJS initJS" }
}
