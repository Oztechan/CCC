package com.oztechan.ccc.common.core.network.mapper

import com.oztechan.ccc.common.core.model.Conversion
import com.oztechan.ccc.common.core.network.model.ExchangeRate

fun ExchangeRate.toConversionModel() = Conversion(
    base, date,
    conversion.aed, conversion.afn, conversion.all, conversion.amd, conversion.ang,
    conversion.aoa, conversion.ars, conversion.aud, conversion.awg, conversion.azn,
    conversion.bam, conversion.bbd, conversion.bdt, conversion.bgn, conversion.bhd,
    conversion.bif, conversion.bmd, conversion.bnd, conversion.bob, conversion.brl,
    conversion.bsd, conversion.btn, conversion.bwp, conversion.byn, conversion.bzd,
    conversion.cad, conversion.cdf, conversion.chf, conversion.clp, conversion.cny,
    conversion.cop, conversion.crc, conversion.cup, conversion.cve, conversion.czk,
    conversion.djf, conversion.dkk, conversion.dop, conversion.dzd, conversion.egp,
    conversion.ern, conversion.etb, conversion.eur, conversion.fjd, conversion.fkp,
    conversion.fok, conversion.gbp, conversion.gel, conversion.ggp, conversion.ghs,
    conversion.gip, conversion.gmd, conversion.gnf, conversion.gtq, conversion.gyd,
    conversion.hkd, conversion.hnl, conversion.hrk, conversion.htg, conversion.huf,
    conversion.idr, conversion.ils, conversion.imp, conversion.inr, conversion.iqd,
    conversion.irr, conversion.isk, conversion.jep, conversion.jmd, conversion.jod,
    conversion.jpy, conversion.kes, conversion.kgs, conversion.khr, conversion.kmf,
    conversion.krw, conversion.kwd, conversion.kyd, conversion.kzt, conversion.lak,
    conversion.lbp, conversion.lkr, conversion.lrd, conversion.lsl, conversion.lyd,
    conversion.mad, conversion.mdl, conversion.mga, conversion.mkd, conversion.mmk,
    conversion.mnt, conversion.mop, conversion.mru, conversion.mur, conversion.mvr,
    conversion.mwk, conversion.mxn, conversion.myr, conversion.mzn, conversion.nad,
    conversion.ngn, conversion.nio, conversion.nok, conversion.npr, conversion.nzd,
    conversion.omr, conversion.pab, conversion.pen, conversion.pgk, conversion.php,
    conversion.pkr, conversion.pln, conversion.pyg, conversion.qar, conversion.ron,
    conversion.rsd, conversion.rub, conversion.rwf, conversion.sar, conversion.sbd,
    conversion.scr, conversion.sdg, conversion.sek, conversion.sgd, conversion.shp,
    conversion.sll, conversion.sos, conversion.srd, conversion.ssp, conversion.stn,
    conversion.syp, conversion.szl, conversion.thb, conversion.tjs, conversion.tmt,
    conversion.tnd, conversion.top, conversion.`try`, conversion.ttd, conversion.twd,
    conversion.tzs, conversion.uah, conversion.ugx, conversion.usd, conversion.uyu,
    conversion.uzs, conversion.ves, conversion.vnd, conversion.vuv, conversion.wst,
    conversion.xaf, conversion.xcd, conversion.xdr, conversion.xof, conversion.xpf,
    conversion.yer, conversion.zar, conversion.zmw, conversion.zwl
)
