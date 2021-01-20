/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client

import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.ResourceStringDesc
import dev.icerock.moko.resources.desc.StringDesc

fun getStringDesc(): StringDesc {
    return StringDesc.Resource(MR.strings.app_name)
}

fun getResourceStringDesc(): ResourceStringDesc {
    return StringDesc.Resource(MR.strings.app_name)
}
