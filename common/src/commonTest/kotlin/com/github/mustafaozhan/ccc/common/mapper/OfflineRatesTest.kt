package com.github.mustafaozhan.ccc.common.mapper

import com.github.mustafaozhan.ccc.common.entity.CurrencyResponseEntity
import com.github.mustafaozhan.ccc.common.util.assertAllTrue
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.github.mustafaozhan.ccc.common.db.sql.Offline_rates as OfflineRates

class OfflineRatesTest {
    private val offline = OfflineRates(
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
        val entity = offline.toRatesEntity()
        assertAllTrue(
            offline.base == entity.base, offline.date == entity.date, offline.AED == entity.aed,
            offline.AFN == entity.afn, offline.ALL == entity.all, offline.AMD == entity.amd,
            offline.ANG == entity.ang, offline.AOA == entity.aoa, offline.ARS == entity.ars,
            offline.AUD == entity.aud, offline.AWG == entity.awg, offline.AZN == entity.azn,
            offline.BAM == entity.bam, offline.BBD == entity.bbd, offline.BDT == entity.bdt,
            offline.BGN == entity.bgn, offline.BHD == entity.bhd, offline.BIF == entity.bif,
            offline.BMD == entity.bmd, offline.BND == entity.bnd, offline.BOB == entity.bob,
            offline.BRL == entity.brl, offline.BSD == entity.bsd, offline.BTC == entity.btc,
            offline.BTN == entity.btn, offline.BWP == entity.bwp, offline.BYN == entity.byn,
            offline.BZD == entity.bzd, offline.CAD == entity.cad, offline.CDF == entity.cdf,
            offline.CHF == entity.chf, offline.CLF == entity.clf, offline.CLP == entity.clp,
            offline.CNH == entity.cnh, offline.CNY == entity.cny, offline.COP == entity.cop,
            offline.CRC == entity.crc, offline.CUC == entity.cuc, offline.CUP == entity.cup,
            offline.CVE == entity.cve, offline.CZK == entity.czk, offline.DJF == entity.djf,
            offline.DKK == entity.dkk, offline.DOP == entity.dop, offline.DZD == entity.dzd,
            offline.EGP == entity.egp, offline.ERN == entity.ern, offline.ETB == entity.etb,
            offline.EUR == entity.eur, offline.FJD == entity.fjd, offline.FKP == entity.fkp,
            offline.GBP == entity.gbp, offline.GEL == entity.gel, offline.GGP == entity.ggp,
            offline.GHS == entity.ghs, offline.GIP == entity.gip, offline.GMD == entity.gmd,
            offline.GNF == entity.gnf, offline.GTQ == entity.gtq, offline.GYD == entity.gyd,
            offline.HKD == entity.hkd, offline.HNL == entity.hnl, offline.HRK == entity.hrk,
            offline.HTG == entity.htg, offline.HUF == entity.huf, offline.IDR == entity.idr,
            offline.ILS == entity.ils, offline.IMP == entity.imp, offline.INR == entity.inr,
            offline.IQD == entity.iqd, offline.IRR == entity.irr, offline.ISK == entity.isk,
            offline.JEP == entity.jep, offline.JMD == entity.jmd, offline.JOD == entity.jod,
            offline.JPY == entity.jpy, offline.KES == entity.kes, offline.KGS == entity.kgs,
            offline.KHR == entity.khr, offline.KMF == entity.kmf, offline.KPW == entity.kpw,
            offline.KRW == entity.krw, offline.KWD == entity.kwd, offline.KYD == entity.kyd,
            offline.KZT == entity.kzt, offline.LAK == entity.lak, offline.LBP == entity.lbp,
            offline.LKR == entity.lkr, offline.LRD == entity.lrd, offline.LSL == entity.lsl,
            offline.LYD == entity.lyd, offline.MAD == entity.mad, offline.MDL == entity.mdl,
            offline.MGA == entity.mga, offline.MKD == entity.mkd, offline.MMK == entity.mmk,
            offline.MNT == entity.mnt, offline.MOP == entity.mop, offline.MRO == entity.mro,
            offline.MRU == entity.mru, offline.MUR == entity.mur, offline.MVR == entity.mvr,
            offline.MWK == entity.mwk, offline.MXN == entity.mxn, offline.MYR == entity.myr,
            offline.MZN == entity.mzn, offline.NAD == entity.nad, offline.NGN == entity.ngn,
            offline.NIO == entity.nio, offline.NOK == entity.nok, offline.NPR == entity.npr,
            offline.NZD == entity.nzd, offline.OMR == entity.omr, offline.PAB == entity.pab,
            offline.PEN == entity.pen, offline.PGK == entity.pgk, offline.PHP == entity.php,
            offline.PKR == entity.pkr, offline.PLN == entity.pln, offline.PYG == entity.pyg,
            offline.QAR == entity.qar, offline.RON == entity.ron, offline.RSD == entity.rsd,
            offline.RUB == entity.rub, offline.RWF == entity.rwf, offline.SAR == entity.sar,
            offline.SBD == entity.sbd, offline.SCR == entity.scr, offline.SDG == entity.sdg,
            offline.SEK == entity.sek, offline.SGD == entity.sgd, offline.SHP == entity.shp,
            offline.SLL == entity.sll, offline.SOS == entity.sos, offline.SRD == entity.srd,
            offline.SSP == entity.ssp, offline.STD == entity.std, offline.STN == entity.stn,
            offline.SVC == entity.svc, offline.SYP == entity.syp, offline.SZL == entity.szl,
            offline.THB == entity.thb, offline.TJS == entity.tjs, offline.TMT == entity.tmt,
            offline.TND == entity.tnd, offline.TOP == entity.top, offline.TRY == entity.`try`,
            offline.TTD == entity.ttd, offline.TWD == entity.twd, offline.TZS == entity.tzs,
            offline.UAH == entity.uah, offline.UGX == entity.ugx, offline.USD == entity.usd,
            offline.UYU == entity.uyu, offline.UZS == entity.uzs, offline.VES == entity.ves,
            offline.VND == entity.vnd, offline.VUV == entity.vuv, offline.WST == entity.wst,
            offline.XAF == entity.xaf, offline.XAG == entity.xag, offline.XAU == entity.xau,
            offline.XCD == entity.xcd, offline.XDR == entity.xdr, offline.XOF == entity.xof,
            offline.XPD == entity.xpd, offline.XPF == entity.xpf, offline.XPT == entity.xpt,
            offline.YER == entity.yer, offline.ZAR == entity.zar, offline.ZMW == entity.zmw,
            offline.ZWL == entity.zwl
        )
    }

    @Suppress("LongMethod")
    @Test
    fun toCurrencyResponseEntity() {
        val response = offline.toCurrencyResponseEntity()

        assertEquals(offline.base, response?.base)
        assertEquals(offline.date, response?.date)
        assertEquals(offline.toRatesEntity(), response?.rates)
    }

    @Test
    fun toSerializedString() {
        val subject = offline.toCurrencyResponseEntity()
        val serializedString = subject.toSerializedString()
        assertTrue {
            Json.decodeFromString<CurrencyResponseEntity>(
                serializedString
            ) == subject
        }
    }

    @Suppress("LongMethod")
    @Test
    fun toModel() {

        val model = offline.toModel()

        assertAllTrue(
            offline.base == model.base, offline.date == model.date, offline.AED == model.aed,
            offline.AFN == model.afn, offline.ALL == model.all, offline.AMD == model.amd,
            offline.ANG == model.ang, offline.AOA == model.aoa, offline.ARS == model.ars,
            offline.AUD == model.aud, offline.AWG == model.awg, offline.AZN == model.azn,
            offline.BAM == model.bam, offline.BBD == model.bbd, offline.BDT == model.bdt,
            offline.BGN == model.bgn, offline.BHD == model.bhd, offline.BIF == model.bif,
            offline.BMD == model.bmd, offline.BND == model.bnd, offline.BOB == model.bob,
            offline.BRL == model.brl, offline.BSD == model.bsd, offline.BTC == model.btc,
            offline.BTN == model.btn, offline.BWP == model.bwp, offline.BYN == model.byn,
            offline.BZD == model.bzd, offline.CAD == model.cad, offline.CDF == model.cdf,
            offline.CHF == model.chf, offline.CLF == model.clf, offline.CLP == model.clp,
            offline.CNH == model.cnh, offline.CNY == model.cny, offline.COP == model.cop,
            offline.CRC == model.crc, offline.CUC == model.cuc, offline.CUP == model.cup,
            offline.CVE == model.cve, offline.CZK == model.czk, offline.DJF == model.djf,
            offline.DKK == model.dkk, offline.DOP == model.dop, offline.DZD == model.dzd,
            offline.EGP == model.egp, offline.ERN == model.ern, offline.ETB == model.etb,
            offline.EUR == model.eur, offline.FJD == model.fjd, offline.FKP == model.fkp,
            offline.GBP == model.gbp, offline.GEL == model.gel, offline.GGP == model.ggp,
            offline.GHS == model.ghs, offline.GIP == model.gip, offline.GMD == model.gmd,
            offline.GNF == model.gnf, offline.GTQ == model.gtq, offline.GYD == model.gyd,
            offline.HKD == model.hkd, offline.HNL == model.hnl, offline.HRK == model.hrk,
            offline.HTG == model.htg, offline.HUF == model.huf, offline.IDR == model.idr,
            offline.ILS == model.ils, offline.IMP == model.imp, offline.INR == model.inr,
            offline.IQD == model.iqd, offline.IRR == model.irr, offline.ISK == model.isk,
            offline.JEP == model.jep, offline.JMD == model.jmd, offline.JOD == model.jod,
            offline.JPY == model.jpy, offline.KES == model.kes, offline.KGS == model.kgs,
            offline.KHR == model.khr, offline.KMF == model.kmf, offline.KPW == model.kpw,
            offline.KRW == model.krw, offline.KWD == model.kwd, offline.KYD == model.kyd,
            offline.KZT == model.kzt, offline.LAK == model.lak, offline.LBP == model.lbp,
            offline.LKR == model.lkr, offline.LRD == model.lrd, offline.LSL == model.lsl,
            offline.LYD == model.lyd, offline.MAD == model.mad, offline.MDL == model.mdl,
            offline.MGA == model.mga, offline.MKD == model.mkd, offline.MMK == model.mmk,
            offline.MNT == model.mnt, offline.MOP == model.mop, offline.MRO == model.mro,
            offline.MRU == model.mru, offline.MUR == model.mur, offline.MVR == model.mvr,
            offline.MWK == model.mwk, offline.MXN == model.mxn, offline.MYR == model.myr,
            offline.MZN == model.mzn, offline.NAD == model.nad, offline.NGN == model.ngn,
            offline.NIO == model.nio, offline.NOK == model.nok, offline.NPR == model.npr,
            offline.NZD == model.nzd, offline.OMR == model.omr, offline.PAB == model.pab,
            offline.PEN == model.pen, offline.PGK == model.pgk, offline.PHP == model.php,
            offline.PKR == model.pkr, offline.PLN == model.pln, offline.PYG == model.pyg,
            offline.QAR == model.qar, offline.RON == model.ron, offline.RSD == model.rsd,
            offline.RUB == model.rub, offline.RWF == model.rwf, offline.SAR == model.sar,
            offline.SBD == model.sbd, offline.SCR == model.scr, offline.SDG == model.sdg,
            offline.SEK == model.sek, offline.SGD == model.sgd, offline.SHP == model.shp,
            offline.SLL == model.sll, offline.SOS == model.sos, offline.SRD == model.srd,
            offline.SSP == model.ssp, offline.STD == model.std, offline.STN == model.stn,
            offline.SVC == model.svc, offline.SYP == model.syp, offline.SZL == model.szl,
            offline.THB == model.thb, offline.TJS == model.tjs, offline.TMT == model.tmt,
            offline.TND == model.tnd, offline.TOP == model.top, offline.TRY == model.`try`,
            offline.TTD == model.ttd, offline.TWD == model.twd, offline.TZS == model.tzs,
            offline.UAH == model.uah, offline.UGX == model.ugx, offline.USD == model.usd,
            offline.UYU == model.uyu, offline.UZS == model.uzs, offline.VES == model.ves,
            offline.VND == model.vnd, offline.VUV == model.vuv, offline.WST == model.wst,
            offline.XAF == model.xaf, offline.XAG == model.xag, offline.XAU == model.xau,
            offline.XCD == model.xcd, offline.XDR == model.xdr, offline.XOF == model.xof,
            offline.XPD == model.xpd, offline.XPF == model.xpf, offline.XPT == model.xpt,
            offline.YER == model.yer, offline.ZAR == model.zar, offline.ZMW == model.zmw,
            offline.ZWL == model.zwl
        )
    }
}
