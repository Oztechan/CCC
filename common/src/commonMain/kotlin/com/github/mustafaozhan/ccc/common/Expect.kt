/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import kotlin.coroutines.CoroutineContext

expect val platformName: String
expect val platformVersion: String

expect val platformCoroutineContext: CoroutineContext
