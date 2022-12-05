package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.db.sql.Rates
import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.api.model.CurrencyResponse as CurrencyResponseEntity

internal fun CurrencyResponseEntity.toModel(
    fallbackBase: String = base
) = CurrencyResponse(
    base = fallbackBase,
    date = date,
    rates = rates.toModel()
)

internal fun CurrencyResponse.toRates() = Rates(
    base,
    date,
    rates.aed, rates.afn, rates.all, rates.amd, rates.ang, rates.aoa, rates.ars, rates.aud,
    rates.awg, rates.azn, rates.bam, rates.bbd, rates.bdt, rates.bgn, rates.bhd, rates.bif,
    rates.bmd, rates.bnd, rates.bob, rates.brl, rates.bsd, rates.btc, rates.btn, rates.bwp,
    rates.byn, rates.bzd, rates.cad, rates.cdf, rates.chf, rates.clf, rates.clp, rates.cnh,
    rates.cny, rates.cop, rates.crc, rates.cuc, rates.cup, rates.cve, rates.czk, rates.djf,
    rates.dkk, rates.dop, rates.dzd, rates.egp, rates.ern, rates.etb, rates.eur, rates.fjd,
    rates.fkp, rates.gbp, rates.gel, rates.ggp, rates.ghs, rates.gip, rates.gmd, rates.gnf,
    rates.gtq, rates.gyd, rates.hkd, rates.hnl, rates.hrk, rates.htg, rates.huf, rates.idr,
    rates.ils, rates.imp, rates.inr, rates.iqd, rates.irr, rates.isk, rates.jep, rates.jmd,
    rates.jod, rates.jpy, rates.kes, rates.kgs, rates.khr, rates.kmf, rates.kpw, rates.krw,
    rates.kwd, rates.kyd, rates.kzt, rates.lak, rates.lbp, rates.lkr, rates.lrd, rates.lsl,
    rates.lyd, rates.mad, rates.mdl, rates.mga, rates.mkd, rates.mmk, rates.mnt, rates.mop,
    rates.mro, rates.mru, rates.mur, rates.mvr, rates.mwk, rates.mxn, rates.myr, rates.mzn,
    rates.nad, rates.ngn, rates.nio, rates.nok, rates.npr, rates.nzd, rates.omr, rates.pab,
    rates.pen, rates.pgk, rates.php, rates.pkr, rates.pln, rates.pyg, rates.qar, rates.ron,
    rates.rsd, rates.rub, rates.rwf, rates.sar, rates.sbd, rates.scr, rates.sdg, rates.sek,
    rates.sgd, rates.shp, rates.sll, rates.sos, rates.srd, rates.ssp, rates.std, rates.stn,
    rates.svc, rates.syp, rates.szl, rates.thb, rates.tjs, rates.tmt, rates.tnd, rates.top,
    rates.`try`, rates.ttd, rates.twd, rates.tzs, rates.uah, rates.ugx, rates.usd, rates.uyu,
    rates.uzs, rates.ves, rates.vnd, rates.vuv, rates.wst, rates.xaf, rates.xag, rates.xau,
    rates.xcd, rates.xdr, rates.xof, rates.xpd, rates.xpf, rates.xpt, rates.yer, rates.zar,
    rates.zmw, rates.zwl
)
