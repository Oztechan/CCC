/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.common.repository

import com.github.mustafaozhan.ccc.common.model.APP_NAME
import com.github.mustafaozhan.ccc.common.model.Platform
import com.github.mustafaozhan.ccc.common.platformName
import com.github.mustafaozhan.ccc.common.platformVersion

class PlatformRepository {
    val platform = Platform(
        platformName,
        platformVersion
    )
    val appName = APP_NAME
}
