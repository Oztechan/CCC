package mustafaozhan.github.com.mycurrencies.extensions

import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import mustafaozhan.github.com.mycurrencies.app.Application
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.main.fragment.model.Rates
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.room.model.CurrencyJson
import mustafaozhan.github.com.mycurrencies.room.model.OfflineRates

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

fun String.replaceCommas(): String =
    this.replace(",", ".")
        .replace("٫", ".")

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

fun Rates.findBase() =
    this::class.java.fields.firstOrNull {
        this.getThroughReflection<Double>(it.name) == 1.0
    }?.name
        ?: "NULL"

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

fun CurrencyResponse.toOfflineRates() = OfflineRates(rates?.findBase().toString(),
    rates?.AED,
    rates?.AFN,
    rates?.ALL,
    rates?.AMD,
    rates?.ANG,
    rates?.AOA,
    rates?.ARS,
    rates?.AUD,
    rates?.AWG,
    rates?.AZN,
    rates?.BAM,
    rates?.BBD,
    rates?.BDT,
    rates?.BGN,
    rates?.BHD,
    rates?.BIF,
    rates?.BMD,
    rates?.BND,
    rates?.BOB,
    rates?.BRL,
    rates?.BSD,
    rates?.BTC,
    rates?.BTN,
    rates?.BWP,
    rates?.BYN,
    rates?.BYR,
    rates?.BZD,
    rates?.CAD,
    rates?.CDF,
    rates?.CHF,
    rates?.CLF,
    rates?.CLP,
    rates?.CNY,
    rates?.COP,
    rates?.CRC,
    rates?.CUC,
    rates?.CUP,
    rates?.CVE,
    rates?.CZK,
    rates?.DJF,
    rates?.DKK,
    rates?.DOP,
    rates?.DZD,
    rates?.EGP,
    rates?.ERN,
    rates?.ETB,
    rates?.EUR,
    rates?.FJD,
    rates?.FKP,
    rates?.GBP,
    rates?.GEL,
    rates?.GGP,
    rates?.GHS,
    rates?.GIP,
    rates?.GMD,
    rates?.GNF,
    rates?.GTQ,
    rates?.GYD,
    rates?.HKD,
    rates?.HNL,
    rates?.HRK,
    rates?.HTG,
    rates?.HUF,
    rates?.IDR,
    rates?.ILS,
    rates?.IMP,
    rates?.INR,
    rates?.IQD,
    rates?.IRR,
    rates?.ISK,
    rates?.JEP,
    rates?.JMD,
    rates?.JOD,
    rates?.JPY,
    rates?.KES,
    rates?.KGS,
    rates?.KHR,
    rates?.KMF,
    rates?.KPW,
    rates?.KRW,
    rates?.KWD,
    rates?.KYD,
    rates?.KZT,
    rates?.LAK,
    rates?.LBP,
    rates?.LKR,
    rates?.LRD,
    rates?.LSL,
    rates?.LTL,
    rates?.LVL,
    rates?.LYD,
    rates?.MAD,
    rates?.MDL,
    rates?.MGA,
    rates?.MKD,
    rates?.MMK,
    rates?.MNT,
    rates?.MOP,
    rates?.MRO,
    rates?.MUR,
    rates?.MVR,
    rates?.MWK,
    rates?.MXN,
    rates?.MYR,
    rates?.MZN,
    rates?.NAD,
    rates?.NGN,
    rates?.NIO,
    rates?.NOK,
    rates?.NPR,
    rates?.NZD,
    rates?.OMR,
    rates?.PAB,
    rates?.PEN,
    rates?.PGK,
    rates?.PHP,
    rates?.PKR,
    rates?.PLN,
    rates?.PYG,
    rates?.QAR,
    rates?.RON,
    rates?.RSD,
    rates?.RUB,
    rates?.RWF,
    rates?.SAR,
    rates?.SBD,
    rates?.SCR,
    rates?.SDG,
    rates?.SEK,
    rates?.SGD,
    rates?.SHP,
    rates?.SLL,
    rates?.SOS,
    rates?.SRD,
    rates?.STD,
    rates?.SVC,
    rates?.SYP,
    rates?.SZL,
    rates?.THB,
    rates?.TJS,
    rates?.TMT,
    rates?.TND,
    rates?.TOP,
    rates?.TRY,
    rates?.TTD,
    rates?.TWD,
    rates?.TZS,
    rates?.UAH,
    rates?.UGX,
    rates?.USD,
    rates?.UYU,
    rates?.UZS,
    rates?.VEF,
    rates?.VND,
    rates?.VUV,
    rates?.WST,
    rates?.XAF,
    rates?.XAG,
    rates?.XAU,
    rates?.XCD,
    rates?.XDR,
    rates?.XOF,
    rates?.XPF,
    rates?.YER,
    rates?.ZAR,
    rates?.ZMK,
    rates?.ZMW,
    rates?.ZWL

)

fun OfflineRates.getRates(): Rates? = Rates(
    AED,
    AFN,
    ALL,
    AMD,
    ANG,
    AOA,
    ARS,
    AUD,
    AWG,
    AZN,
    BAM,
    BBD,
    BDT,
    BGN,
    BHD,
    BIF,
    BMD,
    BND,
    BOB,
    BRL,
    BSD,
    BTC,
    BTN,
    BWP,
    BYN,
    BYR,
    BZD,
    CAD,
    CDF,
    CHF,
    CLF,
    CLP,
    CNY,
    COP,
    CRC,
    CUC,
    CUP,
    CVE,
    CZK,
    DJF,
    DKK,
    DOP,
    DZD,
    EGP,
    ERN,
    ETB,
    EUR,
    FJD,
    FKP,
    GBP,
    GEL,
    GGP,
    GHS,
    GIP,
    GMD,
    GNF,
    GTQ,
    GYD,
    HKD,
    HNL,
    HRK,
    HTG,
    HUF,
    IDR,
    ILS,
    IMP,
    INR,
    IQD,
    IRR,
    ISK,
    JEP,
    JMD,
    JOD,
    JPY,
    KES,
    KGS,
    KHR,
    KMF,
    KPW,
    KRW,
    KWD,
    KYD,
    KZT,
    LAK,
    LBP,
    LKR,
    LRD,
    LSL,
    LTL,
    LVL,
    LYD,
    MAD,
    MDL,
    MGA,
    MKD,
    MMK,
    MNT,
    MOP,
    MRO,
    MUR,
    MVR,
    MWK,
    MXN,
    MYR,
    MZN,
    NAD,
    NGN,
    NIO,
    NOK,
    NPR,
    NZD,
    OMR,
    PAB,
    PEN,
    PGK,
    PHP,
    PKR,
    PLN,
    PYG,
    QAR,
    RON,
    RSD,
    RUB,
    RWF,
    SAR,
    SBD,
    SCR,
    SDG,
    SEK,
    SGD,
    SHP,
    SLL,
    SOS,
    SRD,
    STD,
    SVC,
    SYP,
    SZL,
    THB,
    TJS,
    TMT,
    TND,
    TOP,
    TRY,
    TTD,
    TWD,
    TZS,
    UAH,
    UGX,
    USD,
    UYU,
    UZS,
    VEF,
    VND,
    VUV,
    WST,
    XAF,
    XAG,
    XAU,
    XCD,
    XDR,
    XOF,
    XPF,
    YER,
    ZAR,
    ZMK,
    ZMW,
    ZWL
)