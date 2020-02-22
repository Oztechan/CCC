package mustafaozhan.github.com.mycurrencies.function.extension

import mustafaozhan.github.com.mycurrencies.function.scope.mapTo
import mustafaozhan.github.com.mycurrencies.function.scope.whether
import mustafaozhan.github.com.mycurrencies.function.scope.whetherNot
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_FORMAT = "HH:mm:ss MM.dd.yyyy"

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

fun String.dropDecimal() = replace(" ", "").let { nonEmpty ->
    nonEmpty.whether { contains(".") }
        ?.substring(0, nonEmpty.indexOf("."))
        ?: run { nonEmpty }
}

fun Date.toFormattedString(): String =
    SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        .format(this)
