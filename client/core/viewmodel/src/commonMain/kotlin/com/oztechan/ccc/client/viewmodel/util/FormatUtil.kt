package com.oztechan.ccc.client.viewmodel.util

const val MAXIMUM_FLOATING_POINT = 9

expect fun Double.getFormatted(precision: Int): String

fun String.toSupportedCharacters() = replace(",", ".")
    .replace("٫", ".")
    .replace(" ", "")
    .replace("−", "-")
