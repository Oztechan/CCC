/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.bookbarter.common.repository

import com.github.mustafaozhan.bookbarter.common.model.APP_NAME
import com.github.mustafaozhan.bookbarter.common.model.Platform
import com.github.mustafaozhan.bookbarter.common.platformName
import com.github.mustafaozhan.bookbarter.common.platformVersion

class PlatformRepository {
    val platform = Platform(
        platformName,
        platformVersion
    )
    val appName = APP_NAME
}
