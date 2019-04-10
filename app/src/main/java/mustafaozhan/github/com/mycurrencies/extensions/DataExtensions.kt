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

//fun Rates.findBase() =
//    this::class.java.fields.firstOrNull {
//        this.getThroughReflection<Double>(it.name) == 1.0
//    }?.name
//        ?: "NULL"

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

fun CurrencyResponse.toOfflineRates() = OfflineRates(
    rates?.findBase().toString(),
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

fun Rates.findBase(): String {
    return when {
        AED == 1.0 || AED == null -> "AED"
        AFN == 1.0 || AFN == null -> "AFN"
        ALL == 1.0 || ALL == null -> "ALL"
        AMD == 1.0 || AMD == null -> "AMD"
        ANG == 1.0 || ANG == null -> "ANG"
        AOA == 1.0 || AOA == null -> "AOA"
        ARS == 1.0 || ARS == null -> "ARS"
        AUD == 1.0 || AUD == null -> "AUD"
        AWG == 1.0 || AWG == null -> "AWG"
        AZN == 1.0 || AZN == null -> "AZN"
        BAM == 1.0 || BAM == null -> "BAM"
        BBD == 1.0 || BBD == null -> "BBD"
        BDT == 1.0 || BDT == null -> "BDT"
        BGN == 1.0 || BGN == null -> "BGN"
        BHD == 1.0 || BHD == null -> "BHD"
        BIF == 1.0 || BIF == null -> "BIF"
        BMD == 1.0 || BMD == null -> "BMD"
        BND == 1.0 || BND == null -> "BND"
        BOB == 1.0 || BOB == null -> "BOB"
        BRL == 1.0 || BRL == null -> "BRL"
        BSD == 1.0 || BSD == null -> "BSD"
        BTC == 1.0 || BTC == null -> "BTC"
        BTN == 1.0 || BTN == null -> "BTN"
        BWP == 1.0 || BWP == null -> "BWP"
        BYN == 1.0 || BYN == null -> "BYN"
        BYR == 1.0 || BYR == null -> "BYR"
        BZD == 1.0 || BZD == null -> "BZD"
        CAD == 1.0 || CAD == null -> "CAD"
        CDF == 1.0 || CDF == null -> "CDF"
        CHF == 1.0 || CHF == null -> "CHF"
        CLF == 1.0 || CLF == null -> "CLF"
        CLP == 1.0 || CLP == null -> "CLP"
        CNY == 1.0 || CNY == null -> "CNY"
        COP == 1.0 || COP == null -> "COP"
        CRC == 1.0 || CRC == null -> "CRC"
        CUC == 1.0 || CUC == null -> "CUC"
        CUP == 1.0 || CUP == null -> "CUP"
        CVE == 1.0 || CVE == null -> "CVE"
        CZK == 1.0 || CZK == null -> "CZK"
        DJF == 1.0 || DJF == null -> "DJF"
        DKK == 1.0 || DKK == null -> "DKK"
        DOP == 1.0 || DOP == null -> "DOP"
        DZD == 1.0 || DZD == null -> "DZD"
        EGP == 1.0 || EGP == null -> "EGP"
        ERN == 1.0 || ERN == null -> "ERN"
        ETB == 1.0 || ETB == null -> "ETB"
        EUR == 1.0 || EUR == null -> "EUR"
        FJD == 1.0 || FJD == null -> "FJD"
        FKP == 1.0 || FKP == null -> "FKP"
        GBP == 1.0 || GBP == null -> "GBP"
        GEL == 1.0 || GEL == null -> "GEL"
        GGP == 1.0 || GGP == null -> "GGP"
        GHS == 1.0 || GHS == null -> "GHS"
        GIP == 1.0 || GIP == null -> "GIP"
        GMD == 1.0 || GMD == null -> "GMD"
        GNF == 1.0 || GNF == null -> "GNF"
        GTQ == 1.0 || GTQ == null -> "GTQ"
        GYD == 1.0 || GYD == null -> "GYD"
        HKD == 1.0 || HKD == null -> "HKD"
        HNL == 1.0 || HNL == null -> "HNL"
        HRK == 1.0 || HRK == null -> "HRK"
        HTG == 1.0 || HTG == null -> "HTG"
        HUF == 1.0 || HUF == null -> "HUF"
        IDR == 1.0 || IDR == null -> "IDR"
        ILS == 1.0 || ILS == null -> "ILS"
        IMP == 1.0 || IMP == null -> "IMP"
        INR == 1.0 || INR == null -> "INR"
        IQD == 1.0 || IQD == null -> "IQD"
        IRR == 1.0 || IRR == null -> "IRR"
        ISK == 1.0 || ISK == null -> "ISK"
        JEP == 1.0 || JEP == null -> "JEP"
        JMD == 1.0 || JMD == null -> "JMD"
        JOD == 1.0 || JOD == null -> "JOD"
        JPY == 1.0 || JPY == null -> "JPY"
        KES == 1.0 || KES == null -> "KES"
        KGS == 1.0 || KGS == null -> "KGS"
        KHR == 1.0 || KHR == null -> "KHR"
        KMF == 1.0 || KMF == null -> "KMF"
        KPW == 1.0 || KPW == null -> "KPW"
        KRW == 1.0 || KRW == null -> "KRW"
        KWD == 1.0 || KWD == null -> "KWD"
        KYD == 1.0 || KYD == null -> "KYD"
        KZT == 1.0 || KZT == null -> "KZT"
        LAK == 1.0 || LAK == null -> "LAK"
        LBP == 1.0 || LBP == null -> "LBP"
        LKR == 1.0 || LKR == null -> "LKR"
        LRD == 1.0 || LRD == null -> "LRD"
        LSL == 1.0 || LSL == null -> "LSL"
        LTL == 1.0 || LTL == null -> "LTL"
        LVL == 1.0 || LVL == null -> "LVL"
        LYD == 1.0 || LYD == null -> "LYD"
        MAD == 1.0 || MAD == null -> "MAD"
        MDL == 1.0 || MDL == null -> "MDL"
        MGA == 1.0 || MGA == null -> "MGA"
        MKD == 1.0 || MKD == null -> "MKD"
        MMK == 1.0 || MMK == null -> "MMK"
        MNT == 1.0 || MNT == null -> "MNT"
        MOP == 1.0 || MOP == null -> "MOP"
        MRO == 1.0 || MRO == null -> "MRO"
        MUR == 1.0 || MUR == null -> "MUR"
        MVR == 1.0 || MVR == null -> "MVR"
        MWK == 1.0 || MWK == null -> "MWK"
        MXN == 1.0 || MXN == null -> "MXN"
        MYR == 1.0 || MYR == null -> "MYR"
        MZN == 1.0 || MZN == null -> "MZN"
        NAD == 1.0 || NAD == null -> "NAD"
        NGN == 1.0 || NGN == null -> "NGN"
        NIO == 1.0 || NIO == null -> "NIO"
        NOK == 1.0 || NOK == null -> "NOK"
        NPR == 1.0 || NPR == null -> "NPR"
        NZD == 1.0 || NZD == null -> "NZD"
        OMR == 1.0 || OMR == null -> "OMR"
        PAB == 1.0 || PAB == null -> "PAB"
        PEN == 1.0 || PEN == null -> "PEN"
        PGK == 1.0 || PGK == null -> "PGK"
        PHP == 1.0 || PHP == null -> "PHP"
        PKR == 1.0 || PKR == null -> "PKR"
        PLN == 1.0 || PLN == null -> "PLN"
        PYG == 1.0 || PYG == null -> "PYG"
        QAR == 1.0 || QAR == null -> "QAR"
        RON == 1.0 || RON == null -> "RON"
        RSD == 1.0 || RSD == null -> "RSD"
        RUB == 1.0 || RUB == null -> "RUB"
        RWF == 1.0 || RWF == null -> "RWF"
        SAR == 1.0 || SAR == null -> "SAR"
        SBD == 1.0 || SBD == null -> "SBD"
        SCR == 1.0 || SCR == null -> "SCR"
        SDG == 1.0 || SDG == null -> "SDG"
        SEK == 1.0 || SEK == null -> "SEK"
        SGD == 1.0 || SGD == null -> "SGD"
        SHP == 1.0 || SHP == null -> "SHP"
        SLL == 1.0 || SLL == null -> "SLL"
        SOS == 1.0 || SOS == null -> "SOS"
        SRD == 1.0 || SRD == null -> "SRD"
        STD == 1.0 || STD == null -> "STD"
        SVC == 1.0 || SVC == null -> "SVC"
        SYP == 1.0 || SYP == null -> "SYP"
        SZL == 1.0 || SZL == null -> "SZL"
        THB == 1.0 || THB == null -> "THB"
        TJS == 1.0 || TJS == null -> "TJS"
        TMT == 1.0 || TMT == null -> "TMT"
        TND == 1.0 || TND == null -> "TND"
        TOP == 1.0 || TOP == null -> "TOP"
        TRY == 1.0 || TRY == null -> "TRY"
        TTD == 1.0 || TTD == null -> "TTD"
        TWD == 1.0 || TWD == null -> "TWD"
        TZS == 1.0 || TZS == null -> "TZS"
        UAH == 1.0 || UAH == null -> "UAH"
        UGX == 1.0 || UGX == null -> "UGX"
        USD == 1.0 || USD == null -> "USD"
        UYU == 1.0 || UYU == null -> "UYU"
        UZS == 1.0 || UZS == null -> "UZS"
        VEF == 1.0 || VEF == null -> "VEF"
        VND == 1.0 || VND == null -> "VND"
        VUV == 1.0 || VUV == null -> "VUV"
        WST == 1.0 || WST == null -> "WST"
        XAF == 1.0 || XAF == null -> "XAF"
        XAG == 1.0 || XAG == null -> "XAG"
        XAU == 1.0 || XAU == null -> "XAU"
        XCD == 1.0 || XCD == null -> "XCD"
        XDR == 1.0 || XDR == null -> "XDR"
        XOF == 1.0 || XOF == null -> "XOF"
        XPF == 1.0 || XPF == null -> "XPF"
        YER == 1.0 || YER == null -> "YER"
        ZAR == 1.0 || ZAR == null -> "ZAR"
        ZMK == 1.0 || ZMK == null -> "ZMK"
        ZMW == 1.0 || ZMW == null -> "ZMW"
        ZWL == 1.0 || ZWL == null -> "ZWL"

        else -> "NULL"
    }
}