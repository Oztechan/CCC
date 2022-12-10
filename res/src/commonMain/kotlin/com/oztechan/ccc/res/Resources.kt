package com.oztechan.ccc.res

import dev.icerock.moko.resources.getImageByFileName

internal fun String.toImageFileName() = lowercase()
    .replace("try", "tryy")
    .ifEmpty { "unknown" }

fun getImageResourceByName(name: String) = MR.images.getImageByFileName(name.toImageFileName()) ?: MR.images.unknown
