/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

import co.touchlab.kermit.Logger

expect val platformName: String
expect val platformVersion: String

@Suppress("EmptyDefaultConstructor")
expect class PlatformLogger() : Logger
