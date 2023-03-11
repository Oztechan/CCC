package com.oztechan.ccc.client.core.shared.util

const val MAXIMUM_FLOATING_POINT = 9

expect fun Double.getFormatted(precision: Int): String

fun String.toSupportedCharacters() = replace(",", ".")
    .replace("٫", ".")
    .replace(" ", "")
    .replace("−", "-")

fun String.toStandardDigits(): String {
    val builder = StringBuilder()
    forEach { char ->
        char.toString().toIntOrNull().let {
            if (it != null && it >= 0) {
                builder.append(it)
            } else {
                builder.append(char)
            }
        }
    }
    return builder.toString()
}
