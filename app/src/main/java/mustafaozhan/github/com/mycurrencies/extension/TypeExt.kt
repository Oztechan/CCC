package mustafaozhan.github.com.mycurrencies.extension

import mustafaozhan.github.com.mycurrencies.function.mapTo
import mustafaozhan.github.com.mycurrencies.function.whether
import mustafaozhan.github.com.mycurrencies.function.whetherNot
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun String.replaceNonStandardDigits(): String {
    val builder = StringBuilder()
    forEach { char ->
        char.whether { Character.isDigit(it) }
            ?.whetherNot { it in '0'..'9' }
            ?.mapTo { Character.getNumericValue(it) }
            ?.whether { it >= 0 }
            ?.let { builder.append(it) }
            ?: run { builder.append(char) }
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
    .whether { contains(".") }
    ?.substring(0, indexOf("."))
    ?: run { this }
