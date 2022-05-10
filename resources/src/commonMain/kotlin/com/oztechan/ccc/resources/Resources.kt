package com.oztechan.ccc.resources

fun String.toImageFileName() = lowercase()
    .replace("try", "tryy")
    .ifEmpty { "unknown" }
