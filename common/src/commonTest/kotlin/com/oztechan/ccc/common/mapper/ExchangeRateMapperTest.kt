package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.test.BaseTest
import com.oztechan.ccc.test.util.assertAllTrue
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.oztechan.ccc.common.core.network.model.Conversion as ConversionAPIModel
import com.oztechan.ccc.common.core.network.model.ExchangeRate as ExchangeRateAPIModel

internal class ExchangeRateMapperTest : BaseTest() {
    private val conversionAPIModel = ConversionAPIModel(
        "base", "01.12.21", 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0,
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
        158.0, 159.0, 160.0, 161.0, 162.0, 163.0, 164.0, 165.0, 166.0, 167.0, 168.0, 169.0,
        170.0
    )

    private val apiModel = ExchangeRateAPIModel("EUR", "21.12.21", conversionAPIModel)

    @Suppress("LongMethod")
    @Test
    fun toExchangeRateModel() {
        val model = apiModel.toExchangeRateModel()

        assertEquals(apiModel.base, model.base)
        assertEquals(apiModel.date, model.date)
        assertEquals(apiModel.conversion.toConversionModel(), model.conversion)
    }

    @Suppress("LongMethod")
    @Test
    fun toConversionDBModel() {
        val conversionDBModel = apiModel.toExchangeRateModel().toConversionDBModel()

        assertAllTrue(
            conversionDBModel.base == apiModel.base, conversionDBModel.date == apiModel.date,
            conversionDBModel.AED == apiModel.conversion.aed, conversionDBModel.AFN == apiModel.conversion.afn,
            conversionDBModel.ALL == apiModel.conversion.all, conversionDBModel.AMD == apiModel.conversion.amd,
            conversionDBModel.ANG == apiModel.conversion.ang, conversionDBModel.AOA == apiModel.conversion.aoa,
            conversionDBModel.ARS == apiModel.conversion.ars, conversionDBModel.AUD == apiModel.conversion.aud,
            conversionDBModel.AWG == apiModel.conversion.awg, conversionDBModel.AZN == apiModel.conversion.azn,
            conversionDBModel.BAM == apiModel.conversion.bam, conversionDBModel.BBD == apiModel.conversion.bbd,
            conversionDBModel.BDT == apiModel.conversion.bdt, conversionDBModel.BGN == apiModel.conversion.bgn,
            conversionDBModel.BHD == apiModel.conversion.bhd, conversionDBModel.BIF == apiModel.conversion.bif,
            conversionDBModel.BMD == apiModel.conversion.bmd, conversionDBModel.BND == apiModel.conversion.bnd,
            conversionDBModel.BOB == apiModel.conversion.bob, conversionDBModel.BRL == apiModel.conversion.brl,
            conversionDBModel.BSD == apiModel.conversion.bsd, conversionDBModel.BTC == apiModel.conversion.btc,
            conversionDBModel.BTN == apiModel.conversion.btn, conversionDBModel.BWP == apiModel.conversion.bwp,
            conversionDBModel.BYN == apiModel.conversion.byn, conversionDBModel.BZD == apiModel.conversion.bzd,
            conversionDBModel.CAD == apiModel.conversion.cad, conversionDBModel.CDF == apiModel.conversion.cdf,
            conversionDBModel.CHF == apiModel.conversion.chf, conversionDBModel.CLF == apiModel.conversion.clf,
            conversionDBModel.CLP == apiModel.conversion.clp, conversionDBModel.CNH == apiModel.conversion.cnh,
            conversionDBModel.CNY == apiModel.conversion.cny, conversionDBModel.COP == apiModel.conversion.cop,
            conversionDBModel.CRC == apiModel.conversion.crc, conversionDBModel.CUC == apiModel.conversion.cuc,
            conversionDBModel.CUP == apiModel.conversion.cup, conversionDBModel.CVE == apiModel.conversion.cve,
            conversionDBModel.CZK == apiModel.conversion.czk, conversionDBModel.DJF == apiModel.conversion.djf,
            conversionDBModel.DKK == apiModel.conversion.dkk, conversionDBModel.DOP == apiModel.conversion.dop,
            conversionDBModel.DZD == apiModel.conversion.dzd, conversionDBModel.EGP == apiModel.conversion.egp,
            conversionDBModel.ERN == apiModel.conversion.ern, conversionDBModel.ETB == apiModel.conversion.etb,
            conversionDBModel.EUR == apiModel.conversion.eur, conversionDBModel.FJD == apiModel.conversion.fjd,
            conversionDBModel.FKP == apiModel.conversion.fkp, conversionDBModel.GBP == apiModel.conversion.gbp,
            conversionDBModel.GEL == apiModel.conversion.gel, conversionDBModel.GGP == apiModel.conversion.ggp,
            conversionDBModel.GHS == apiModel.conversion.ghs, conversionDBModel.GIP == apiModel.conversion.gip,
            conversionDBModel.GMD == apiModel.conversion.gmd, conversionDBModel.GNF == apiModel.conversion.gnf,
            conversionDBModel.GTQ == apiModel.conversion.gtq, conversionDBModel.GYD == apiModel.conversion.gyd,
            conversionDBModel.HKD == apiModel.conversion.hkd, conversionDBModel.HNL == apiModel.conversion.hnl,
            conversionDBModel.HRK == apiModel.conversion.hrk, conversionDBModel.HTG == apiModel.conversion.htg,
            conversionDBModel.HUF == apiModel.conversion.huf, conversionDBModel.IDR == apiModel.conversion.idr,
            conversionDBModel.ILS == apiModel.conversion.ils, conversionDBModel.IMP == apiModel.conversion.imp,
            conversionDBModel.INR == apiModel.conversion.inr, conversionDBModel.IQD == apiModel.conversion.iqd,
            conversionDBModel.IRR == apiModel.conversion.irr, conversionDBModel.ISK == apiModel.conversion.isk,
            conversionDBModel.JEP == apiModel.conversion.jep, conversionDBModel.JMD == apiModel.conversion.jmd,
            conversionDBModel.JOD == apiModel.conversion.jod, conversionDBModel.JPY == apiModel.conversion.jpy,
            conversionDBModel.KES == apiModel.conversion.kes, conversionDBModel.KGS == apiModel.conversion.kgs,
            conversionDBModel.KHR == apiModel.conversion.khr, conversionDBModel.KMF == apiModel.conversion.kmf,
            conversionDBModel.KPW == apiModel.conversion.kpw, conversionDBModel.KRW == apiModel.conversion.krw,
            conversionDBModel.KWD == apiModel.conversion.kwd, conversionDBModel.KYD == apiModel.conversion.kyd,
            conversionDBModel.KZT == apiModel.conversion.kzt, conversionDBModel.LAK == apiModel.conversion.lak,
            conversionDBModel.LBP == apiModel.conversion.lbp, conversionDBModel.LKR == apiModel.conversion.lkr,
            conversionDBModel.LRD == apiModel.conversion.lrd, conversionDBModel.LSL == apiModel.conversion.lsl,
            conversionDBModel.LYD == apiModel.conversion.lyd, conversionDBModel.MAD == apiModel.conversion.mad,
            conversionDBModel.MDL == apiModel.conversion.mdl, conversionDBModel.MGA == apiModel.conversion.mga,
            conversionDBModel.MKD == apiModel.conversion.mkd, conversionDBModel.MMK == apiModel.conversion.mmk,
            conversionDBModel.MNT == apiModel.conversion.mnt, conversionDBModel.MOP == apiModel.conversion.mop,
            conversionDBModel.MRO == apiModel.conversion.mro, conversionDBModel.MRU == apiModel.conversion.mru,
            conversionDBModel.MUR == apiModel.conversion.mur, conversionDBModel.MVR == apiModel.conversion.mvr,
            conversionDBModel.MWK == apiModel.conversion.mwk, conversionDBModel.MXN == apiModel.conversion.mxn,
            conversionDBModel.MYR == apiModel.conversion.myr, conversionDBModel.MZN == apiModel.conversion.mzn,
            conversionDBModel.NAD == apiModel.conversion.nad, conversionDBModel.NGN == apiModel.conversion.ngn,
            conversionDBModel.NIO == apiModel.conversion.nio, conversionDBModel.NOK == apiModel.conversion.nok,
            conversionDBModel.NPR == apiModel.conversion.npr, conversionDBModel.NZD == apiModel.conversion.nzd,
            conversionDBModel.OMR == apiModel.conversion.omr, conversionDBModel.PAB == apiModel.conversion.pab,
            conversionDBModel.PEN == apiModel.conversion.pen, conversionDBModel.PGK == apiModel.conversion.pgk,
            conversionDBModel.PHP == apiModel.conversion.php, conversionDBModel.PKR == apiModel.conversion.pkr,
            conversionDBModel.PLN == apiModel.conversion.pln, conversionDBModel.PYG == apiModel.conversion.pyg,
            conversionDBModel.QAR == apiModel.conversion.qar, conversionDBModel.RON == apiModel.conversion.ron,
            conversionDBModel.RSD == apiModel.conversion.rsd, conversionDBModel.RUB == apiModel.conversion.rub,
            conversionDBModel.RWF == apiModel.conversion.rwf, conversionDBModel.SAR == apiModel.conversion.sar,
            conversionDBModel.SBD == apiModel.conversion.sbd, conversionDBModel.SCR == apiModel.conversion.scr,
            conversionDBModel.SDG == apiModel.conversion.sdg, conversionDBModel.SEK == apiModel.conversion.sek,
            conversionDBModel.SGD == apiModel.conversion.sgd, conversionDBModel.SHP == apiModel.conversion.shp,
            conversionDBModel.SLL == apiModel.conversion.sll, conversionDBModel.SOS == apiModel.conversion.sos,
            conversionDBModel.SRD == apiModel.conversion.srd, conversionDBModel.SSP == apiModel.conversion.ssp,
            conversionDBModel.STD == apiModel.conversion.std, conversionDBModel.STN == apiModel.conversion.stn,
            conversionDBModel.SVC == apiModel.conversion.svc, conversionDBModel.SYP == apiModel.conversion.syp,
            conversionDBModel.SZL == apiModel.conversion.szl, conversionDBModel.THB == apiModel.conversion.thb,
            conversionDBModel.TJS == apiModel.conversion.tjs, conversionDBModel.TMT == apiModel.conversion.tmt,
            conversionDBModel.TND == apiModel.conversion.tnd, conversionDBModel.TOP == apiModel.conversion.top,
            conversionDBModel.TRY == apiModel.conversion.`try`, conversionDBModel.TTD == apiModel.conversion.ttd,
            conversionDBModel.TWD == apiModel.conversion.twd, conversionDBModel.TZS == apiModel.conversion.tzs,
            conversionDBModel.UAH == apiModel.conversion.uah, conversionDBModel.UGX == apiModel.conversion.ugx,
            conversionDBModel.USD == apiModel.conversion.usd, conversionDBModel.UYU == apiModel.conversion.uyu,
            conversionDBModel.UZS == apiModel.conversion.uzs, conversionDBModel.VES == apiModel.conversion.ves,
            conversionDBModel.VND == apiModel.conversion.vnd, conversionDBModel.VUV == apiModel.conversion.vuv,
            conversionDBModel.WST == apiModel.conversion.wst, conversionDBModel.XAF == apiModel.conversion.xaf,
            conversionDBModel.XAG == apiModel.conversion.xag, conversionDBModel.XAU == apiModel.conversion.xau,
            conversionDBModel.XCD == apiModel.conversion.xcd, conversionDBModel.XDR == apiModel.conversion.xdr,
            conversionDBModel.XOF == apiModel.conversion.xof, conversionDBModel.XPD == apiModel.conversion.xpd,
            conversionDBModel.XPF == apiModel.conversion.xpf, conversionDBModel.XPT == apiModel.conversion.xpt,
            conversionDBModel.YER == apiModel.conversion.yer, conversionDBModel.ZAR == apiModel.conversion.zar,
            conversionDBModel.ZMW == apiModel.conversion.zmw, conversionDBModel.ZWL == apiModel.conversion.zwl
        )
    }

    @Test
    fun toSerializedString() {
        val serializedString = apiModel.toSerializedString()
        assertTrue {
            Json.decodeFromString<ExchangeRateAPIModel>(serializedString) == apiModel
        }
    }
}
