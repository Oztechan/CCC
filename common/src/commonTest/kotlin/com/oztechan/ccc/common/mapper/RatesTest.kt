package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.api.model.CurrencyResponse
import com.oztechan.ccc.test.BaseTest
import com.oztechan.ccc.test.util.assertAllTrue
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.oztechan.ccc.common.api.model.Rates as APIRates
import com.oztechan.ccc.common.database.sql.Rates as DBRates

internal class RatesTest : BaseTest() {
    private val dbRates = DBRates(
        "base", "12.12.21", 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0,
        13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0, 21.0, 22.0, 23.0, 24.0, 25.0, 26.0,
        27.0, 28.0, 29.0, 30.0, 31.0, 32.0, 33.0, 34.0, 35.0, 36.0, 37.0, 38.0, 39.0, 40.0,
        41.0, 42.0, 43.0, 44.0, 45.0, 46.0, 47.0, 48.0, 49.0, 50.0, 51.0, 52.0, 53.0, 54.0,
        55.0, 56.0, 57.0, 58.0, 59.0, 60.0, 61.0, 62.0, 63.0, 64.0, 65.0, 66.0, 67.0, 68.0,
        69.0, 70.0, 71.0, 72.0, 73.0, 74.0, 75.0, 76.0, 77.0, 78.0, 79.0, 80.0, 81.0, 82.0,
        83.0, 84.0, 85.0, 86.0, 87.0, 88.0, 89.0, 90.0, 91.0, 92.0, 93.0, 94.0, 95.0, 96.0,
        97.0, 98.0, 99.0, 100.0, 101.0, 102.0, 103.0, 104.0, 105.0, 106.0, 107.0, 108.0, 109.0,
        110.0, 111.0, 112.0, 113.0, 114.0, 115.0, 116.0, 117.0, 118.0, 119.0, 120.0, 121.0,
        122.0, 123.0, 124.0, 125.0, 126.0, 127.0, 128.0, 129.0, 130.0, 131.0, 132.0, 133.0,
        134.0, 135.0, 136.0, 137.0, 138.0, 139.0, 140.0, 141.0, 142.0, 143.0, 144.0, 145.0,
        146.0, 147.0, 148.0, 149.0, 150.0, 151.0, 152.0, 153.0, 154.0, 155.0, 156.0, 157.0,
        158.0, 159.0, 160.0, 161.0, 162.0, 163.0, 164.0, 165.0, 166.0, 167.0, 168.0, 169.0, 170.0
    )

    @Suppress("LongMethod")
    @Test
    fun toRatesEntity() {
        val entity = dbRates.toRatesEntity()
        assertAllTrue(
            dbRates.base == entity.base, dbRates.date == entity.date, dbRates.AED == entity.aed,
            dbRates.AFN == entity.afn, dbRates.ALL == entity.all, dbRates.AMD == entity.amd,
            dbRates.ANG == entity.ang, dbRates.AOA == entity.aoa, dbRates.ARS == entity.ars,
            dbRates.AUD == entity.aud, dbRates.AWG == entity.awg, dbRates.AZN == entity.azn,
            dbRates.BAM == entity.bam, dbRates.BBD == entity.bbd, dbRates.BDT == entity.bdt,
            dbRates.BGN == entity.bgn, dbRates.BHD == entity.bhd, dbRates.BIF == entity.bif,
            dbRates.BMD == entity.bmd, dbRates.BND == entity.bnd, dbRates.BOB == entity.bob,
            dbRates.BRL == entity.brl, dbRates.BSD == entity.bsd, dbRates.BTC == entity.btc,
            dbRates.BTN == entity.btn, dbRates.BWP == entity.bwp, dbRates.BYN == entity.byn,
            dbRates.BZD == entity.bzd, dbRates.CAD == entity.cad, dbRates.CDF == entity.cdf,
            dbRates.CHF == entity.chf, dbRates.CLF == entity.clf, dbRates.CLP == entity.clp,
            dbRates.CNH == entity.cnh, dbRates.CNY == entity.cny, dbRates.COP == entity.cop,
            dbRates.CRC == entity.crc, dbRates.CUC == entity.cuc, dbRates.CUP == entity.cup,
            dbRates.CVE == entity.cve, dbRates.CZK == entity.czk, dbRates.DJF == entity.djf,
            dbRates.DKK == entity.dkk, dbRates.DOP == entity.dop, dbRates.DZD == entity.dzd,
            dbRates.EGP == entity.egp, dbRates.ERN == entity.ern, dbRates.ETB == entity.etb,
            dbRates.EUR == entity.eur, dbRates.FJD == entity.fjd, dbRates.FKP == entity.fkp,
            dbRates.GBP == entity.gbp, dbRates.GEL == entity.gel, dbRates.GGP == entity.ggp,
            dbRates.GHS == entity.ghs, dbRates.GIP == entity.gip, dbRates.GMD == entity.gmd,
            dbRates.GNF == entity.gnf, dbRates.GTQ == entity.gtq, dbRates.GYD == entity.gyd,
            dbRates.HKD == entity.hkd, dbRates.HNL == entity.hnl, dbRates.HRK == entity.hrk,
            dbRates.HTG == entity.htg, dbRates.HUF == entity.huf, dbRates.IDR == entity.idr,
            dbRates.ILS == entity.ils, dbRates.IMP == entity.imp, dbRates.INR == entity.inr,
            dbRates.IQD == entity.iqd, dbRates.IRR == entity.irr, dbRates.ISK == entity.isk,
            dbRates.JEP == entity.jep, dbRates.JMD == entity.jmd, dbRates.JOD == entity.jod,
            dbRates.JPY == entity.jpy, dbRates.KES == entity.kes, dbRates.KGS == entity.kgs,
            dbRates.KHR == entity.khr, dbRates.KMF == entity.kmf, dbRates.KPW == entity.kpw,
            dbRates.KRW == entity.krw, dbRates.KWD == entity.kwd, dbRates.KYD == entity.kyd,
            dbRates.KZT == entity.kzt, dbRates.LAK == entity.lak, dbRates.LBP == entity.lbp,
            dbRates.LKR == entity.lkr, dbRates.LRD == entity.lrd, dbRates.LSL == entity.lsl,
            dbRates.LYD == entity.lyd, dbRates.MAD == entity.mad, dbRates.MDL == entity.mdl,
            dbRates.MGA == entity.mga, dbRates.MKD == entity.mkd, dbRates.MMK == entity.mmk,
            dbRates.MNT == entity.mnt, dbRates.MOP == entity.mop, dbRates.MRO == entity.mro,
            dbRates.MRU == entity.mru, dbRates.MUR == entity.mur, dbRates.MVR == entity.mvr,
            dbRates.MWK == entity.mwk, dbRates.MXN == entity.mxn, dbRates.MYR == entity.myr,
            dbRates.MZN == entity.mzn, dbRates.NAD == entity.nad, dbRates.NGN == entity.ngn,
            dbRates.NIO == entity.nio, dbRates.NOK == entity.nok, dbRates.NPR == entity.npr,
            dbRates.NZD == entity.nzd, dbRates.OMR == entity.omr, dbRates.PAB == entity.pab,
            dbRates.PEN == entity.pen, dbRates.PGK == entity.pgk, dbRates.PHP == entity.php,
            dbRates.PKR == entity.pkr, dbRates.PLN == entity.pln, dbRates.PYG == entity.pyg,
            dbRates.QAR == entity.qar, dbRates.RON == entity.ron, dbRates.RSD == entity.rsd,
            dbRates.RUB == entity.rub, dbRates.RWF == entity.rwf, dbRates.SAR == entity.sar,
            dbRates.SBD == entity.sbd, dbRates.SCR == entity.scr, dbRates.SDG == entity.sdg,
            dbRates.SEK == entity.sek, dbRates.SGD == entity.sgd, dbRates.SHP == entity.shp,
            dbRates.SLL == entity.sll, dbRates.SOS == entity.sos, dbRates.SRD == entity.srd,
            dbRates.SSP == entity.ssp, dbRates.STD == entity.std, dbRates.STN == entity.stn,
            dbRates.SVC == entity.svc, dbRates.SYP == entity.syp, dbRates.SZL == entity.szl,
            dbRates.THB == entity.thb, dbRates.TJS == entity.tjs, dbRates.TMT == entity.tmt,
            dbRates.TND == entity.tnd, dbRates.TOP == entity.top, dbRates.TRY == entity.`try`,
            dbRates.TTD == entity.ttd, dbRates.TWD == entity.twd, dbRates.TZS == entity.tzs,
            dbRates.UAH == entity.uah, dbRates.UGX == entity.ugx, dbRates.USD == entity.usd,
            dbRates.UYU == entity.uyu, dbRates.UZS == entity.uzs, dbRates.VES == entity.ves,
            dbRates.VND == entity.vnd, dbRates.VUV == entity.vuv, dbRates.WST == entity.wst,
            dbRates.XAF == entity.xaf, dbRates.XAG == entity.xag, dbRates.XAU == entity.xau,
            dbRates.XCD == entity.xcd, dbRates.XDR == entity.xdr, dbRates.XOF == entity.xof,
            dbRates.XPD == entity.xpd, dbRates.XPF == entity.xpf, dbRates.XPT == entity.xpt,
            dbRates.YER == entity.yer, dbRates.ZAR == entity.zar, dbRates.ZMW == entity.zmw,
            dbRates.ZWL == entity.zwl
        )
    }

    @Suppress("LongMethod")
    @Test
    fun toCurrencyResponseEntity() {
        val response = dbRates.toCurrencyResponseEntity()

        assertEquals(dbRates.base, response?.base)
        assertEquals(dbRates.date, response?.date)
        assertEquals(dbRates.toRatesEntity(), response?.rates)
    }

    @Test
    fun toSerializedString() {
        val subject = dbRates.toCurrencyResponseEntity()
        val serializedString = subject.toSerializedString()
        assertTrue {
            Json.decodeFromString<CurrencyResponse>(
                serializedString
            ) == subject
        }
    }

    @Suppress("LongMethod")
    @Test
    fun dbRatestoModel() {
        val model = dbRates.toModel()

        assertAllTrue(
            dbRates.base == model.base, dbRates.date == model.date, dbRates.AED == model.aed,
            dbRates.AFN == model.afn, dbRates.ALL == model.all, dbRates.AMD == model.amd,
            dbRates.ANG == model.ang, dbRates.AOA == model.aoa, dbRates.ARS == model.ars,
            dbRates.AUD == model.aud, dbRates.AWG == model.awg, dbRates.AZN == model.azn,
            dbRates.BAM == model.bam, dbRates.BBD == model.bbd, dbRates.BDT == model.bdt,
            dbRates.BGN == model.bgn, dbRates.BHD == model.bhd, dbRates.BIF == model.bif,
            dbRates.BMD == model.bmd, dbRates.BND == model.bnd, dbRates.BOB == model.bob,
            dbRates.BRL == model.brl, dbRates.BSD == model.bsd, dbRates.BTC == model.btc,
            dbRates.BTN == model.btn, dbRates.BWP == model.bwp, dbRates.BYN == model.byn,
            dbRates.BZD == model.bzd, dbRates.CAD == model.cad, dbRates.CDF == model.cdf,
            dbRates.CHF == model.chf, dbRates.CLF == model.clf, dbRates.CLP == model.clp,
            dbRates.CNH == model.cnh, dbRates.CNY == model.cny, dbRates.COP == model.cop,
            dbRates.CRC == model.crc, dbRates.CUC == model.cuc, dbRates.CUP == model.cup,
            dbRates.CVE == model.cve, dbRates.CZK == model.czk, dbRates.DJF == model.djf,
            dbRates.DKK == model.dkk, dbRates.DOP == model.dop, dbRates.DZD == model.dzd,
            dbRates.EGP == model.egp, dbRates.ERN == model.ern, dbRates.ETB == model.etb,
            dbRates.EUR == model.eur, dbRates.FJD == model.fjd, dbRates.FKP == model.fkp,
            dbRates.GBP == model.gbp, dbRates.GEL == model.gel, dbRates.GGP == model.ggp,
            dbRates.GHS == model.ghs, dbRates.GIP == model.gip, dbRates.GMD == model.gmd,
            dbRates.GNF == model.gnf, dbRates.GTQ == model.gtq, dbRates.GYD == model.gyd,
            dbRates.HKD == model.hkd, dbRates.HNL == model.hnl, dbRates.HRK == model.hrk,
            dbRates.HTG == model.htg, dbRates.HUF == model.huf, dbRates.IDR == model.idr,
            dbRates.ILS == model.ils, dbRates.IMP == model.imp, dbRates.INR == model.inr,
            dbRates.IQD == model.iqd, dbRates.IRR == model.irr, dbRates.ISK == model.isk,
            dbRates.JEP == model.jep, dbRates.JMD == model.jmd, dbRates.JOD == model.jod,
            dbRates.JPY == model.jpy, dbRates.KES == model.kes, dbRates.KGS == model.kgs,
            dbRates.KHR == model.khr, dbRates.KMF == model.kmf, dbRates.KPW == model.kpw,
            dbRates.KRW == model.krw, dbRates.KWD == model.kwd, dbRates.KYD == model.kyd,
            dbRates.KZT == model.kzt, dbRates.LAK == model.lak, dbRates.LBP == model.lbp,
            dbRates.LKR == model.lkr, dbRates.LRD == model.lrd, dbRates.LSL == model.lsl,
            dbRates.LYD == model.lyd, dbRates.MAD == model.mad, dbRates.MDL == model.mdl,
            dbRates.MGA == model.mga, dbRates.MKD == model.mkd, dbRates.MMK == model.mmk,
            dbRates.MNT == model.mnt, dbRates.MOP == model.mop, dbRates.MRO == model.mro,
            dbRates.MRU == model.mru, dbRates.MUR == model.mur, dbRates.MVR == model.mvr,
            dbRates.MWK == model.mwk, dbRates.MXN == model.mxn, dbRates.MYR == model.myr,
            dbRates.MZN == model.mzn, dbRates.NAD == model.nad, dbRates.NGN == model.ngn,
            dbRates.NIO == model.nio, dbRates.NOK == model.nok, dbRates.NPR == model.npr,
            dbRates.NZD == model.nzd, dbRates.OMR == model.omr, dbRates.PAB == model.pab,
            dbRates.PEN == model.pen, dbRates.PGK == model.pgk, dbRates.PHP == model.php,
            dbRates.PKR == model.pkr, dbRates.PLN == model.pln, dbRates.PYG == model.pyg,
            dbRates.QAR == model.qar, dbRates.RON == model.ron, dbRates.RSD == model.rsd,
            dbRates.RUB == model.rub, dbRates.RWF == model.rwf, dbRates.SAR == model.sar,
            dbRates.SBD == model.sbd, dbRates.SCR == model.scr, dbRates.SDG == model.sdg,
            dbRates.SEK == model.sek, dbRates.SGD == model.sgd, dbRates.SHP == model.shp,
            dbRates.SLL == model.sll, dbRates.SOS == model.sos, dbRates.SRD == model.srd,
            dbRates.SSP == model.ssp, dbRates.STD == model.std, dbRates.STN == model.stn,
            dbRates.SVC == model.svc, dbRates.SYP == model.syp, dbRates.SZL == model.szl,
            dbRates.THB == model.thb, dbRates.TJS == model.tjs, dbRates.TMT == model.tmt,
            dbRates.TND == model.tnd, dbRates.TOP == model.top, dbRates.TRY == model.`try`,
            dbRates.TTD == model.ttd, dbRates.TWD == model.twd, dbRates.TZS == model.tzs,
            dbRates.UAH == model.uah, dbRates.UGX == model.ugx, dbRates.USD == model.usd,
            dbRates.UYU == model.uyu, dbRates.UZS == model.uzs, dbRates.VES == model.ves,
            dbRates.VND == model.vnd, dbRates.VUV == model.vuv, dbRates.WST == model.wst,
            dbRates.XAF == model.xaf, dbRates.XAG == model.xag, dbRates.XAU == model.xau,
            dbRates.XCD == model.xcd, dbRates.XDR == model.xdr, dbRates.XOF == model.xof,
            dbRates.XPD == model.xpd, dbRates.XPF == model.xpf, dbRates.XPT == model.xpt,
            dbRates.YER == model.yer, dbRates.ZAR == model.zar, dbRates.ZMW == model.zmw,
            dbRates.ZWL == model.zwl
        )
    }

    @Suppress("LongMethod")
    @Test
    fun apiRatestoModel() {
        val entity = APIRates()
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
