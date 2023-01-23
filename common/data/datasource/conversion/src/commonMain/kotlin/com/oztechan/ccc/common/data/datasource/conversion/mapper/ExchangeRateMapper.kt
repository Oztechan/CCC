package com.oztechan.ccc.common.data.datasource.conversion.mapper

import com.oztechan.ccc.common.core.database.sql.Conversion as ConversionDBModel
import com.oztechan.ccc.common.core.model.ExchangeRate as ExchangeRateModel

internal fun ExchangeRateModel.toConversionDBModel() = ConversionDBModel(
    base,
    date,
    conversion.aed, conversion.afn, conversion.all, conversion.amd, conversion.ang, conversion.aoa, conversion.ars,
    conversion.aud, conversion.awg, conversion.azn, conversion.bam, conversion.bbd, conversion.bdt, conversion.bgn,
    conversion.bhd, conversion.bif, conversion.bmd, conversion.bnd, conversion.bob, conversion.brl, conversion.bsd,
    conversion.btc, conversion.btn, conversion.bwp, conversion.byn, conversion.bzd, conversion.cad, conversion.cdf,
    conversion.chf, conversion.clf, conversion.clp, conversion.cnh, conversion.cny, conversion.cop, conversion.crc,
    conversion.cuc, conversion.cup, conversion.cve, conversion.czk, conversion.djf, conversion.dkk, conversion.dop,
    conversion.dzd, conversion.egp, conversion.ern, conversion.etb, conversion.eur, conversion.fjd, conversion.fkp,
    conversion.gbp, conversion.gel, conversion.ggp, conversion.ghs, conversion.gip, conversion.gmd, conversion.gnf,
    conversion.gtq, conversion.gyd, conversion.hkd, conversion.hnl, conversion.hrk, conversion.htg, conversion.huf,
    conversion.idr, conversion.ils, conversion.imp, conversion.inr, conversion.iqd, conversion.irr, conversion.isk,
    conversion.jep, conversion.jmd, conversion.jod, conversion.jpy, conversion.kes, conversion.kgs, conversion.khr,
    conversion.kmf, conversion.kpw, conversion.krw, conversion.kwd, conversion.kyd, conversion.kzt, conversion.lak,
    conversion.lbp, conversion.lkr, conversion.lrd, conversion.lsl, conversion.lyd, conversion.mad, conversion.mdl,
    conversion.mga, conversion.mkd, conversion.mmk, conversion.mnt, conversion.mop, conversion.mro, conversion.mru,
    conversion.mur, conversion.mvr, conversion.mwk, conversion.mxn, conversion.myr, conversion.mzn, conversion.nad,
    conversion.ngn, conversion.nio, conversion.nok, conversion.npr, conversion.nzd, conversion.omr, conversion.pab,
    conversion.pen, conversion.pgk, conversion.php, conversion.pkr, conversion.pln, conversion.pyg, conversion.qar,
    conversion.ron, conversion.rsd, conversion.rub, conversion.rwf, conversion.sar, conversion.sbd, conversion.scr,
    conversion.sdg, conversion.sek, conversion.sgd, conversion.shp, conversion.sll, conversion.sos, conversion.srd,
    conversion.ssp, conversion.std, conversion.stn, conversion.svc, conversion.syp, conversion.szl, conversion.thb,
    conversion.tjs, conversion.tmt, conversion.tnd, conversion.top, conversion.`try`, conversion.ttd, conversion.twd,
    conversion.tzs, conversion.uah, conversion.ugx, conversion.usd, conversion.uyu, conversion.uzs, conversion.ves,
    conversion.vnd, conversion.vuv, conversion.wst, conversion.xaf, conversion.xag, conversion.xau, conversion.xcd,
    conversion.xdr, conversion.xof, conversion.xpd, conversion.xpf, conversion.xpt, conversion.yer, conversion.zar,
    conversion.zmw, conversion.zwl
)
