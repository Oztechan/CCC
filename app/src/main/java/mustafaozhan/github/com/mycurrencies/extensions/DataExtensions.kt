package mustafaozhan.github.com.mycurrencies.extensions

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import mustafaozhan.github.com.mycurrencies.app.Application
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.room.model.CurrencyJson
import mustafaozhan.github.com.mycurrencies.room.model.Rates
import mustafaozhan.github.com.mycurrencies.tools.Currencies

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
fun calculateResultByCurrency(name: String, value: String, rate: Rates?) =
    if (value.isNotEmpty()) {
        try {
            rate?.getThroughReflection<Double>(name)
                ?.times(value.replaceCommas().toDouble())
                ?: 0.0
        } catch (e: NumberFormatException) {
            val numericValue = replaceNonstandardDigits(value.replaceCommas())

            Crashlytics.logException(e)
            Crashlytics.log(Log.ERROR,
                "NumberFormatException $value to $numericValue",
                "If no crash making numeric is done successfully"
            )

            rate?.getThroughReflection<Double>(name)
                ?.times(numericValue.replaceCommas().toDouble())
                ?: 0.0
        }
    } else {
        0.0
    }

private fun replaceNonstandardDigits(input: String): String {
    val builder = StringBuilder()
    for (i in 0 until input.length) {
        val ch = input[i]
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

fun CurrencyDao.insertInitialCurrencies() {
    Gson().fromJson(Application.instance.assets.open("currencies.json").bufferedReader().use {
        it.readText()
    }, CurrencyJson::class.java).currencies.forEach { currency ->
        this.insertCurrency(Currency(currency.name, currency.longName, currency.symbol))
    }
}

fun MutableList<Currency>?.removeUnUsedCurrencies(): MutableList<Currency>? =
    this?.filterNot {
        it.name == Currencies.BYR.toString() ||
            it.name == Currencies.LVL.toString() ||
            it.name == Currencies.LTL.toString() ||
            it.name == Currencies.ZMK.toString() ||
            it.name == Currencies.CRYPTO_BTC.toString()
    }?.toMutableList()

inline fun <reified T : Any> Any.getThroughReflection(propertyName: String): T? {
    val getterName = "get" + propertyName.capitalize()
    return try {
        javaClass.getMethod(getterName).invoke(this) as? T
    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
        Crashlytics.logException(e)
        null
    }
}