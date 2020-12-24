/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.di

import com.github.mustafaozhan.ccc.common.log.kermit
import java.util.prefs.Preferences
import org.koin.dsl.module

fun initJVM(delegate: Preferences) = initClient(
    module { single { delegate } }
).also {
    kermit.d { "KoinJVM initJVM" }
}
