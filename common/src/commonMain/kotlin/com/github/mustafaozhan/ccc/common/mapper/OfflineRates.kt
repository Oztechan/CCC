package com.github.mustafaozhan.ccc.common.mapper

import com.github.mustafaozhan.ccc.common.entity.CurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.entity.RatesEntity
import com.github.mustafaozhan.ccc.common.model.Rates
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.github.mustafaozhan.ccc.common.db.sql.Offline_rates as OfflineRates

internal fun OfflineRates.toRatesEntity() = RatesEntity(
    base, date, AED, AFN, ALL, AMD, ANG, AOA, ARS, AUD, AWG, AZN, BAM, BBD, BDT,
    BGN, BHD, BIF, BMD, BND, BOB, BRL, BSD, BTC, BTN, BWP, BYN, BZD, CAD, CDF, CHF,
    CLF, CLP, CNH, CNY, COP, CRC, CUC, CUP, CVE, CZK, DJF, DKK, DOP, DZD, EGP, ERN,
    ETB, EUR, FJD, FKP, GBP, GEL, GGP, GHS, GIP, GMD, GNF, GTQ, GYD, HKD, HNL, HRK,
    HTG, HUF, IDR, ILS, IMP, INR, IQD, IRR, ISK, JEP, JMD, JOD, JPY, KES, KGS, KHR,
    KMF, KPW, KRW, KWD, KYD, KZT, LAK, LBP, LKR, LRD, LSL, LYD, MAD, MDL, MGA, MKD,
    MMK, MNT, MOP, MRO, MRU, MUR, MVR, MWK, MXN, MYR, MZN, NAD, NGN, NIO, NOK, NPR,
    NZD, OMR, PAB, PEN, PGK, PHP, PKR, PLN, PYG, QAR, RON, RSD, RUB, RWF, SAR, SBD,
    SCR, SDG, SEK, SGD, SHP, SLL, SOS, SRD, SSP, STD, STN, SVC, SYP, SZL, THB, TJS,
    TMT, TND, TOP, TRY, TTD, TWD, TZS, UAH, UGX, USD, UYU, UZS, VES, VND, VUV, WST,
    XAF, XAG, XAU, XCD, XDR, XOF, XPD, XPF, XPT, YER, ZAR, ZMW, ZWL
)

internal fun OfflineRates?.toCurrencyResponseEntity() = this?.run {
    CurrencyResponseEntity(base, date, toRatesEntity())
}

internal fun CurrencyResponseEntity?.toSerializedString() = Json.encodeToString(this)

internal fun OfflineRates.toModel() = Rates(
    base, date, AED, AFN, ALL, AMD, ANG, AOA, ARS, AUD, AWG, AZN, BAM, BBD, BDT,
    BGN, BHD, BIF, BMD, BND, BOB, BRL, BSD, BTC, BTN, BWP, BYN, BZD, CAD, CDF, CHF,
    CLF, CLP, CNH, CNY, COP, CRC, CUC, CUP, CVE, CZK, DJF, DKK, DOP, DZD, EGP, ERN,
    ETB, EUR, FJD, FKP, GBP, GEL, GGP, GHS, GIP, GMD, GNF, GTQ, GYD, HKD, HNL, HRK,
    HTG, HUF, IDR, ILS, IMP, INR, IQD, IRR, ISK, JEP, JMD, JOD, JPY, KES, KGS, KHR,
    KMF, KPW, KRW, KWD, KYD, KZT, LAK, LBP, LKR, LRD, LSL, LYD, MAD, MDL, MGA, MKD,
    MMK, MNT, MOP, MRO, MRU, MUR, MVR, MWK, MXN, MYR, MZN, NAD, NGN, NIO, NOK, NPR,
    NZD, OMR, PAB, PEN, PGK, PHP, PKR, PLN, PYG, QAR, RON, RSD, RUB, RWF, SAR, SBD,
    SCR, SDG, SEK, SGD, SHP, SLL, SOS, SRD, SSP, STD, STN, SVC, SYP, SZL, THB, TJS,
    TMT, TND, TOP, TRY, TTD, TWD, TZS, UAH, UGX, USD, UYU, UZS, VES, VND, VUV, WST,
    XAF, XAG, XAU, XCD, XDR, XOF, XPD, XPF, XPT, YER, ZAR, ZMW, ZWL
)
