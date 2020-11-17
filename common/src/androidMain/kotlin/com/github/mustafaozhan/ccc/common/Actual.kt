/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common

actual val platformName by lazy { "Android" }
actual val platformVersion = android.os.Build.VERSION.SDK_INT.toString()
