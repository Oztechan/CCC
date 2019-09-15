package mustafaozhan.github.com.mycurrencies.extensions

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

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
