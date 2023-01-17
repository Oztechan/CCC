package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.model.Conversion
import com.oztechan.ccc.test.BaseTest
import com.oztechan.ccc.test.util.assertAllTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.common.core.database.sql.Conversion as ConversionDBModel

internal class ConversionMapperTest : BaseTest() {
    private val dbModel = ConversionDBModel(
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

    @Test
    fun `ConversionDBModel toConversionModel`() {
        val model = dbModel.toConversionModel()
        assertAllConversationRatesWithTheDBModel(model)
    }

    @Test
    fun `ConversionAPIModel toConversionModel`() {
        val model = dbModel.toExchangeRateAPIModel()
            .conversion
            .toConversionModel()

        assertAllConversationRatesWithTheDBModel(model)
    }

    @Test
    fun toExchangeRateAPIModel() {
        val response = dbModel.toExchangeRateAPIModel()

        assertEquals(dbModel.base, response.base)
        assertEquals(dbModel.date, response.date)

        assertAllConversationRatesWithTheDBModel(response.conversion.toConversionModel())
    }

    @Suppress("LongMethod")
    private fun assertAllConversationRatesWithTheDBModel(model: Conversion) = assertAllTrue(
        dbModel.base == model.base, dbModel.date == model.date, dbModel.AED == model.aed,
        dbModel.AFN == model.afn, dbModel.ALL == model.all, dbModel.AMD == model.amd,
        dbModel.ANG == model.ang, dbModel.AOA == model.aoa, dbModel.ARS == model.ars,
        dbModel.AUD == model.aud, dbModel.AWG == model.awg, dbModel.AZN == model.azn,
        dbModel.BAM == model.bam, dbModel.BBD == model.bbd, dbModel.BDT == model.bdt,
        dbModel.BGN == model.bgn, dbModel.BHD == model.bhd, dbModel.BIF == model.bif,
        dbModel.BMD == model.bmd, dbModel.BND == model.bnd, dbModel.BOB == model.bob,
        dbModel.BRL == model.brl, dbModel.BSD == model.bsd, dbModel.BTC == model.btc,
        dbModel.BTN == model.btn, dbModel.BWP == model.bwp, dbModel.BYN == model.byn,
        dbModel.BZD == model.bzd, dbModel.CAD == model.cad, dbModel.CDF == model.cdf,
        dbModel.CHF == model.chf, dbModel.CLF == model.clf, dbModel.CLP == model.clp,
        dbModel.CNH == model.cnh, dbModel.CNY == model.cny, dbModel.COP == model.cop,
        dbModel.CRC == model.crc, dbModel.CUC == model.cuc, dbModel.CUP == model.cup,
        dbModel.CVE == model.cve, dbModel.CZK == model.czk, dbModel.DJF == model.djf,
        dbModel.DKK == model.dkk, dbModel.DOP == model.dop, dbModel.DZD == model.dzd,
        dbModel.EGP == model.egp, dbModel.ERN == model.ern, dbModel.ETB == model.etb,
        dbModel.EUR == model.eur, dbModel.FJD == model.fjd, dbModel.FKP == model.fkp,
        dbModel.GBP == model.gbp, dbModel.GEL == model.gel, dbModel.GGP == model.ggp,
        dbModel.GHS == model.ghs, dbModel.GIP == model.gip, dbModel.GMD == model.gmd,
        dbModel.GNF == model.gnf, dbModel.GTQ == model.gtq, dbModel.GYD == model.gyd,
        dbModel.HKD == model.hkd, dbModel.HNL == model.hnl, dbModel.HRK == model.hrk,
        dbModel.HTG == model.htg, dbModel.HUF == model.huf, dbModel.IDR == model.idr,
        dbModel.ILS == model.ils, dbModel.IMP == model.imp, dbModel.INR == model.inr,
        dbModel.IQD == model.iqd, dbModel.IRR == model.irr, dbModel.ISK == model.isk,
        dbModel.JEP == model.jep, dbModel.JMD == model.jmd, dbModel.JOD == model.jod,
        dbModel.JPY == model.jpy, dbModel.KES == model.kes, dbModel.KGS == model.kgs,
        dbModel.KHR == model.khr, dbModel.KMF == model.kmf, dbModel.KPW == model.kpw,
        dbModel.KRW == model.krw, dbModel.KWD == model.kwd, dbModel.KYD == model.kyd,
        dbModel.KZT == model.kzt, dbModel.LAK == model.lak, dbModel.LBP == model.lbp,
        dbModel.LKR == model.lkr, dbModel.LRD == model.lrd, dbModel.LSL == model.lsl,
        dbModel.LYD == model.lyd, dbModel.MAD == model.mad, dbModel.MDL == model.mdl,
        dbModel.MGA == model.mga, dbModel.MKD == model.mkd, dbModel.MMK == model.mmk,
        dbModel.MNT == model.mnt, dbModel.MOP == model.mop, dbModel.MRO == model.mro,
        dbModel.MRU == model.mru, dbModel.MUR == model.mur, dbModel.MVR == model.mvr,
        dbModel.MWK == model.mwk, dbModel.MXN == model.mxn, dbModel.MYR == model.myr,
        dbModel.MZN == model.mzn, dbModel.NAD == model.nad, dbModel.NGN == model.ngn,
        dbModel.NIO == model.nio, dbModel.NOK == model.nok, dbModel.NPR == model.npr,
        dbModel.NZD == model.nzd, dbModel.OMR == model.omr, dbModel.PAB == model.pab,
        dbModel.PEN == model.pen, dbModel.PGK == model.pgk, dbModel.PHP == model.php,
        dbModel.PKR == model.pkr, dbModel.PLN == model.pln, dbModel.PYG == model.pyg,
        dbModel.QAR == model.qar, dbModel.RON == model.ron, dbModel.RSD == model.rsd,
        dbModel.RUB == model.rub, dbModel.RWF == model.rwf, dbModel.SAR == model.sar,
        dbModel.SBD == model.sbd, dbModel.SCR == model.scr, dbModel.SDG == model.sdg,
        dbModel.SEK == model.sek, dbModel.SGD == model.sgd, dbModel.SHP == model.shp,
        dbModel.SLL == model.sll, dbModel.SOS == model.sos, dbModel.SRD == model.srd,
        dbModel.SSP == model.ssp, dbModel.STD == model.std, dbModel.STN == model.stn,
        dbModel.SVC == model.svc, dbModel.SYP == model.syp, dbModel.SZL == model.szl,
        dbModel.THB == model.thb, dbModel.TJS == model.tjs, dbModel.TMT == model.tmt,
        dbModel.TND == model.tnd, dbModel.TOP == model.top, dbModel.TRY == model.`try`,
        dbModel.TTD == model.ttd, dbModel.TWD == model.twd, dbModel.TZS == model.tzs,
        dbModel.UAH == model.uah, dbModel.UGX == model.ugx, dbModel.USD == model.usd,
        dbModel.UYU == model.uyu, dbModel.UZS == model.uzs, dbModel.VES == model.ves,
        dbModel.VND == model.vnd, dbModel.VUV == model.vuv, dbModel.WST == model.wst,
        dbModel.XAF == model.xaf, dbModel.XAG == model.xag, dbModel.XAU == model.xau,
        dbModel.XCD == model.xcd, dbModel.XDR == model.xdr, dbModel.XOF == model.xof,
        dbModel.XPD == model.xpd, dbModel.XPF == model.xpf, dbModel.XPT == model.xpt,
        dbModel.YER == model.yer, dbModel.ZAR == model.zar, dbModel.ZMW == model.zmw,
        dbModel.ZWL == model.zwl
    )
}
