package com.oztechan.ccc.common.datasource.conversion.mapper

import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.database.sql.Conversion as ConversionDBModel

internal fun ConversionDBModel.toConversionModel() = Conversion(
    base, date, AED, AFN, ALL, AMD, ANG, AOA, ARS, AUD, AWG, AZN, BAM, BBD, BDT,
    BGN, BHD, BIF, BMD, BND, BOB, BRL, BSD, BTN, BWP, BYN, BZD, CAD, CDF, CHF, CLP,
    CNY, COP, CRC, CUP, CVE, CZK, DJF, DKK, DOP, DZD, EGP, ERN, ETB, EUR, FJD, FKP,
    FOK, GBP, GEL, GGP, GHS, GIP, GMD, GNF, GTQ, GYD, HKD, HNL, HRK, HTG, HUF, IDR,
    ILS, IMP, INR, IQD, IRR, ISK, JEP, JMD, JOD, JPY, KES, KGS, KHR, KID, KMF, KRW,
    KWD, KYD, KZT, LAK, LBP, LKR, LRD, LSL, LYD, MAD, MDL, MGA, MKD, MMK, MNT, MOP,
    MRU, MUR, MVR, MWK, MXN, MYR, MZN, NAD, NGN, NIO, NOK, NPR, NZD, OMR, PAB, PEN,
    PGK, PHP, PKR, PLN, PYG, QAR, RON, RSD, RUB, RWF, SAR, SBD, SCR, SDG, SEK, SGD,
    SHP, SLE, SLL, SOS, SRD, SSP, STN, SYP, SZL, THB, TJS, TMT, TND, TOP, TRY, TTD,
    TVD, TWD, TZS, UAH, UGX, USD, UYU, UZS, VES, VND, VUV, WST, XAF, XCD, XDR, XOF,
    XPF, YER, ZAR, ZMW, ZWL
)

internal fun Conversion.toConversionDBModel() = ConversionDBModel(
    base, date, aed, afn, all, amd, ang, aoa, ars, aud, awg, azn, bam, bbd, bdt, bgn, bhd, bif,
    bmd, bnd, bob, brl, bsd, btn, bwp, byn, bzd, cad, cdf, chf, clp, cny, cop, crc, cup, cve,
    czk, djf, dkk, dop, dzd, egp, ern, etb, eur, fjd, fkp, fok, gbp, gel, ggp, ghs, gip, gmd,
    gnf, gtq, gyd, hkd, hnl, hrk, htg, huf, idr, ils, imp, inr, iqd, irr, isk, jep, jmd, jod,
    jpy, kes, kgs, khr, kid, kmf, krw, kwd, kyd, kzt, lak, lbp, lkr, lrd, lsl, lyd, mad, mdl,
    mga, mkd, mmk, mnt, mop, mru, mur, mvr, mwk, mxn, myr, mzn, nad, ngn, nio, nok, npr, nzd,
    omr, pab, pen, pgk, php, pkr, pln, pyg, qar, ron, rsd, rub, rwf, sar, sbd, scr, sdg, sek,
    sgd, shp, sle, sll, sos, srd, ssp, stn, syp, szl, thb, tjs, tmt, tnd, top, `try`, ttd, tvd,
    twd, tzs, uah, ugx, usd, uyu, uzs, ves, vnd, vuv, wst, xaf, xcd, xdr, xof, xpf, yer, zar,
    zmw, zwl
)
