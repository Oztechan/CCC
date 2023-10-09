package com.oztechan.ccc.backend.controller.api.mapper

import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.network.model.Conversion as ConversionAPIModel
import com.oztechan.ccc.common.core.network.model.ExchangeRate as ExchangeRateAPIModel

@Suppress("NamedArguments")
internal fun Conversion.toExchangeRateAPIModel() = ExchangeRateAPIModel(
    base,
    date,
    ConversionAPIModel(
        base, date, aed, afn, all, amd, ang, aoa, ars, aud, awg, azn, bam, bbd, bdt, bgn, bhd, bif,
        bmd, bnd, bob, brl, bsd, btn, bwp, byn, bzd, cad, cdf, chf, clp, cny, cop, crc, cup, cve,
        czk, djf, dkk, dop, dzd, egp, ern, etb, eur, fjd, fkp, gbp, gel, ggp, ghs, gip, gmd, gnf,
        gtq, gyd, hkd, hnl, hrk, htg, huf, idr, ils, imp, inr, iqd, irr, isk, jep, jmd, jod, jpy,
        kes, kgs, khr, kmf, krw, kwd, kyd, kzt, lak, lbp, lkr, lrd, lsl, lyd, mad, mdl, mga, mkd,
        mmk, mnt, mop, mru, mur, mvr, mwk, mxn, myr, mzn, nad, ngn, nio, nok, npr, nzd, omr, pab,
        pen, pgk, php, pkr, pln, pyg, qar, ron, rsd, rub, rwf, sar, sbd, scr, sdg, sek, sgd, shp,
        sll, sos, srd, ssp, stn, syp, szl, thb, tjs, tmt, tnd, top, `try`, ttd, twd, tzs, uah, ugx,
        usd, uyu, uzs, ves, vnd, vuv, wst, xaf, xcd, xdr, xof, xpf, yer, zar, zmw, zwl,
    )
)
