package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.api.model.CurrencyResponse
import com.oztechan.ccc.test.BaseTest
import com.oztechan.ccc.test.util.assertAllTrue
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.oztechan.ccc.common.api.model.Conversion as APIConversion
import com.oztechan.ccc.common.database.sql.Conversion as DBConversion

internal class ConversionTest : BaseTest() {
    private val dbConversion = DBConversion(
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
    fun toConversionEntity() {
        val entity = dbConversion.toConversionEntity()
        assertAllTrue(
            dbConversion.base == entity.base, dbConversion.date == entity.date, dbConversion.AED == entity.aed,
            dbConversion.AFN == entity.afn, dbConversion.ALL == entity.all, dbConversion.AMD == entity.amd,
            dbConversion.ANG == entity.ang, dbConversion.AOA == entity.aoa, dbConversion.ARS == entity.ars,
            dbConversion.AUD == entity.aud, dbConversion.AWG == entity.awg, dbConversion.AZN == entity.azn,
            dbConversion.BAM == entity.bam, dbConversion.BBD == entity.bbd, dbConversion.BDT == entity.bdt,
            dbConversion.BGN == entity.bgn, dbConversion.BHD == entity.bhd, dbConversion.BIF == entity.bif,
            dbConversion.BMD == entity.bmd, dbConversion.BND == entity.bnd, dbConversion.BOB == entity.bob,
            dbConversion.BRL == entity.brl, dbConversion.BSD == entity.bsd, dbConversion.BTC == entity.btc,
            dbConversion.BTN == entity.btn, dbConversion.BWP == entity.bwp, dbConversion.BYN == entity.byn,
            dbConversion.BZD == entity.bzd, dbConversion.CAD == entity.cad, dbConversion.CDF == entity.cdf,
            dbConversion.CHF == entity.chf, dbConversion.CLF == entity.clf, dbConversion.CLP == entity.clp,
            dbConversion.CNH == entity.cnh, dbConversion.CNY == entity.cny, dbConversion.COP == entity.cop,
            dbConversion.CRC == entity.crc, dbConversion.CUC == entity.cuc, dbConversion.CUP == entity.cup,
            dbConversion.CVE == entity.cve, dbConversion.CZK == entity.czk, dbConversion.DJF == entity.djf,
            dbConversion.DKK == entity.dkk, dbConversion.DOP == entity.dop, dbConversion.DZD == entity.dzd,
            dbConversion.EGP == entity.egp, dbConversion.ERN == entity.ern, dbConversion.ETB == entity.etb,
            dbConversion.EUR == entity.eur, dbConversion.FJD == entity.fjd, dbConversion.FKP == entity.fkp,
            dbConversion.GBP == entity.gbp, dbConversion.GEL == entity.gel, dbConversion.GGP == entity.ggp,
            dbConversion.GHS == entity.ghs, dbConversion.GIP == entity.gip, dbConversion.GMD == entity.gmd,
            dbConversion.GNF == entity.gnf, dbConversion.GTQ == entity.gtq, dbConversion.GYD == entity.gyd,
            dbConversion.HKD == entity.hkd, dbConversion.HNL == entity.hnl, dbConversion.HRK == entity.hrk,
            dbConversion.HTG == entity.htg, dbConversion.HUF == entity.huf, dbConversion.IDR == entity.idr,
            dbConversion.ILS == entity.ils, dbConversion.IMP == entity.imp, dbConversion.INR == entity.inr,
            dbConversion.IQD == entity.iqd, dbConversion.IRR == entity.irr, dbConversion.ISK == entity.isk,
            dbConversion.JEP == entity.jep, dbConversion.JMD == entity.jmd, dbConversion.JOD == entity.jod,
            dbConversion.JPY == entity.jpy, dbConversion.KES == entity.kes, dbConversion.KGS == entity.kgs,
            dbConversion.KHR == entity.khr, dbConversion.KMF == entity.kmf, dbConversion.KPW == entity.kpw,
            dbConversion.KRW == entity.krw, dbConversion.KWD == entity.kwd, dbConversion.KYD == entity.kyd,
            dbConversion.KZT == entity.kzt, dbConversion.LAK == entity.lak, dbConversion.LBP == entity.lbp,
            dbConversion.LKR == entity.lkr, dbConversion.LRD == entity.lrd, dbConversion.LSL == entity.lsl,
            dbConversion.LYD == entity.lyd, dbConversion.MAD == entity.mad, dbConversion.MDL == entity.mdl,
            dbConversion.MGA == entity.mga, dbConversion.MKD == entity.mkd, dbConversion.MMK == entity.mmk,
            dbConversion.MNT == entity.mnt, dbConversion.MOP == entity.mop, dbConversion.MRO == entity.mro,
            dbConversion.MRU == entity.mru, dbConversion.MUR == entity.mur, dbConversion.MVR == entity.mvr,
            dbConversion.MWK == entity.mwk, dbConversion.MXN == entity.mxn, dbConversion.MYR == entity.myr,
            dbConversion.MZN == entity.mzn, dbConversion.NAD == entity.nad, dbConversion.NGN == entity.ngn,
            dbConversion.NIO == entity.nio, dbConversion.NOK == entity.nok, dbConversion.NPR == entity.npr,
            dbConversion.NZD == entity.nzd, dbConversion.OMR == entity.omr, dbConversion.PAB == entity.pab,
            dbConversion.PEN == entity.pen, dbConversion.PGK == entity.pgk, dbConversion.PHP == entity.php,
            dbConversion.PKR == entity.pkr, dbConversion.PLN == entity.pln, dbConversion.PYG == entity.pyg,
            dbConversion.QAR == entity.qar, dbConversion.RON == entity.ron, dbConversion.RSD == entity.rsd,
            dbConversion.RUB == entity.rub, dbConversion.RWF == entity.rwf, dbConversion.SAR == entity.sar,
            dbConversion.SBD == entity.sbd, dbConversion.SCR == entity.scr, dbConversion.SDG == entity.sdg,
            dbConversion.SEK == entity.sek, dbConversion.SGD == entity.sgd, dbConversion.SHP == entity.shp,
            dbConversion.SLL == entity.sll, dbConversion.SOS == entity.sos, dbConversion.SRD == entity.srd,
            dbConversion.SSP == entity.ssp, dbConversion.STD == entity.std, dbConversion.STN == entity.stn,
            dbConversion.SVC == entity.svc, dbConversion.SYP == entity.syp, dbConversion.SZL == entity.szl,
            dbConversion.THB == entity.thb, dbConversion.TJS == entity.tjs, dbConversion.TMT == entity.tmt,
            dbConversion.TND == entity.tnd, dbConversion.TOP == entity.top, dbConversion.TRY == entity.`try`,
            dbConversion.TTD == entity.ttd, dbConversion.TWD == entity.twd, dbConversion.TZS == entity.tzs,
            dbConversion.UAH == entity.uah, dbConversion.UGX == entity.ugx, dbConversion.USD == entity.usd,
            dbConversion.UYU == entity.uyu, dbConversion.UZS == entity.uzs, dbConversion.VES == entity.ves,
            dbConversion.VND == entity.vnd, dbConversion.VUV == entity.vuv, dbConversion.WST == entity.wst,
            dbConversion.XAF == entity.xaf, dbConversion.XAG == entity.xag, dbConversion.XAU == entity.xau,
            dbConversion.XCD == entity.xcd, dbConversion.XDR == entity.xdr, dbConversion.XOF == entity.xof,
            dbConversion.XPD == entity.xpd, dbConversion.XPF == entity.xpf, dbConversion.XPT == entity.xpt,
            dbConversion.YER == entity.yer, dbConversion.ZAR == entity.zar, dbConversion.ZMW == entity.zmw,
            dbConversion.ZWL == entity.zwl
        )
    }

    @Suppress("LongMethod")
    @Test
    fun toCurrencyResponseEntity() {
        val response = dbConversion.toCurrencyResponseEntity()

        assertEquals(dbConversion.base, response?.base)
        assertEquals(dbConversion.date, response?.date)
        assertEquals(dbConversion.toConversionEntity(), response?.conversion)
    }

    @Test
    fun toSerializedString() {
        val subject = dbConversion.toCurrencyResponseEntity()
        val serializedString = subject.toSerializedString()
        assertTrue {
            Json.decodeFromString<CurrencyResponse>(
                serializedString
            ) == subject
        }
    }

    @Suppress("LongMethod")
    @Test
    fun dbConversionToModel() {
        val model = dbConversion.toModel()

        assertAllTrue(
            dbConversion.base == model.base, dbConversion.date == model.date, dbConversion.AED == model.aed,
            dbConversion.AFN == model.afn, dbConversion.ALL == model.all, dbConversion.AMD == model.amd,
            dbConversion.ANG == model.ang, dbConversion.AOA == model.aoa, dbConversion.ARS == model.ars,
            dbConversion.AUD == model.aud, dbConversion.AWG == model.awg, dbConversion.AZN == model.azn,
            dbConversion.BAM == model.bam, dbConversion.BBD == model.bbd, dbConversion.BDT == model.bdt,
            dbConversion.BGN == model.bgn, dbConversion.BHD == model.bhd, dbConversion.BIF == model.bif,
            dbConversion.BMD == model.bmd, dbConversion.BND == model.bnd, dbConversion.BOB == model.bob,
            dbConversion.BRL == model.brl, dbConversion.BSD == model.bsd, dbConversion.BTC == model.btc,
            dbConversion.BTN == model.btn, dbConversion.BWP == model.bwp, dbConversion.BYN == model.byn,
            dbConversion.BZD == model.bzd, dbConversion.CAD == model.cad, dbConversion.CDF == model.cdf,
            dbConversion.CHF == model.chf, dbConversion.CLF == model.clf, dbConversion.CLP == model.clp,
            dbConversion.CNH == model.cnh, dbConversion.CNY == model.cny, dbConversion.COP == model.cop,
            dbConversion.CRC == model.crc, dbConversion.CUC == model.cuc, dbConversion.CUP == model.cup,
            dbConversion.CVE == model.cve, dbConversion.CZK == model.czk, dbConversion.DJF == model.djf,
            dbConversion.DKK == model.dkk, dbConversion.DOP == model.dop, dbConversion.DZD == model.dzd,
            dbConversion.EGP == model.egp, dbConversion.ERN == model.ern, dbConversion.ETB == model.etb,
            dbConversion.EUR == model.eur, dbConversion.FJD == model.fjd, dbConversion.FKP == model.fkp,
            dbConversion.GBP == model.gbp, dbConversion.GEL == model.gel, dbConversion.GGP == model.ggp,
            dbConversion.GHS == model.ghs, dbConversion.GIP == model.gip, dbConversion.GMD == model.gmd,
            dbConversion.GNF == model.gnf, dbConversion.GTQ == model.gtq, dbConversion.GYD == model.gyd,
            dbConversion.HKD == model.hkd, dbConversion.HNL == model.hnl, dbConversion.HRK == model.hrk,
            dbConversion.HTG == model.htg, dbConversion.HUF == model.huf, dbConversion.IDR == model.idr,
            dbConversion.ILS == model.ils, dbConversion.IMP == model.imp, dbConversion.INR == model.inr,
            dbConversion.IQD == model.iqd, dbConversion.IRR == model.irr, dbConversion.ISK == model.isk,
            dbConversion.JEP == model.jep, dbConversion.JMD == model.jmd, dbConversion.JOD == model.jod,
            dbConversion.JPY == model.jpy, dbConversion.KES == model.kes, dbConversion.KGS == model.kgs,
            dbConversion.KHR == model.khr, dbConversion.KMF == model.kmf, dbConversion.KPW == model.kpw,
            dbConversion.KRW == model.krw, dbConversion.KWD == model.kwd, dbConversion.KYD == model.kyd,
            dbConversion.KZT == model.kzt, dbConversion.LAK == model.lak, dbConversion.LBP == model.lbp,
            dbConversion.LKR == model.lkr, dbConversion.LRD == model.lrd, dbConversion.LSL == model.lsl,
            dbConversion.LYD == model.lyd, dbConversion.MAD == model.mad, dbConversion.MDL == model.mdl,
            dbConversion.MGA == model.mga, dbConversion.MKD == model.mkd, dbConversion.MMK == model.mmk,
            dbConversion.MNT == model.mnt, dbConversion.MOP == model.mop, dbConversion.MRO == model.mro,
            dbConversion.MRU == model.mru, dbConversion.MUR == model.mur, dbConversion.MVR == model.mvr,
            dbConversion.MWK == model.mwk, dbConversion.MXN == model.mxn, dbConversion.MYR == model.myr,
            dbConversion.MZN == model.mzn, dbConversion.NAD == model.nad, dbConversion.NGN == model.ngn,
            dbConversion.NIO == model.nio, dbConversion.NOK == model.nok, dbConversion.NPR == model.npr,
            dbConversion.NZD == model.nzd, dbConversion.OMR == model.omr, dbConversion.PAB == model.pab,
            dbConversion.PEN == model.pen, dbConversion.PGK == model.pgk, dbConversion.PHP == model.php,
            dbConversion.PKR == model.pkr, dbConversion.PLN == model.pln, dbConversion.PYG == model.pyg,
            dbConversion.QAR == model.qar, dbConversion.RON == model.ron, dbConversion.RSD == model.rsd,
            dbConversion.RUB == model.rub, dbConversion.RWF == model.rwf, dbConversion.SAR == model.sar,
            dbConversion.SBD == model.sbd, dbConversion.SCR == model.scr, dbConversion.SDG == model.sdg,
            dbConversion.SEK == model.sek, dbConversion.SGD == model.sgd, dbConversion.SHP == model.shp,
            dbConversion.SLL == model.sll, dbConversion.SOS == model.sos, dbConversion.SRD == model.srd,
            dbConversion.SSP == model.ssp, dbConversion.STD == model.std, dbConversion.STN == model.stn,
            dbConversion.SVC == model.svc, dbConversion.SYP == model.syp, dbConversion.SZL == model.szl,
            dbConversion.THB == model.thb, dbConversion.TJS == model.tjs, dbConversion.TMT == model.tmt,
            dbConversion.TND == model.tnd, dbConversion.TOP == model.top, dbConversion.TRY == model.`try`,
            dbConversion.TTD == model.ttd, dbConversion.TWD == model.twd, dbConversion.TZS == model.tzs,
            dbConversion.UAH == model.uah, dbConversion.UGX == model.ugx, dbConversion.USD == model.usd,
            dbConversion.UYU == model.uyu, dbConversion.UZS == model.uzs, dbConversion.VES == model.ves,
            dbConversion.VND == model.vnd, dbConversion.VUV == model.vuv, dbConversion.WST == model.wst,
            dbConversion.XAF == model.xaf, dbConversion.XAG == model.xag, dbConversion.XAU == model.xau,
            dbConversion.XCD == model.xcd, dbConversion.XDR == model.xdr, dbConversion.XOF == model.xof,
            dbConversion.XPD == model.xpd, dbConversion.XPF == model.xpf, dbConversion.XPT == model.xpt,
            dbConversion.YER == model.yer, dbConversion.ZAR == model.zar, dbConversion.ZMW == model.zmw,
            dbConversion.ZWL == model.zwl
        )
    }

    @Suppress("LongMethod")
    @Test
    fun apiConversionToModel() {
        val entity = APIConversion()
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
