package com.oztechan.ccc.common.mapper

import com.oztechan.ccc.common.model.CurrencyResponse
import com.oztechan.ccc.common.model.Rates
import com.oztechan.ccc.test.BaseTest
import com.oztechan.ccc.test.util.assertAllTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import com.oztechan.ccc.common.api.model.CurrencyResponse as CurrencyResponseEntity
import com.oztechan.ccc.common.api.model.Rates as RatesEntity

class CurrencyResponseTest : BaseTest() {
    @Suppress("LongMethod")
    @Test
    fun toModel() {
        val ratesEntity = RatesEntity(
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
        val entity = CurrencyResponseEntity("EUR", "15.12.21", ratesEntity)
        entity.toModel()

        val model = entity.toModel()
        assertEquals(entity.base, model.base)
        assertEquals(entity.date, model.date)
        assertEquals(entity.rates.toModel(), model.rates)
    }

    @Suppress("LongMethod")
    @Test
    fun toOfflineRates() {
        val rates = Rates(
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
        val response = CurrencyResponse("EUR", "21.12.21", rates)

        val offline = response.toOfflineRates()

        assertAllTrue(
            offline.base == response.base, offline.date == response.date,
            offline.AED == response.rates.aed, offline.AFN == response.rates.afn,
            offline.ALL == response.rates.all, offline.AMD == response.rates.amd,
            offline.ANG == response.rates.ang, offline.AOA == response.rates.aoa,
            offline.ARS == response.rates.ars, offline.AUD == response.rates.aud,
            offline.AWG == response.rates.awg, offline.AZN == response.rates.azn,
            offline.BAM == response.rates.bam, offline.BBD == response.rates.bbd,
            offline.BDT == response.rates.bdt, offline.BGN == response.rates.bgn,
            offline.BHD == response.rates.bhd, offline.BIF == response.rates.bif,
            offline.BMD == response.rates.bmd, offline.BND == response.rates.bnd,
            offline.BOB == response.rates.bob, offline.BRL == response.rates.brl,
            offline.BSD == response.rates.bsd, offline.BTC == response.rates.btc,
            offline.BTN == response.rates.btn, offline.BWP == response.rates.bwp,
            offline.BYN == response.rates.byn, offline.BZD == response.rates.bzd,
            offline.CAD == response.rates.cad, offline.CDF == response.rates.cdf,
            offline.CHF == response.rates.chf, offline.CLF == response.rates.clf,
            offline.CLP == response.rates.clp, offline.CNH == response.rates.cnh,
            offline.CNY == response.rates.cny, offline.COP == response.rates.cop,
            offline.CRC == response.rates.crc, offline.CUC == response.rates.cuc,
            offline.CUP == response.rates.cup, offline.CVE == response.rates.cve,
            offline.CZK == response.rates.czk, offline.DJF == response.rates.djf,
            offline.DKK == response.rates.dkk, offline.DOP == response.rates.dop,
            offline.DZD == response.rates.dzd, offline.EGP == response.rates.egp,
            offline.ERN == response.rates.ern, offline.ETB == response.rates.etb,
            offline.EUR == response.rates.eur, offline.FJD == response.rates.fjd,
            offline.FKP == response.rates.fkp, offline.GBP == response.rates.gbp,
            offline.GEL == response.rates.gel, offline.GGP == response.rates.ggp,
            offline.GHS == response.rates.ghs, offline.GIP == response.rates.gip,
            offline.GMD == response.rates.gmd, offline.GNF == response.rates.gnf,
            offline.GTQ == response.rates.gtq, offline.GYD == response.rates.gyd,
            offline.HKD == response.rates.hkd, offline.HNL == response.rates.hnl,
            offline.HRK == response.rates.hrk, offline.HTG == response.rates.htg,
            offline.HUF == response.rates.huf, offline.IDR == response.rates.idr,
            offline.ILS == response.rates.ils, offline.IMP == response.rates.imp,
            offline.INR == response.rates.inr, offline.IQD == response.rates.iqd,
            offline.IRR == response.rates.irr, offline.ISK == response.rates.isk,
            offline.JEP == response.rates.jep, offline.JMD == response.rates.jmd,
            offline.JOD == response.rates.jod, offline.JPY == response.rates.jpy,
            offline.KES == response.rates.kes, offline.KGS == response.rates.kgs,
            offline.KHR == response.rates.khr, offline.KMF == response.rates.kmf,
            offline.KPW == response.rates.kpw, offline.KRW == response.rates.krw,
            offline.KWD == response.rates.kwd, offline.KYD == response.rates.kyd,
            offline.KZT == response.rates.kzt, offline.LAK == response.rates.lak,
            offline.LBP == response.rates.lbp, offline.LKR == response.rates.lkr,
            offline.LRD == response.rates.lrd, offline.LSL == response.rates.lsl,
            offline.LYD == response.rates.lyd, offline.MAD == response.rates.mad,
            offline.MDL == response.rates.mdl, offline.MGA == response.rates.mga,
            offline.MKD == response.rates.mkd, offline.MMK == response.rates.mmk,
            offline.MNT == response.rates.mnt, offline.MOP == response.rates.mop,
            offline.MRO == response.rates.mro, offline.MRU == response.rates.mru,
            offline.MUR == response.rates.mur, offline.MVR == response.rates.mvr,
            offline.MWK == response.rates.mwk, offline.MXN == response.rates.mxn,
            offline.MYR == response.rates.myr, offline.MZN == response.rates.mzn,
            offline.NAD == response.rates.nad, offline.NGN == response.rates.ngn,
            offline.NIO == response.rates.nio, offline.NOK == response.rates.nok,
            offline.NPR == response.rates.npr, offline.NZD == response.rates.nzd,
            offline.OMR == response.rates.omr, offline.PAB == response.rates.pab,
            offline.PEN == response.rates.pen, offline.PGK == response.rates.pgk,
            offline.PHP == response.rates.php, offline.PKR == response.rates.pkr,
            offline.PLN == response.rates.pln, offline.PYG == response.rates.pyg,
            offline.QAR == response.rates.qar, offline.RON == response.rates.ron,
            offline.RSD == response.rates.rsd, offline.RUB == response.rates.rub,
            offline.RWF == response.rates.rwf, offline.SAR == response.rates.sar,
            offline.SBD == response.rates.sbd, offline.SCR == response.rates.scr,
            offline.SDG == response.rates.sdg, offline.SEK == response.rates.sek,
            offline.SGD == response.rates.sgd, offline.SHP == response.rates.shp,
            offline.SLL == response.rates.sll, offline.SOS == response.rates.sos,
            offline.SRD == response.rates.srd, offline.SSP == response.rates.ssp,
            offline.STD == response.rates.std, offline.STN == response.rates.stn,
            offline.SVC == response.rates.svc, offline.SYP == response.rates.syp,
            offline.SZL == response.rates.szl, offline.THB == response.rates.thb,
            offline.TJS == response.rates.tjs, offline.TMT == response.rates.tmt,
            offline.TND == response.rates.tnd, offline.TOP == response.rates.top,
            offline.TRY == response.rates.`try`, offline.TTD == response.rates.ttd,
            offline.TWD == response.rates.twd, offline.TZS == response.rates.tzs,
            offline.UAH == response.rates.uah, offline.UGX == response.rates.ugx,
            offline.USD == response.rates.usd, offline.UYU == response.rates.uyu,
            offline.UZS == response.rates.uzs, offline.VES == response.rates.ves,
            offline.VND == response.rates.vnd, offline.VUV == response.rates.vuv,
            offline.WST == response.rates.wst, offline.XAF == response.rates.xaf,
            offline.XAG == response.rates.xag, offline.XAU == response.rates.xau,
            offline.XCD == response.rates.xcd, offline.XDR == response.rates.xdr,
            offline.XOF == response.rates.xof, offline.XPD == response.rates.xpd,
            offline.XPF == response.rates.xpf, offline.XPT == response.rates.xpt,
            offline.YER == response.rates.yer, offline.ZAR == response.rates.zar,
            offline.ZMW == response.rates.zmw, offline.ZWL == response.rates.zwl
        )
    }
}
