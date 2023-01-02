package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.model.Conversion
import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.test.BaseTest
import com.oztechan.ccc.test.util.assertAllTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.common.api.model.Conversion as ConversionEntity
import com.oztechan.ccc.common.api.model.CurrencyResponse as CurrencyResponseEntity

internal class CurrencyResponseTest : BaseTest() {
    @Suppress("LongMethod")
    @Test
    fun toModel() {
        val conversionEntity = ConversionEntity(
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
        val entity = CurrencyResponseEntity("EUR", "15.12.21", conversionEntity)
        entity.toModel()

        val model = entity.toModel()
        assertEquals(entity.base, model.base)
        assertEquals(entity.date, model.date)
        assertEquals(entity.conversion.toModel(), model.conversion)
    }

    @Suppress("LongMethod")
    @Test
    fun toConversion() {
        val conversion = Conversion(
            "EUR", "12.01.21", 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0,
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
        val response = CurrencyResponse("EUR", "21.12.21", conversion)

        val offline = response.toConversion()

        assertAllTrue(
            offline.base == response.base, offline.date == response.date,
            offline.AED == response.conversion.aed, offline.AFN == response.conversion.afn,
            offline.ALL == response.conversion.all, offline.AMD == response.conversion.amd,
            offline.ANG == response.conversion.ang, offline.AOA == response.conversion.aoa,
            offline.ARS == response.conversion.ars, offline.AUD == response.conversion.aud,
            offline.AWG == response.conversion.awg, offline.AZN == response.conversion.azn,
            offline.BAM == response.conversion.bam, offline.BBD == response.conversion.bbd,
            offline.BDT == response.conversion.bdt, offline.BGN == response.conversion.bgn,
            offline.BHD == response.conversion.bhd, offline.BIF == response.conversion.bif,
            offline.BMD == response.conversion.bmd, offline.BND == response.conversion.bnd,
            offline.BOB == response.conversion.bob, offline.BRL == response.conversion.brl,
            offline.BSD == response.conversion.bsd, offline.BTC == response.conversion.btc,
            offline.BTN == response.conversion.btn, offline.BWP == response.conversion.bwp,
            offline.BYN == response.conversion.byn, offline.BZD == response.conversion.bzd,
            offline.CAD == response.conversion.cad, offline.CDF == response.conversion.cdf,
            offline.CHF == response.conversion.chf, offline.CLF == response.conversion.clf,
            offline.CLP == response.conversion.clp, offline.CNH == response.conversion.cnh,
            offline.CNY == response.conversion.cny, offline.COP == response.conversion.cop,
            offline.CRC == response.conversion.crc, offline.CUC == response.conversion.cuc,
            offline.CUP == response.conversion.cup, offline.CVE == response.conversion.cve,
            offline.CZK == response.conversion.czk, offline.DJF == response.conversion.djf,
            offline.DKK == response.conversion.dkk, offline.DOP == response.conversion.dop,
            offline.DZD == response.conversion.dzd, offline.EGP == response.conversion.egp,
            offline.ERN == response.conversion.ern, offline.ETB == response.conversion.etb,
            offline.EUR == response.conversion.eur, offline.FJD == response.conversion.fjd,
            offline.FKP == response.conversion.fkp, offline.GBP == response.conversion.gbp,
            offline.GEL == response.conversion.gel, offline.GGP == response.conversion.ggp,
            offline.GHS == response.conversion.ghs, offline.GIP == response.conversion.gip,
            offline.GMD == response.conversion.gmd, offline.GNF == response.conversion.gnf,
            offline.GTQ == response.conversion.gtq, offline.GYD == response.conversion.gyd,
            offline.HKD == response.conversion.hkd, offline.HNL == response.conversion.hnl,
            offline.HRK == response.conversion.hrk, offline.HTG == response.conversion.htg,
            offline.HUF == response.conversion.huf, offline.IDR == response.conversion.idr,
            offline.ILS == response.conversion.ils, offline.IMP == response.conversion.imp,
            offline.INR == response.conversion.inr, offline.IQD == response.conversion.iqd,
            offline.IRR == response.conversion.irr, offline.ISK == response.conversion.isk,
            offline.JEP == response.conversion.jep, offline.JMD == response.conversion.jmd,
            offline.JOD == response.conversion.jod, offline.JPY == response.conversion.jpy,
            offline.KES == response.conversion.kes, offline.KGS == response.conversion.kgs,
            offline.KHR == response.conversion.khr, offline.KMF == response.conversion.kmf,
            offline.KPW == response.conversion.kpw, offline.KRW == response.conversion.krw,
            offline.KWD == response.conversion.kwd, offline.KYD == response.conversion.kyd,
            offline.KZT == response.conversion.kzt, offline.LAK == response.conversion.lak,
            offline.LBP == response.conversion.lbp, offline.LKR == response.conversion.lkr,
            offline.LRD == response.conversion.lrd, offline.LSL == response.conversion.lsl,
            offline.LYD == response.conversion.lyd, offline.MAD == response.conversion.mad,
            offline.MDL == response.conversion.mdl, offline.MGA == response.conversion.mga,
            offline.MKD == response.conversion.mkd, offline.MMK == response.conversion.mmk,
            offline.MNT == response.conversion.mnt, offline.MOP == response.conversion.mop,
            offline.MRO == response.conversion.mro, offline.MRU == response.conversion.mru,
            offline.MUR == response.conversion.mur, offline.MVR == response.conversion.mvr,
            offline.MWK == response.conversion.mwk, offline.MXN == response.conversion.mxn,
            offline.MYR == response.conversion.myr, offline.MZN == response.conversion.mzn,
            offline.NAD == response.conversion.nad, offline.NGN == response.conversion.ngn,
            offline.NIO == response.conversion.nio, offline.NOK == response.conversion.nok,
            offline.NPR == response.conversion.npr, offline.NZD == response.conversion.nzd,
            offline.OMR == response.conversion.omr, offline.PAB == response.conversion.pab,
            offline.PEN == response.conversion.pen, offline.PGK == response.conversion.pgk,
            offline.PHP == response.conversion.php, offline.PKR == response.conversion.pkr,
            offline.PLN == response.conversion.pln, offline.PYG == response.conversion.pyg,
            offline.QAR == response.conversion.qar, offline.RON == response.conversion.ron,
            offline.RSD == response.conversion.rsd, offline.RUB == response.conversion.rub,
            offline.RWF == response.conversion.rwf, offline.SAR == response.conversion.sar,
            offline.SBD == response.conversion.sbd, offline.SCR == response.conversion.scr,
            offline.SDG == response.conversion.sdg, offline.SEK == response.conversion.sek,
            offline.SGD == response.conversion.sgd, offline.SHP == response.conversion.shp,
            offline.SLL == response.conversion.sll, offline.SOS == response.conversion.sos,
            offline.SRD == response.conversion.srd, offline.SSP == response.conversion.ssp,
            offline.STD == response.conversion.std, offline.STN == response.conversion.stn,
            offline.SVC == response.conversion.svc, offline.SYP == response.conversion.syp,
            offline.SZL == response.conversion.szl, offline.THB == response.conversion.thb,
            offline.TJS == response.conversion.tjs, offline.TMT == response.conversion.tmt,
            offline.TND == response.conversion.tnd, offline.TOP == response.conversion.top,
            offline.TRY == response.conversion.`try`, offline.TTD == response.conversion.ttd,
            offline.TWD == response.conversion.twd, offline.TZS == response.conversion.tzs,
            offline.UAH == response.conversion.uah, offline.UGX == response.conversion.ugx,
            offline.USD == response.conversion.usd, offline.UYU == response.conversion.uyu,
            offline.UZS == response.conversion.uzs, offline.VES == response.conversion.ves,
            offline.VND == response.conversion.vnd, offline.VUV == response.conversion.vuv,
            offline.WST == response.conversion.wst, offline.XAF == response.conversion.xaf,
            offline.XAG == response.conversion.xag, offline.XAU == response.conversion.xau,
            offline.XCD == response.conversion.xcd, offline.XDR == response.conversion.xdr,
            offline.XOF == response.conversion.xof, offline.XPD == response.conversion.xpd,
            offline.XPF == response.conversion.xpf, offline.XPT == response.conversion.xpt,
            offline.YER == response.conversion.yer, offline.ZAR == response.conversion.zar,
            offline.ZMW == response.conversion.zmw, offline.ZWL == response.conversion.zwl
        )
    }
}
