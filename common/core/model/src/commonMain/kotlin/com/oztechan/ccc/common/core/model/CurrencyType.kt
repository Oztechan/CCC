/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.common.core.model

enum class CurrencyType {
    AED, AFN, ALL, AMD, ANG, AOA, ARS, AUD, AWG, AZN, BAM, BBD, BDT, BGN, BHD, BIF, BMD, BND, BOB,
    BRL, BSD, BTN, BWP, BYN, BZD, CAD, CDF, CHF, CLP, CNY, COP, CRC, CUP, CVE, CZK, DJF, DKK, DOP,
    DZD, EGP, ERN, ETB, EUR, FJD, FKP, FOK, GBP, GEL, GGP, GHS, GIP, GMD, GNF, GTQ, GYD, HKD, HNL,
    HRK, HTG, HUF, IDR, ILS, IMP, INR, IQD, IRR, ISK, JEP, JMD, JOD, JPY, KES, KGS, KHR, KID, KMF,
    KRW, KWD, KYD, KZT, LAK, LBP, LKR, LRD, LSL, LYD, MAD, MDL, MGA, MKD, MMK, MNT, MOP, MRU, MUR,
    MVR, MWK, MXN, MYR, MZN, NAD, NGN, NIO, NOK, NPR, NZD, OMR, PAB, PEN, PGK, PHP, PKR, PLN, PYG,
    QAR, RON, RSD, RUB, RWF, SAR, SBD, SCR, SDG, SEK, SGD, SHP, SLE, SLL, SOS, SRD, SSP, STN, SYP,
    SZL, THB, TJS, TMT, TND, TOP, TRY, TTD, TWD, TZS, UAH, UGX, USD, UYU, UZS, VES, VND, VUV, WST,
    XAF, XCD, XDR, XOF, XPF, YER, ZAR, ZMW, ZWL;

    companion object {
        fun getPrimaryCurrencies() = listOf(
            USD, EUR, TRY, INR, AED, COP, THB, CNY, RUB, GBP, MXN, BRL, IDR
        )

        fun getSecondaryCurrencies() = listOf(
            VND, SAR, AUD, ARS, CAD, UZS, AOA, JPY, PKR, PLN, XOF, EGP, ZAR,
            MYR, KZT, CLP, XAF, KRW, MAD, CRC, PHP, LRD, AZN, KWD, DZD, HUF,
        )

        fun getTertiaryCurrencies() = listOf(
            TZS, NGN, GHS, IQD, QAR, CZK, ILS, GEL, BDT, KES, SGD, ZMW, OMR,
            TND, RON, MZN, DKK, KGS, GNF, NPR, CHF, LKR, RSD, UGX, JOD, LAK,
            UAH, SEK, BHD, AMD, GTQ, NZD, PEN, DOP, XPF, UYU, BWP, IRR, HRK
        )

        fun getNonPopularCurrencies() = values().filterNot {
            getPrimaryCurrencies().contains(it) ||
                getSecondaryCurrencies().contains(it) ||
                getTertiaryCurrencies().contains(it)
        }
    }
}
