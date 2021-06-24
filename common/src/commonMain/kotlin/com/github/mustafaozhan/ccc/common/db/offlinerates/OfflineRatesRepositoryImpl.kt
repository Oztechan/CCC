package com.github.mustafaozhan.ccc.common.db.offlinerates

import com.github.mustafaozhan.ccc.common.db.sql.OfflineRatesQueries
import com.github.mustafaozhan.ccc.common.mapper.toCurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.mapper.toModel
import com.github.mustafaozhan.ccc.common.mapper.toSerializedString
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.logmob.kermit

internal class OfflineRatesRepositoryImpl(
    private val offlineRatesQueries: OfflineRatesQueries
) : OfflineRatesRepository {

    override fun insertOfflineRates(rates: Rates) = with(rates) {
        offlineRatesQueries.insertOfflineRates(
            base, date, aed, afn, all, amd, ang, aoa, ars, aud, awg, azn, bam, bbd, bdt, bgn,
            bhd, bif, bmd, bnd, bob, brl, bsd, btc, btn, bwp, byn, bzd, cad, cdf, chf, clf,
            clp, cnh, cny, cop, crc, cuc, cup, cve, czk, djf, dkk, dop, dzd, egp, ern, etb,
            eur, fjd, fkp, gbp, gel, ggp, ghs, gip, gmd, gnf, gtq, gyd, hkd, hnl, hrk, htg,
            huf, idr, ils, imp, inr, iqd, irr, isk, jep, jmd, jod, jpy, kes, kgs, khr, kmf,
            kpw, krw, kwd, kyd, kzt, lak, lbp, lkr, lrd, lsl, lyd, mad, mdl, mga, mkd, mmk,
            mnt, mop, mro, mru, mur, mvr, mwk, mxn, myr, mzn, nad, ngn, nio, nok, npr, nzd,
            omr, pab, pen, pgk, php, pkr, pln, pyg, qar, ron, rsd, rub, rwf, sar, sbd, scr,
            sdg, sek, sgd, shp, sll, sos, srd, ssp, std, stn, svc, syp, szl, thb, tjs, tmt,
            tnd, top, `try`, ttd, twd, tzs, uah, ugx, usd, uyu, uzs, ves, vnd, vuv, wst, xaf,
            xag, xau, xcd, xdr, xof, xpd, xpf, xpt, yer, zar, zmw, zwl
        ).also {
            kermit.d { "OfflineRatesRepositoryImpl insertOfflineRates ${rates.base}" }
        }
    }

    override fun getOfflineRatesByBase(baseName: String) = offlineRatesQueries
        .getOfflineRatesByBase(baseName)
        .executeAsOneOrNull()
        ?.toModel()
        .also { kermit.d { "OfflineRatesRepositoryImpl getOfflineRatesByBase $baseName" } }

    override fun getOfflineCurrencyResponseByBase(baseName: String) = offlineRatesQueries
        .getOfflineRatesByBase(baseName.uppercase())
        .executeAsOneOrNull()
        ?.toCurrencyResponseEntity()
        ?.toSerializedString()
        .also { kermit.d { "OfflineRatesRepositoryImpl getOfflineCurrencyResponseByBase $baseName" } }
}
