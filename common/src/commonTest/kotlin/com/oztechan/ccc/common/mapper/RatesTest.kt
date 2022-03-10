package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.entity.RatesEntity
import com.oztechan.ccc.common.util.assertAllTrue
import kotlin.test.Test

class RatesTest {

    @Suppress("LongMethod")
    @Test
    fun toModel() {
        val entity = RatesEntity()
        val model = entity.toModel()
        assertAllTrue(
            entity.base == model.base, entity.date == model.date, entity.aed == model.aed,
            entity.afn == model.afn, entity.all == model.all, entity.amd == model.amd,
            entity.ang == model.ang, entity.aoa == model.aoa, entity.ars == model.ars,
            entity.aud == model.aud, entity.awg == model.awg, entity.azn == model.azn,
            entity.bam == model.bam, entity.bbd == model.bbd, entity.bdt == model.bdt,
            entity.bgn == model.bgn, entity.bhd == model.bhd, entity.bif == model.bif,
            entity.bmd == model.bmd, entity.bnd == model.bnd, entity.bob == model.bob,
            entity.brl == model.brl, entity.bsd == model.bsd, entity.btc == model.btc,
            entity.btn == model.btn, entity.bwp == model.bwp, entity.byn == model.byn,
            entity.bzd == model.bzd, entity.cad == model.cad, entity.cdf == model.cdf,
            entity.chf == model.chf, entity.clf == model.clf, entity.clp == model.clp,
            entity.cnh == model.cnh, entity.cny == model.cny, entity.cop == model.cop,
            entity.crc == model.crc, entity.cuc == model.cuc, entity.cup == model.cup,
            entity.cve == model.cve, entity.czk == model.czk, entity.djf == model.djf,
            entity.dkk == model.dkk, entity.dop == model.dop, entity.dzd == model.dzd,
            entity.egp == model.egp, entity.ern == model.ern, entity.etb == model.etb,
            entity.eur == model.eur, entity.fjd == model.fjd, entity.fkp == model.fkp,
            entity.gbp == model.gbp, entity.gel == model.gel, entity.ggp == model.ggp,
            entity.ghs == model.ghs, entity.gip == model.gip, entity.gmd == model.gmd,
            entity.gnf == model.gnf, entity.gtq == model.gtq, entity.gyd == model.gyd,
            entity.hkd == model.hkd, entity.hnl == model.hnl, entity.hrk == model.hrk,
            entity.htg == model.htg, entity.huf == model.huf, entity.idr == model.idr,
            entity.ils == model.ils, entity.imp == model.imp, entity.inr == model.inr,
            entity.iqd == model.iqd, entity.irr == model.irr, entity.isk == model.isk,
            entity.jep == model.jep, entity.jmd == model.jmd, entity.jod == model.jod,
            entity.jpy == model.jpy, entity.kes == model.kes, entity.kgs == model.kgs,
            entity.khr == model.khr, entity.kmf == model.kmf, entity.kpw == model.kpw,
            entity.krw == model.krw, entity.kwd == model.kwd, entity.kyd == model.kyd,
            entity.kzt == model.kzt, entity.lak == model.lak, entity.lbp == model.lbp,
            entity.lkr == model.lkr, entity.lrd == model.lrd, entity.lsl == model.lsl,
            entity.lyd == model.lyd, entity.mad == model.mad, entity.mdl == model.mdl,
            entity.mga == model.mga, entity.mkd == model.mkd, entity.mmk == model.mmk,
            entity.mnt == model.mnt, entity.mop == model.mop, entity.mro == model.mro,
            entity.mru == model.mru, entity.mur == model.mur, entity.mvr == model.mvr,
            entity.mwk == model.mwk, entity.mxn == model.mxn, entity.myr == model.myr,
            entity.mzn == model.mzn, entity.nad == model.nad, entity.ngn == model.ngn,
            entity.nio == model.nio, entity.nok == model.nok, entity.npr == model.npr,
            entity.nzd == model.nzd, entity.omr == model.omr, entity.pab == model.pab,
            entity.pen == model.pen, entity.pgk == model.pgk, entity.php == model.php,
            entity.pkr == model.pkr, entity.pln == model.pln, entity.pyg == model.pyg,
            entity.qar == model.qar, entity.ron == model.ron, entity.rsd == model.rsd,
            entity.rub == model.rub, entity.rwf == model.rwf, entity.sar == model.sar,
            entity.sbd == model.sbd, entity.scr == model.scr, entity.sdg == model.sdg,
            entity.sek == model.sek, entity.sgd == model.sgd, entity.shp == model.shp,
            entity.sll == model.sll, entity.sos == model.sos, entity.srd == model.srd,
            entity.ssp == model.ssp, entity.std == model.std, entity.stn == model.stn,
            entity.svc == model.svc, entity.syp == model.syp, entity.szl == model.szl,
            entity.thb == model.thb, entity.tjs == model.tjs, entity.tmt == model.tmt,
            entity.tnd == model.tnd, entity.top == model.top, entity.`try` == model.`try`,
            entity.ttd == model.ttd, entity.twd == model.twd, entity.tzs == model.tzs,
            entity.uah == model.uah, entity.ugx == model.ugx, entity.usd == model.usd,
            entity.uyu == model.uyu, entity.uzs == model.uzs, entity.ves == model.ves,
            entity.vnd == model.vnd, entity.vuv == model.vuv, entity.wst == model.wst,
            entity.xaf == model.xaf, entity.xag == model.xag, entity.xau == model.xau,
            entity.xcd == model.xcd, entity.xdr == model.xdr, entity.xof == model.xof,
            entity.xpd == model.xpd, entity.xpf == model.xpf, entity.xpt == model.xpt,
            entity.yer == model.yer, entity.zar == model.zar, entity.zmw == model.zmw,
            entity.zwl == model.zwl
        )
    }
}
