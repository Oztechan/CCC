package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.model.Rates
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.oztechan.ccc.common.api.model.CurrencyResponse as CurrencyResponseEntity
import com.oztechan.ccc.common.api.model.Rates as APIRates
import com.oztechan.ccc.common.database.sql.Rates as DBRates

internal fun DBRates.toRatesEntity() = APIRates(
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

internal fun DBRates?.toCurrencyResponseEntity() = this?.run {
    CurrencyResponseEntity(base, date, toRatesEntity())
}

internal fun CurrencyResponseEntity?.toSerializedString() = Json.encodeToString(this)

internal fun DBRates.toModel() = Rates(
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

internal fun APIRates.toModel() = Rates(
    base, date, aed, afn, all, amd, ang, aoa, ars, aud, awg, azn, bam, bbd, bdt, bgn, bhd, bif,
    bmd, bnd, bob, brl, bsd, btc, btn, bwp, byn, bzd, cad, cdf, chf, clf, clp, cnh, cny, cop,
    crc, cuc, cup, cve, czk, djf, dkk, dop, dzd, egp, ern, etb, eur, fjd, fkp, gbp, gel, ggp,
    ghs, gip, gmd, gnf, gtq, gyd, hkd, hnl, hrk, htg, huf, idr, ils, imp, inr, iqd, irr, isk,
    jep, jmd, jod, jpy, kes, kgs, khr, kmf, kpw, krw, kwd, kyd, kzt, lak, lbp, lkr, lrd, lsl,
    lyd, mad, mdl, mga, mkd, mmk, mnt, mop, mro, mru, mur, mvr, mwk, mxn, myr, mzn, nad, ngn,
    nio, nok, npr, nzd, omr, pab, pen, pgk, php, pkr, pln, pyg, qar, ron, rsd, rub, rwf, sar,
    sbd, scr, sdg, sek, sgd, shp, sll, sos, srd, ssp, std, stn, svc, syp, szl, thb, tjs, tmt,
    tnd, top, `try`, ttd, twd, tzs, uah, ugx, usd, uyu, uzs, ves, vnd, vuv, wst, xaf, xag, xau,
    xcd, xdr, xof, xpd, xpf, xpt, yer, zar, zmw, zwl
)
