/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.getImageByFileName

/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

fun getDrawableByFileName(name: String): ImageResource {
    return MR.images.getImageByFileName(name) ?: MR.images.transparent
}

fun getString(stringResource: StringResource): StringDesc {
    return StringDesc.Resource(stringResource)
}
