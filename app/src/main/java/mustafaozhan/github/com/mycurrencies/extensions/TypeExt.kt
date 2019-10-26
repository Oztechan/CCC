package mustafaozhan.github.com.mycurrencies.extensions

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun String.replaceNonStandardDigits(): String {
    val builder = StringBuilder()
    this.forEach { ch ->
        if (isNonstandardDigit(ch)) {
            val numericValue = Character.getNumericValue(ch)

            if (numericValue >= 0) {
                builder.append(numericValue)
            }
        } else {
            builder.append(ch)
        }
    }
    return builder.toString()
}

private fun isNonstandardDigit(ch: Char): Boolean {
    return Character.isDigit(ch) && ch !in '0'..'9'
}

fun String.replaceUnsupportedCharacters(): String =
    this.replace(",", ".")
        .replace("٫", ".")
        .replace(" ", "")
        .replace("−", "-")

fun Double.getFormatted(): String {
    val symbols = DecimalFormatSymbols.getInstance()
    symbols.groupingSeparator = ' '
    return DecimalFormat("###,###.###", symbols).format(this)
}
