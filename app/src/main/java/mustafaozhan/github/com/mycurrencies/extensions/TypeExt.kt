package mustafaozhan.github.com.mycurrencies.extensions

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun String.replaceNonStandardDigits(): String {
    val builder = StringBuilder()
    this.forEach { char ->
        char.whether { Character.isDigit(it) }
            .whetherNot { it in '0'..'9' }
            .let { c ->
                Character.getNumericValue(char)
                    .whether { it >= 0 }
                    ?.let { builder.append(c) }
                    ?: run { builder.append(char) }
            }
    }
    return builder.toString()
}

fun String.replaceUnsupportedCharacters() =
    replace(",", ".")
        .replace("٫", ".")
        .replace(" ", "")
        .replace("−", "-")

fun String.toPercent() = replace("%", "/100*")

fun Double.getFormatted(): String {
    val symbols = DecimalFormatSymbols.getInstance()
    symbols.groupingSeparator = ' '
    return DecimalFormat("###,###.###", symbols).format(this)
}

fun String.dropDecimal() = replace(" ", "")
    .whetherThis { contains(".") }
    .apply { substring(0, indexOf(".")) }
    ?: run { this }
