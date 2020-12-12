/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.data.platform

import com.github.mustafaozhan.ccc.common.platformName
import com.github.mustafaozhan.ccc.common.platformVersion

class PlatformRepository {
    companion object {
        private const val APP_NAME = "Currency Converter Calculator"
    }

    val platform = PlatformEntity(
        platformName,
        platformVersion
    ).toModel()

    val appName = APP_NAME
}
