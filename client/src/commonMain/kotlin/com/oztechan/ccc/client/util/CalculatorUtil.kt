/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("TooManyFunctions")

package com.oztechan.ccc.client.util

import com.github.submob.scopemob.whether
import com.github.submob.scopemob.whetherNot
import com.oztechan.ccc.client.model.Currency
import com.oztechan.ccc.common.model.CurrencyType
import com.oztechan.ccc.common.model.Rates

const val MAXIMUM_FLOATING_POINT = 9

internal expect fun Double.getFormatted(precision: Int): String

internal expect fun Double.removeScientificNotation(): String

internal fun Rates?.calculateResult(code: String, input: String?) = this
    ?.whetherNot { input.isNullOrEmpty() }
    ?.getConversionByCode(code)
    ?.times(input?.toSupportedCharacters()?.toStandardDigits()?.toDouble() ?: 0.0)
    ?: 0.0

internal fun String.toSupportedCharacters() = replace(",", ".")
    .replace("٫", ".")
    .replace(" ", "")
    .replace("−", "-")

internal fun String.toStandardDigits(): String {
    val builder = StringBuilder()
    forEach { char ->
        char.toString().toIntOrNull()
            ?.whether { it >= 0 }
            ?.let { builder.append(it) }
            ?: run { builder.append(char) }
    }
    return builder.toString()
}

internal fun Currency.getCurrencyConversionByRate(
    base: String,
    rate: Rates?
) = "1 $base = ${rate?.getConversionByCode(code)} ${getVariablesOneLine()}"

fun List<Currency>?.toValidList(currentBase: String) = this?.filter {
    it.code != currentBase &&
        it.isActive &&
        it.rate != "NaN" &&
        it.rate != "0.0" &&
        it.rate != "0"
} ?: mutableListOf()

internal fun Int.indexToNumber() = this + 1

fun Int.numberToIndex() = this - 1

@Suppress("ComplexMethod", "LongMethod")
internal fun Rates.getConversionByCode(code: String) = when (code.uppercase()) {
    CurrencyType.AED.toString() -> aed
    CurrencyType.AFN.toString() -> afn
    CurrencyType.ALL.toString() -> all
    CurrencyType.AMD.toString() -> amd
    CurrencyType.ANG.toString() -> ang
    CurrencyType.AOA.toString() -> aoa
    CurrencyType.ARS.toString() -> ars
    CurrencyType.AUD.toString() -> aud
    CurrencyType.AWG.toString() -> awg
    CurrencyType.AZN.toString() -> azn
    CurrencyType.BAM.toString() -> bam
    CurrencyType.BBD.toString() -> bbd
    CurrencyType.BDT.toString() -> bdt
    CurrencyType.BGN.toString() -> bgn
    CurrencyType.BHD.toString() -> bhd
    CurrencyType.BIF.toString() -> bif
    CurrencyType.BMD.toString() -> bmd
    CurrencyType.BND.toString() -> bnd
    CurrencyType.BOB.toString() -> bob
    CurrencyType.BRL.toString() -> brl
    CurrencyType.BSD.toString() -> bsd
    CurrencyType.BTC.toString() -> btc
    CurrencyType.BTN.toString() -> btn
    CurrencyType.BWP.toString() -> bwp
    CurrencyType.BYN.toString() -> byn
    CurrencyType.BZD.toString() -> bzd
    CurrencyType.CAD.toString() -> cad
    CurrencyType.CDF.toString() -> cdf
    CurrencyType.CHF.toString() -> chf
    CurrencyType.CLF.toString() -> clf
    CurrencyType.CLP.toString() -> clp
    CurrencyType.CNH.toString() -> cnh
    CurrencyType.CNY.toString() -> cny
    CurrencyType.COP.toString() -> cop
    CurrencyType.CRC.toString() -> crc
    CurrencyType.CUC.toString() -> cuc
    CurrencyType.CUP.toString() -> cup
    CurrencyType.CVE.toString() -> cve
    CurrencyType.CZK.toString() -> czk
    CurrencyType.DJF.toString() -> djf
    CurrencyType.DKK.toString() -> dkk
    CurrencyType.DOP.toString() -> dop
    CurrencyType.DZD.toString() -> dzd
    CurrencyType.EGP.toString() -> egp
    CurrencyType.ERN.toString() -> ern
    CurrencyType.ETB.toString() -> etb
    CurrencyType.EUR.toString() -> eur
    CurrencyType.FJD.toString() -> fjd
    CurrencyType.FKP.toString() -> fkp
    CurrencyType.GBP.toString() -> gbp
    CurrencyType.GEL.toString() -> gel
    CurrencyType.GGP.toString() -> ggp
    CurrencyType.GHS.toString() -> ghs
    CurrencyType.GIP.toString() -> gip
    CurrencyType.GMD.toString() -> gmd
    CurrencyType.GNF.toString() -> gnf
    CurrencyType.GTQ.toString() -> gtq
    CurrencyType.GYD.toString() -> gyd
    CurrencyType.HKD.toString() -> hkd
    CurrencyType.HNL.toString() -> hnl
    CurrencyType.HRK.toString() -> hrk
    CurrencyType.HTG.toString() -> htg
    CurrencyType.HUF.toString() -> huf
    CurrencyType.IDR.toString() -> idr
    CurrencyType.ILS.toString() -> ils
    CurrencyType.IMP.toString() -> imp
    CurrencyType.INR.toString() -> inr
    CurrencyType.IQD.toString() -> iqd
    CurrencyType.IRR.toString() -> irr
    CurrencyType.ISK.toString() -> isk
    CurrencyType.JEP.toString() -> jep
    CurrencyType.JMD.toString() -> jmd
    CurrencyType.JOD.toString() -> jod
    CurrencyType.JPY.toString() -> jpy
    CurrencyType.KES.toString() -> kes
    CurrencyType.KGS.toString() -> kgs
    CurrencyType.KHR.toString() -> khr
    CurrencyType.KMF.toString() -> kmf
    CurrencyType.KPW.toString() -> kpw
    CurrencyType.KRW.toString() -> krw
    CurrencyType.KWD.toString() -> kwd
    CurrencyType.KYD.toString() -> kyd
    CurrencyType.KZT.toString() -> kzt
    CurrencyType.LAK.toString() -> lak
    CurrencyType.LBP.toString() -> lbp
    CurrencyType.LKR.toString() -> lkr
    CurrencyType.LRD.toString() -> lrd
    CurrencyType.LSL.toString() -> lsl
    CurrencyType.LYD.toString() -> lyd
    CurrencyType.MAD.toString() -> mad
    CurrencyType.MDL.toString() -> mdl
    CurrencyType.MGA.toString() -> mga
    CurrencyType.MKD.toString() -> mkd
    CurrencyType.MMK.toString() -> mmk
    CurrencyType.MNT.toString() -> mnt
    CurrencyType.MOP.toString() -> mop
    CurrencyType.MRO.toString() -> mro
    CurrencyType.MRU.toString() -> mru
    CurrencyType.MUR.toString() -> mur
    CurrencyType.MVR.toString() -> mvr
    CurrencyType.MWK.toString() -> mwk
    CurrencyType.MXN.toString() -> mxn
    CurrencyType.MYR.toString() -> myr
    CurrencyType.MZN.toString() -> mzn
    CurrencyType.NAD.toString() -> nad
    CurrencyType.NGN.toString() -> ngn
    CurrencyType.NIO.toString() -> nio
    CurrencyType.NOK.toString() -> nok
    CurrencyType.NPR.toString() -> npr
    CurrencyType.NZD.toString() -> nzd
    CurrencyType.OMR.toString() -> omr
    CurrencyType.PAB.toString() -> pab
    CurrencyType.PEN.toString() -> pen
    CurrencyType.PGK.toString() -> pgk
    CurrencyType.PHP.toString() -> php
    CurrencyType.PKR.toString() -> pkr
    CurrencyType.PLN.toString() -> pln
    CurrencyType.PYG.toString() -> pyg
    CurrencyType.QAR.toString() -> qar
    CurrencyType.RON.toString() -> ron
    CurrencyType.RSD.toString() -> rsd
    CurrencyType.RUB.toString() -> rub
    CurrencyType.RWF.toString() -> rwf
    CurrencyType.SAR.toString() -> sar
    CurrencyType.SBD.toString() -> sbd
    CurrencyType.SCR.toString() -> scr
    CurrencyType.SDG.toString() -> sdg
    CurrencyType.SEK.toString() -> sek
    CurrencyType.SGD.toString() -> sgd
    CurrencyType.SHP.toString() -> shp
    CurrencyType.SLL.toString() -> sll
    CurrencyType.SOS.toString() -> sos
    CurrencyType.SRD.toString() -> srd
    CurrencyType.SSP.toString() -> ssp
    CurrencyType.STD.toString() -> std
    CurrencyType.STN.toString() -> stn
    CurrencyType.SVC.toString() -> svc
    CurrencyType.SYP.toString() -> syp
    CurrencyType.SZL.toString() -> szl
    CurrencyType.THB.toString() -> thb
    CurrencyType.TJS.toString() -> tjs
    CurrencyType.TMT.toString() -> tmt
    CurrencyType.TND.toString() -> tnd
    CurrencyType.TOP.toString() -> top
    CurrencyType.TRY.toString() -> `try`
    CurrencyType.TTD.toString() -> ttd
    CurrencyType.TWD.toString() -> twd
    CurrencyType.TZS.toString() -> tzs
    CurrencyType.UAH.toString() -> uah
    CurrencyType.UGX.toString() -> ugx
    CurrencyType.USD.toString() -> usd
    CurrencyType.UYU.toString() -> uyu
    CurrencyType.UZS.toString() -> uzs
    CurrencyType.VES.toString() -> ves
    CurrencyType.VND.toString() -> vnd
    CurrencyType.VUV.toString() -> vuv
    CurrencyType.WST.toString() -> wst
    CurrencyType.XAF.toString() -> xaf
    CurrencyType.XAG.toString() -> xag
    CurrencyType.XAU.toString() -> xau
    CurrencyType.XCD.toString() -> xcd
    CurrencyType.XDR.toString() -> xdr
    CurrencyType.XOF.toString() -> xof
    CurrencyType.XPD.toString() -> xpd
    CurrencyType.XPF.toString() -> xpf
    CurrencyType.XPT.toString() -> xpt
    CurrencyType.YER.toString() -> yer
    CurrencyType.ZAR.toString() -> zar
    CurrencyType.ZMW.toString() -> zmw
    CurrencyType.ZWL.toString() -> zwl
    else -> 0.0
}
