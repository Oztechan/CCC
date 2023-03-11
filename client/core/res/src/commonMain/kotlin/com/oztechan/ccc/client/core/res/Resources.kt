package com.oztechan.ccc.client.core.res

import dev.icerock.moko.resources.getImageByFileName

internal fun String.toImageFileName() = lowercase()
    .replace("try", "tryy")
    .ifEmpty { "unknown" }

fun getImageByName(name: String) = Res.images.getImageByFileName(name.toImageFileName()) ?: Res.images.unknown
