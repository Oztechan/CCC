package com.oztechan.ccc.common.datasource.conversion.mapper

import com.oztechan.ccc.common.datasource.conversion.fakes.Fakes
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ConversionMapperTest {

    @Suppress("LongMethod")
    @Test
    fun toConversionModel() {
        val model = Fakes.conversionDBModel.toConversionModel()

        assertEquals(Fakes.conversionDBModel.base, model.base)
        assertEquals(Fakes.conversionDBModel.date, model.date)
        assertEquals(Fakes.conversionDBModel.AED, model.aed)
        assertEquals(Fakes.conversionDBModel.AFN, model.afn)
        assertEquals(Fakes.conversionDBModel.ALL, model.all)
        assertEquals(Fakes.conversionDBModel.AMD, model.amd)
        assertEquals(Fakes.conversionDBModel.ANG, model.ang)
        assertEquals(Fakes.conversionDBModel.AOA, model.aoa)
        assertEquals(Fakes.conversionDBModel.ARS, model.ars)
        assertEquals(Fakes.conversionDBModel.AUD, model.aud)
        assertEquals(Fakes.conversionDBModel.AWG, model.awg)
        assertEquals(Fakes.conversionDBModel.AZN, model.azn)
        assertEquals(Fakes.conversionDBModel.BAM, model.bam)
        assertEquals(Fakes.conversionDBModel.BBD, model.bbd)
        assertEquals(Fakes.conversionDBModel.BDT, model.bdt)
        assertEquals(Fakes.conversionDBModel.BGN, model.bgn)
        assertEquals(Fakes.conversionDBModel.BHD, model.bhd)
        assertEquals(Fakes.conversionDBModel.BIF, model.bif)
        assertEquals(Fakes.conversionDBModel.BMD, model.bmd)
        assertEquals(Fakes.conversionDBModel.BND, model.bnd)
        assertEquals(Fakes.conversionDBModel.BOB, model.bob)
        assertEquals(Fakes.conversionDBModel.BRL, model.brl)
        assertEquals(Fakes.conversionDBModel.BSD, model.bsd)
        assertEquals(Fakes.conversionDBModel.BTN, model.btn)
        assertEquals(Fakes.conversionDBModel.BWP, model.bwp)
        assertEquals(Fakes.conversionDBModel.BYN, model.byn)
        assertEquals(Fakes.conversionDBModel.BZD, model.bzd)
        assertEquals(Fakes.conversionDBModel.CAD, model.cad)
        assertEquals(Fakes.conversionDBModel.CDF, model.cdf)
        assertEquals(Fakes.conversionDBModel.CHF, model.chf)
        assertEquals(Fakes.conversionDBModel.CLP, model.clp)
        assertEquals(Fakes.conversionDBModel.CNY, model.cny)
        assertEquals(Fakes.conversionDBModel.COP, model.cop)
        assertEquals(Fakes.conversionDBModel.CRC, model.crc)
        assertEquals(Fakes.conversionDBModel.CUP, model.cup)
        assertEquals(Fakes.conversionDBModel.CVE, model.cve)
        assertEquals(Fakes.conversionDBModel.CZK, model.czk)
        assertEquals(Fakes.conversionDBModel.DJF, model.djf)
        assertEquals(Fakes.conversionDBModel.DKK, model.dkk)
        assertEquals(Fakes.conversionDBModel.DOP, model.dop)
        assertEquals(Fakes.conversionDBModel.DZD, model.dzd)
        assertEquals(Fakes.conversionDBModel.EGP, model.egp)
        assertEquals(Fakes.conversionDBModel.ERN, model.ern)
        assertEquals(Fakes.conversionDBModel.ETB, model.etb)
        assertEquals(Fakes.conversionDBModel.EUR, model.eur)
        assertEquals(Fakes.conversionDBModel.FJD, model.fjd)
        assertEquals(Fakes.conversionDBModel.FKP, model.fkp)
        assertEquals(Fakes.conversionDBModel.FOK, model.fok)
        assertEquals(Fakes.conversionDBModel.GBP, model.gbp)
        assertEquals(Fakes.conversionDBModel.GEL, model.gel)
        assertEquals(Fakes.conversionDBModel.GGP, model.ggp)
        assertEquals(Fakes.conversionDBModel.GHS, model.ghs)
        assertEquals(Fakes.conversionDBModel.GIP, model.gip)
        assertEquals(Fakes.conversionDBModel.GMD, model.gmd)
        assertEquals(Fakes.conversionDBModel.GNF, model.gnf)
        assertEquals(Fakes.conversionDBModel.GTQ, model.gtq)
        assertEquals(Fakes.conversionDBModel.GYD, model.gyd)
        assertEquals(Fakes.conversionDBModel.HKD, model.hkd)
        assertEquals(Fakes.conversionDBModel.HNL, model.hnl)
        assertEquals(Fakes.conversionDBModel.HRK, model.hrk)
        assertEquals(Fakes.conversionDBModel.HTG, model.htg)
        assertEquals(Fakes.conversionDBModel.HUF, model.huf)
        assertEquals(Fakes.conversionDBModel.IDR, model.idr)
        assertEquals(Fakes.conversionDBModel.ILS, model.ils)
        assertEquals(Fakes.conversionDBModel.IMP, model.imp)
        assertEquals(Fakes.conversionDBModel.INR, model.inr)
        assertEquals(Fakes.conversionDBModel.IQD, model.iqd)
        assertEquals(Fakes.conversionDBModel.IRR, model.irr)
        assertEquals(Fakes.conversionDBModel.ISK, model.isk)
        assertEquals(Fakes.conversionDBModel.JEP, model.jep)
        assertEquals(Fakes.conversionDBModel.JMD, model.jmd)
        assertEquals(Fakes.conversionDBModel.JOD, model.jod)
        assertEquals(Fakes.conversionDBModel.JPY, model.jpy)
        assertEquals(Fakes.conversionDBModel.KES, model.kes)
        assertEquals(Fakes.conversionDBModel.KGS, model.kgs)
        assertEquals(Fakes.conversionDBModel.KHR, model.khr)
        assertEquals(Fakes.conversionDBModel.KID, model.kid)
        assertEquals(Fakes.conversionDBModel.KMF, model.kmf)
        assertEquals(Fakes.conversionDBModel.KRW, model.krw)
        assertEquals(Fakes.conversionDBModel.KWD, model.kwd)
        assertEquals(Fakes.conversionDBModel.KYD, model.kyd)
        assertEquals(Fakes.conversionDBModel.KZT, model.kzt)
        assertEquals(Fakes.conversionDBModel.LAK, model.lak)
        assertEquals(Fakes.conversionDBModel.LBP, model.lbp)
        assertEquals(Fakes.conversionDBModel.LKR, model.lkr)
        assertEquals(Fakes.conversionDBModel.LRD, model.lrd)
        assertEquals(Fakes.conversionDBModel.LSL, model.lsl)
        assertEquals(Fakes.conversionDBModel.LYD, model.lyd)
        assertEquals(Fakes.conversionDBModel.MAD, model.mad)
        assertEquals(Fakes.conversionDBModel.MDL, model.mdl)
        assertEquals(Fakes.conversionDBModel.MGA, model.mga)
        assertEquals(Fakes.conversionDBModel.MKD, model.mkd)
        assertEquals(Fakes.conversionDBModel.MMK, model.mmk)
        assertEquals(Fakes.conversionDBModel.MNT, model.mnt)
        assertEquals(Fakes.conversionDBModel.MOP, model.mop)
        assertEquals(Fakes.conversionDBModel.MRU, model.mru)
        assertEquals(Fakes.conversionDBModel.MUR, model.mur)
        assertEquals(Fakes.conversionDBModel.MVR, model.mvr)
        assertEquals(Fakes.conversionDBModel.MWK, model.mwk)
        assertEquals(Fakes.conversionDBModel.MXN, model.mxn)
        assertEquals(Fakes.conversionDBModel.MYR, model.myr)
        assertEquals(Fakes.conversionDBModel.MZN, model.mzn)
        assertEquals(Fakes.conversionDBModel.NAD, model.nad)
        assertEquals(Fakes.conversionDBModel.NGN, model.ngn)
        assertEquals(Fakes.conversionDBModel.NIO, model.nio)
        assertEquals(Fakes.conversionDBModel.NOK, model.nok)
        assertEquals(Fakes.conversionDBModel.NPR, model.npr)
        assertEquals(Fakes.conversionDBModel.NZD, model.nzd)
        assertEquals(Fakes.conversionDBModel.OMR, model.omr)
        assertEquals(Fakes.conversionDBModel.PAB, model.pab)
        assertEquals(Fakes.conversionDBModel.PEN, model.pen)
        assertEquals(Fakes.conversionDBModel.PGK, model.pgk)
        assertEquals(Fakes.conversionDBModel.PHP, model.php)
        assertEquals(Fakes.conversionDBModel.PKR, model.pkr)
        assertEquals(Fakes.conversionDBModel.PLN, model.pln)
        assertEquals(Fakes.conversionDBModel.PYG, model.pyg)
        assertEquals(Fakes.conversionDBModel.QAR, model.qar)
        assertEquals(Fakes.conversionDBModel.RON, model.ron)
        assertEquals(Fakes.conversionDBModel.RSD, model.rsd)
        assertEquals(Fakes.conversionDBModel.RUB, model.rub)
        assertEquals(Fakes.conversionDBModel.RWF, model.rwf)
        assertEquals(Fakes.conversionDBModel.SAR, model.sar)
        assertEquals(Fakes.conversionDBModel.SBD, model.sbd)
        assertEquals(Fakes.conversionDBModel.SCR, model.scr)
        assertEquals(Fakes.conversionDBModel.SDG, model.sdg)
        assertEquals(Fakes.conversionDBModel.SEK, model.sek)
        assertEquals(Fakes.conversionDBModel.SGD, model.sgd)
        assertEquals(Fakes.conversionDBModel.SHP, model.shp)
        assertEquals(Fakes.conversionDBModel.SLE, model.sle)
        assertEquals(Fakes.conversionDBModel.SLL, model.sll)
        assertEquals(Fakes.conversionDBModel.SOS, model.sos)
        assertEquals(Fakes.conversionDBModel.SRD, model.srd)
        assertEquals(Fakes.conversionDBModel.SSP, model.ssp)
        assertEquals(Fakes.conversionDBModel.STN, model.stn)
        assertEquals(Fakes.conversionDBModel.SYP, model.syp)
        assertEquals(Fakes.conversionDBModel.SZL, model.szl)
        assertEquals(Fakes.conversionDBModel.THB, model.thb)
        assertEquals(Fakes.conversionDBModel.TJS, model.tjs)
        assertEquals(Fakes.conversionDBModel.TMT, model.tmt)
        assertEquals(Fakes.conversionDBModel.TND, model.tnd)
        assertEquals(Fakes.conversionDBModel.TOP, model.top)
        assertEquals(Fakes.conversionDBModel.TRY, model.`try`)
        assertEquals(Fakes.conversionDBModel.TTD, model.ttd)
        assertEquals(Fakes.conversionDBModel.TVD, model.tvd)
        assertEquals(Fakes.conversionDBModel.TWD, model.twd)
        assertEquals(Fakes.conversionDBModel.TZS, model.tzs)
        assertEquals(Fakes.conversionDBModel.UAH, model.uah)
        assertEquals(Fakes.conversionDBModel.UGX, model.ugx)
        assertEquals(Fakes.conversionDBModel.USD, model.usd)
        assertEquals(Fakes.conversionDBModel.UYU, model.uyu)
        assertEquals(Fakes.conversionDBModel.UZS, model.uzs)
        assertEquals(Fakes.conversionDBModel.VES, model.ves)
        assertEquals(Fakes.conversionDBModel.VND, model.vnd)
        assertEquals(Fakes.conversionDBModel.VUV, model.vuv)
        assertEquals(Fakes.conversionDBModel.WST, model.wst)
        assertEquals(Fakes.conversionDBModel.XAF, model.xaf)
        assertEquals(Fakes.conversionDBModel.XCD, model.xcd)
        assertEquals(Fakes.conversionDBModel.XDR, model.xdr)
        assertEquals(Fakes.conversionDBModel.XOF, model.xof)
        assertEquals(Fakes.conversionDBModel.XPF, model.xpf)
        assertEquals(Fakes.conversionDBModel.YER, model.yer)
        assertEquals(Fakes.conversionDBModel.ZAR, model.zar)
        assertEquals(Fakes.conversionDBModel.ZMW, model.zmw)
        assertEquals(Fakes.conversionDBModel.ZWL, model.zwl)
    }

    @Suppress("LongMethod")
    @Test
    fun toConversionDBModel() {
        val dbModel = Fakes.conversionModel.toConversionDBModel()

        assertEquals(Fakes.conversionModel.base, dbModel.base)
        assertEquals(Fakes.conversionModel.date, dbModel.date)
        assertEquals(Fakes.conversionModel.aed, dbModel.AED)
        assertEquals(Fakes.conversionModel.afn, dbModel.AFN)
        assertEquals(Fakes.conversionModel.all, dbModel.ALL)
        assertEquals(Fakes.conversionModel.amd, dbModel.AMD)
        assertEquals(Fakes.conversionModel.ang, dbModel.ANG)
        assertEquals(Fakes.conversionModel.aoa, dbModel.AOA)
        assertEquals(Fakes.conversionModel.ars, dbModel.ARS)
        assertEquals(Fakes.conversionModel.aud, dbModel.AUD)
        assertEquals(Fakes.conversionModel.awg, dbModel.AWG)
        assertEquals(Fakes.conversionModel.azn, dbModel.AZN)
        assertEquals(Fakes.conversionModel.bam, dbModel.BAM)
        assertEquals(Fakes.conversionModel.bbd, dbModel.BBD)
        assertEquals(Fakes.conversionModel.bdt, dbModel.BDT)
        assertEquals(Fakes.conversionModel.bgn, dbModel.BGN)
        assertEquals(Fakes.conversionModel.bhd, dbModel.BHD)
        assertEquals(Fakes.conversionModel.bif, dbModel.BIF)
        assertEquals(Fakes.conversionModel.bmd, dbModel.BMD)
        assertEquals(Fakes.conversionModel.bnd, dbModel.BND)
        assertEquals(Fakes.conversionModel.bob, dbModel.BOB)
        assertEquals(Fakes.conversionModel.brl, dbModel.BRL)
        assertEquals(Fakes.conversionModel.bsd, dbModel.BSD)
        assertEquals(Fakes.conversionModel.btn, dbModel.BTN)
        assertEquals(Fakes.conversionModel.bwp, dbModel.BWP)
        assertEquals(Fakes.conversionModel.byn, dbModel.BYN)
        assertEquals(Fakes.conversionModel.bzd, dbModel.BZD)
        assertEquals(Fakes.conversionModel.cad, dbModel.CAD)
        assertEquals(Fakes.conversionModel.cdf, dbModel.CDF)
        assertEquals(Fakes.conversionModel.chf, dbModel.CHF)
        assertEquals(Fakes.conversionModel.clp, dbModel.CLP)
        assertEquals(Fakes.conversionModel.cny, dbModel.CNY)
        assertEquals(Fakes.conversionModel.cop, dbModel.COP)
        assertEquals(Fakes.conversionModel.crc, dbModel.CRC)
        assertEquals(Fakes.conversionModel.cup, dbModel.CUP)
        assertEquals(Fakes.conversionModel.cve, dbModel.CVE)
        assertEquals(Fakes.conversionModel.czk, dbModel.CZK)
        assertEquals(Fakes.conversionModel.djf, dbModel.DJF)
        assertEquals(Fakes.conversionModel.dkk, dbModel.DKK)
        assertEquals(Fakes.conversionModel.dop, dbModel.DOP)
        assertEquals(Fakes.conversionModel.dzd, dbModel.DZD)
        assertEquals(Fakes.conversionModel.egp, dbModel.EGP)
        assertEquals(Fakes.conversionModel.ern, dbModel.ERN)
        assertEquals(Fakes.conversionModel.etb, dbModel.ETB)
        assertEquals(Fakes.conversionModel.eur, dbModel.EUR)
        assertEquals(Fakes.conversionModel.fjd, dbModel.FJD)
        assertEquals(Fakes.conversionModel.fkp, dbModel.FKP)
        assertEquals(Fakes.conversionModel.fok, dbModel.FOK)
        assertEquals(Fakes.conversionModel.gbp, dbModel.GBP)
        assertEquals(Fakes.conversionModel.gel, dbModel.GEL)
        assertEquals(Fakes.conversionModel.ggp, dbModel.GGP)
        assertEquals(Fakes.conversionModel.ghs, dbModel.GHS)
        assertEquals(Fakes.conversionModel.gip, dbModel.GIP)
        assertEquals(Fakes.conversionModel.gmd, dbModel.GMD)
        assertEquals(Fakes.conversionModel.gnf, dbModel.GNF)
        assertEquals(Fakes.conversionModel.gtq, dbModel.GTQ)
        assertEquals(Fakes.conversionModel.gyd, dbModel.GYD)
        assertEquals(Fakes.conversionModel.hkd, dbModel.HKD)
        assertEquals(Fakes.conversionModel.hnl, dbModel.HNL)
        assertEquals(Fakes.conversionModel.hrk, dbModel.HRK)
        assertEquals(Fakes.conversionModel.htg, dbModel.HTG)
        assertEquals(Fakes.conversionModel.huf, dbModel.HUF)
        assertEquals(Fakes.conversionModel.idr, dbModel.IDR)
        assertEquals(Fakes.conversionModel.ils, dbModel.ILS)
        assertEquals(Fakes.conversionModel.imp, dbModel.IMP)
        assertEquals(Fakes.conversionModel.inr, dbModel.INR)
        assertEquals(Fakes.conversionModel.iqd, dbModel.IQD)
        assertEquals(Fakes.conversionModel.irr, dbModel.IRR)
        assertEquals(Fakes.conversionModel.isk, dbModel.ISK)
        assertEquals(Fakes.conversionModel.jep, dbModel.JEP)
        assertEquals(Fakes.conversionModel.jmd, dbModel.JMD)
        assertEquals(Fakes.conversionModel.jod, dbModel.JOD)
        assertEquals(Fakes.conversionModel.jpy, dbModel.JPY)
        assertEquals(Fakes.conversionModel.kes, dbModel.KES)
        assertEquals(Fakes.conversionModel.kgs, dbModel.KGS)
        assertEquals(Fakes.conversionModel.khr, dbModel.KHR)
        assertEquals(Fakes.conversionModel.kid, dbModel.KID)
        assertEquals(Fakes.conversionModel.kmf, dbModel.KMF)
        assertEquals(Fakes.conversionModel.krw, dbModel.KRW)
        assertEquals(Fakes.conversionModel.kwd, dbModel.KWD)
        assertEquals(Fakes.conversionModel.kyd, dbModel.KYD)
        assertEquals(Fakes.conversionModel.kzt, dbModel.KZT)
        assertEquals(Fakes.conversionModel.lak, dbModel.LAK)
        assertEquals(Fakes.conversionModel.lbp, dbModel.LBP)
        assertEquals(Fakes.conversionModel.lkr, dbModel.LKR)
        assertEquals(Fakes.conversionModel.lrd, dbModel.LRD)
        assertEquals(Fakes.conversionModel.lsl, dbModel.LSL)
        assertEquals(Fakes.conversionModel.lyd, dbModel.LYD)
        assertEquals(Fakes.conversionModel.mad, dbModel.MAD)
        assertEquals(Fakes.conversionModel.mdl, dbModel.MDL)
        assertEquals(Fakes.conversionModel.mga, dbModel.MGA)
        assertEquals(Fakes.conversionModel.mkd, dbModel.MKD)
        assertEquals(Fakes.conversionModel.mmk, dbModel.MMK)
        assertEquals(Fakes.conversionModel.mnt, dbModel.MNT)
        assertEquals(Fakes.conversionModel.mop, dbModel.MOP)
        assertEquals(Fakes.conversionModel.mru, dbModel.MRU)
        assertEquals(Fakes.conversionModel.mur, dbModel.MUR)
        assertEquals(Fakes.conversionModel.mvr, dbModel.MVR)
        assertEquals(Fakes.conversionModel.mwk, dbModel.MWK)
        assertEquals(Fakes.conversionModel.mxn, dbModel.MXN)
        assertEquals(Fakes.conversionModel.myr, dbModel.MYR)
        assertEquals(Fakes.conversionModel.mzn, dbModel.MZN)
        assertEquals(Fakes.conversionModel.nad, dbModel.NAD)
        assertEquals(Fakes.conversionModel.ngn, dbModel.NGN)
        assertEquals(Fakes.conversionModel.nio, dbModel.NIO)
        assertEquals(Fakes.conversionModel.nok, dbModel.NOK)
        assertEquals(Fakes.conversionModel.npr, dbModel.NPR)
        assertEquals(Fakes.conversionModel.nzd, dbModel.NZD)
        assertEquals(Fakes.conversionModel.omr, dbModel.OMR)
        assertEquals(Fakes.conversionModel.pab, dbModel.PAB)
        assertEquals(Fakes.conversionModel.pen, dbModel.PEN)
        assertEquals(Fakes.conversionModel.pgk, dbModel.PGK)
        assertEquals(Fakes.conversionModel.php, dbModel.PHP)
        assertEquals(Fakes.conversionModel.pkr, dbModel.PKR)
        assertEquals(Fakes.conversionModel.pln, dbModel.PLN)
        assertEquals(Fakes.conversionModel.pyg, dbModel.PYG)
        assertEquals(Fakes.conversionModel.qar, dbModel.QAR)
        assertEquals(Fakes.conversionModel.ron, dbModel.RON)
        assertEquals(Fakes.conversionModel.rsd, dbModel.RSD)
        assertEquals(Fakes.conversionModel.rub, dbModel.RUB)
        assertEquals(Fakes.conversionModel.rwf, dbModel.RWF)
        assertEquals(Fakes.conversionModel.sar, dbModel.SAR)
        assertEquals(Fakes.conversionModel.sbd, dbModel.SBD)
        assertEquals(Fakes.conversionModel.scr, dbModel.SCR)
        assertEquals(Fakes.conversionModel.sdg, dbModel.SDG)
        assertEquals(Fakes.conversionModel.sek, dbModel.SEK)
        assertEquals(Fakes.conversionModel.sgd, dbModel.SGD)
        assertEquals(Fakes.conversionModel.shp, dbModel.SHP)
        assertEquals(Fakes.conversionModel.sle, dbModel.SLE)
        assertEquals(Fakes.conversionModel.sll, dbModel.SLL)
        assertEquals(Fakes.conversionModel.sos, dbModel.SOS)
        assertEquals(Fakes.conversionModel.srd, dbModel.SRD)
        assertEquals(Fakes.conversionModel.ssp, dbModel.SSP)
        assertEquals(Fakes.conversionModel.stn, dbModel.STN)
        assertEquals(Fakes.conversionModel.syp, dbModel.SYP)
        assertEquals(Fakes.conversionModel.szl, dbModel.SZL)
        assertEquals(Fakes.conversionModel.thb, dbModel.THB)
        assertEquals(Fakes.conversionModel.tjs, dbModel.TJS)
        assertEquals(Fakes.conversionModel.tmt, dbModel.TMT)
        assertEquals(Fakes.conversionModel.tnd, dbModel.TND)
        assertEquals(Fakes.conversionModel.top, dbModel.TOP)
        assertEquals(Fakes.conversionModel.`try`, dbModel.TRY)
        assertEquals(Fakes.conversionModel.ttd, dbModel.TTD)
        assertEquals(Fakes.conversionModel.tvd, dbModel.TVD)
        assertEquals(Fakes.conversionModel.twd, dbModel.TWD)
        assertEquals(Fakes.conversionModel.tzs, dbModel.TZS)
        assertEquals(Fakes.conversionModel.uah, dbModel.UAH)
        assertEquals(Fakes.conversionModel.ugx, dbModel.UGX)
        assertEquals(Fakes.conversionModel.usd, dbModel.USD)
        assertEquals(Fakes.conversionModel.uyu, dbModel.UYU)
        assertEquals(Fakes.conversionModel.uzs, dbModel.UZS)
        assertEquals(Fakes.conversionModel.ves, dbModel.VES)
        assertEquals(Fakes.conversionModel.vnd, dbModel.VND)
        assertEquals(Fakes.conversionModel.vuv, dbModel.VUV)
        assertEquals(Fakes.conversionModel.wst, dbModel.WST)
        assertEquals(Fakes.conversionModel.xaf, dbModel.XAF)
        assertEquals(Fakes.conversionModel.xcd, dbModel.XCD)
        assertEquals(Fakes.conversionModel.xdr, dbModel.XDR)
        assertEquals(Fakes.conversionModel.xof, dbModel.XOF)
        assertEquals(Fakes.conversionModel.xpf, dbModel.XPF)
        assertEquals(Fakes.conversionModel.yer, dbModel.YER)
        assertEquals(Fakes.conversionModel.zar, dbModel.ZAR)
        assertEquals(Fakes.conversionModel.zmw, dbModel.ZMW)
        assertEquals(Fakes.conversionModel.zwl, dbModel.ZWL)
    }
}
