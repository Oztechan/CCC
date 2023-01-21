/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("TooManyFunctions")

package com.oztechan.ccc.client.util

import com.github.submob.scopemob.whether
import com.github.submob.scopemob.whetherNot
import com.oztechan.ccc.client.model.Currency
import com.oztechan.ccc.common.core.model.Conversion

const val MAXIMUM_FLOATING_POINT = 9

internal expect fun Double.getFormatted(precision: Int): String

internal expect fun Double.removeScientificNotation(): String

internal fun Conversion?.calculateRate(code: String, input: String?) = this
    ?.whetherNot { input.isNullOrEmpty() }
    ?.getRateFromCode(code)
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

internal fun Currency.getConversionStringFromBase(
    base: String,
    conversion: Conversion?
) = "1 $base = ${conversion?.getRateFromCode(code)} ${getVariablesOneLine()}"

fun List<Currency>?.toValidList(currentBase: String) = this?.filter {
    it.code != currentBase &&
        it.isActive &&
        it.rate != "NaN" &&
        it.rate != "0.0" &&
        it.rate != "0"
} ?: mutableListOf()

internal fun Int.indexToNumber() = this + 1

fun Int.numberToIndex() = this - 1

// todo refactor when reflection is available in Kotlin Native
@Suppress("ComplexMethod", "LongMethod")
internal fun Conversion.getRateFromCode(code: String) = when (code.uppercase()) {
    com.oztechan.ccc.common.core.model.CurrencyType.AED.toString() -> aed
    com.oztechan.ccc.common.core.model.CurrencyType.AFN.toString() -> afn
    com.oztechan.ccc.common.core.model.CurrencyType.ALL.toString() -> all
    com.oztechan.ccc.common.core.model.CurrencyType.AMD.toString() -> amd
    com.oztechan.ccc.common.core.model.CurrencyType.ANG.toString() -> ang
    com.oztechan.ccc.common.core.model.CurrencyType.AOA.toString() -> aoa
    com.oztechan.ccc.common.core.model.CurrencyType.ARS.toString() -> ars
    com.oztechan.ccc.common.core.model.CurrencyType.AUD.toString() -> aud
    com.oztechan.ccc.common.core.model.CurrencyType.AWG.toString() -> awg
    com.oztechan.ccc.common.core.model.CurrencyType.AZN.toString() -> azn
    com.oztechan.ccc.common.core.model.CurrencyType.BAM.toString() -> bam
    com.oztechan.ccc.common.core.model.CurrencyType.BBD.toString() -> bbd
    com.oztechan.ccc.common.core.model.CurrencyType.BDT.toString() -> bdt
    com.oztechan.ccc.common.core.model.CurrencyType.BGN.toString() -> bgn
    com.oztechan.ccc.common.core.model.CurrencyType.BHD.toString() -> bhd
    com.oztechan.ccc.common.core.model.CurrencyType.BIF.toString() -> bif
    com.oztechan.ccc.common.core.model.CurrencyType.BMD.toString() -> bmd
    com.oztechan.ccc.common.core.model.CurrencyType.BND.toString() -> bnd
    com.oztechan.ccc.common.core.model.CurrencyType.BOB.toString() -> bob
    com.oztechan.ccc.common.core.model.CurrencyType.BRL.toString() -> brl
    com.oztechan.ccc.common.core.model.CurrencyType.BSD.toString() -> bsd
    com.oztechan.ccc.common.core.model.CurrencyType.BTC.toString() -> btc
    com.oztechan.ccc.common.core.model.CurrencyType.BTN.toString() -> btn
    com.oztechan.ccc.common.core.model.CurrencyType.BWP.toString() -> bwp
    com.oztechan.ccc.common.core.model.CurrencyType.BYN.toString() -> byn
    com.oztechan.ccc.common.core.model.CurrencyType.BZD.toString() -> bzd
    com.oztechan.ccc.common.core.model.CurrencyType.CAD.toString() -> cad
    com.oztechan.ccc.common.core.model.CurrencyType.CDF.toString() -> cdf
    com.oztechan.ccc.common.core.model.CurrencyType.CHF.toString() -> chf
    com.oztechan.ccc.common.core.model.CurrencyType.CLF.toString() -> clf
    com.oztechan.ccc.common.core.model.CurrencyType.CLP.toString() -> clp
    com.oztechan.ccc.common.core.model.CurrencyType.CNH.toString() -> cnh
    com.oztechan.ccc.common.core.model.CurrencyType.CNY.toString() -> cny
    com.oztechan.ccc.common.core.model.CurrencyType.COP.toString() -> cop
    com.oztechan.ccc.common.core.model.CurrencyType.CRC.toString() -> crc
    com.oztechan.ccc.common.core.model.CurrencyType.CUC.toString() -> cuc
    com.oztechan.ccc.common.core.model.CurrencyType.CUP.toString() -> cup
    com.oztechan.ccc.common.core.model.CurrencyType.CVE.toString() -> cve
    com.oztechan.ccc.common.core.model.CurrencyType.CZK.toString() -> czk
    com.oztechan.ccc.common.core.model.CurrencyType.DJF.toString() -> djf
    com.oztechan.ccc.common.core.model.CurrencyType.DKK.toString() -> dkk
    com.oztechan.ccc.common.core.model.CurrencyType.DOP.toString() -> dop
    com.oztechan.ccc.common.core.model.CurrencyType.DZD.toString() -> dzd
    com.oztechan.ccc.common.core.model.CurrencyType.EGP.toString() -> egp
    com.oztechan.ccc.common.core.model.CurrencyType.ERN.toString() -> ern
    com.oztechan.ccc.common.core.model.CurrencyType.ETB.toString() -> etb
    com.oztechan.ccc.common.core.model.CurrencyType.EUR.toString() -> eur
    com.oztechan.ccc.common.core.model.CurrencyType.FJD.toString() -> fjd
    com.oztechan.ccc.common.core.model.CurrencyType.FKP.toString() -> fkp
    com.oztechan.ccc.common.core.model.CurrencyType.GBP.toString() -> gbp
    com.oztechan.ccc.common.core.model.CurrencyType.GEL.toString() -> gel
    com.oztechan.ccc.common.core.model.CurrencyType.GGP.toString() -> ggp
    com.oztechan.ccc.common.core.model.CurrencyType.GHS.toString() -> ghs
    com.oztechan.ccc.common.core.model.CurrencyType.GIP.toString() -> gip
    com.oztechan.ccc.common.core.model.CurrencyType.GMD.toString() -> gmd
    com.oztechan.ccc.common.core.model.CurrencyType.GNF.toString() -> gnf
    com.oztechan.ccc.common.core.model.CurrencyType.GTQ.toString() -> gtq
    com.oztechan.ccc.common.core.model.CurrencyType.GYD.toString() -> gyd
    com.oztechan.ccc.common.core.model.CurrencyType.HKD.toString() -> hkd
    com.oztechan.ccc.common.core.model.CurrencyType.HNL.toString() -> hnl
    com.oztechan.ccc.common.core.model.CurrencyType.HRK.toString() -> hrk
    com.oztechan.ccc.common.core.model.CurrencyType.HTG.toString() -> htg
    com.oztechan.ccc.common.core.model.CurrencyType.HUF.toString() -> huf
    com.oztechan.ccc.common.core.model.CurrencyType.IDR.toString() -> idr
    com.oztechan.ccc.common.core.model.CurrencyType.ILS.toString() -> ils
    com.oztechan.ccc.common.core.model.CurrencyType.IMP.toString() -> imp
    com.oztechan.ccc.common.core.model.CurrencyType.INR.toString() -> inr
    com.oztechan.ccc.common.core.model.CurrencyType.IQD.toString() -> iqd
    com.oztechan.ccc.common.core.model.CurrencyType.IRR.toString() -> irr
    com.oztechan.ccc.common.core.model.CurrencyType.ISK.toString() -> isk
    com.oztechan.ccc.common.core.model.CurrencyType.JEP.toString() -> jep
    com.oztechan.ccc.common.core.model.CurrencyType.JMD.toString() -> jmd
    com.oztechan.ccc.common.core.model.CurrencyType.JOD.toString() -> jod
    com.oztechan.ccc.common.core.model.CurrencyType.JPY.toString() -> jpy
    com.oztechan.ccc.common.core.model.CurrencyType.KES.toString() -> kes
    com.oztechan.ccc.common.core.model.CurrencyType.KGS.toString() -> kgs
    com.oztechan.ccc.common.core.model.CurrencyType.KHR.toString() -> khr
    com.oztechan.ccc.common.core.model.CurrencyType.KMF.toString() -> kmf
    com.oztechan.ccc.common.core.model.CurrencyType.KPW.toString() -> kpw
    com.oztechan.ccc.common.core.model.CurrencyType.KRW.toString() -> krw
    com.oztechan.ccc.common.core.model.CurrencyType.KWD.toString() -> kwd
    com.oztechan.ccc.common.core.model.CurrencyType.KYD.toString() -> kyd
    com.oztechan.ccc.common.core.model.CurrencyType.KZT.toString() -> kzt
    com.oztechan.ccc.common.core.model.CurrencyType.LAK.toString() -> lak
    com.oztechan.ccc.common.core.model.CurrencyType.LBP.toString() -> lbp
    com.oztechan.ccc.common.core.model.CurrencyType.LKR.toString() -> lkr
    com.oztechan.ccc.common.core.model.CurrencyType.LRD.toString() -> lrd
    com.oztechan.ccc.common.core.model.CurrencyType.LSL.toString() -> lsl
    com.oztechan.ccc.common.core.model.CurrencyType.LYD.toString() -> lyd
    com.oztechan.ccc.common.core.model.CurrencyType.MAD.toString() -> mad
    com.oztechan.ccc.common.core.model.CurrencyType.MDL.toString() -> mdl
    com.oztechan.ccc.common.core.model.CurrencyType.MGA.toString() -> mga
    com.oztechan.ccc.common.core.model.CurrencyType.MKD.toString() -> mkd
    com.oztechan.ccc.common.core.model.CurrencyType.MMK.toString() -> mmk
    com.oztechan.ccc.common.core.model.CurrencyType.MNT.toString() -> mnt
    com.oztechan.ccc.common.core.model.CurrencyType.MOP.toString() -> mop
    com.oztechan.ccc.common.core.model.CurrencyType.MRO.toString() -> mro
    com.oztechan.ccc.common.core.model.CurrencyType.MRU.toString() -> mru
    com.oztechan.ccc.common.core.model.CurrencyType.MUR.toString() -> mur
    com.oztechan.ccc.common.core.model.CurrencyType.MVR.toString() -> mvr
    com.oztechan.ccc.common.core.model.CurrencyType.MWK.toString() -> mwk
    com.oztechan.ccc.common.core.model.CurrencyType.MXN.toString() -> mxn
    com.oztechan.ccc.common.core.model.CurrencyType.MYR.toString() -> myr
    com.oztechan.ccc.common.core.model.CurrencyType.MZN.toString() -> mzn
    com.oztechan.ccc.common.core.model.CurrencyType.NAD.toString() -> nad
    com.oztechan.ccc.common.core.model.CurrencyType.NGN.toString() -> ngn
    com.oztechan.ccc.common.core.model.CurrencyType.NIO.toString() -> nio
    com.oztechan.ccc.common.core.model.CurrencyType.NOK.toString() -> nok
    com.oztechan.ccc.common.core.model.CurrencyType.NPR.toString() -> npr
    com.oztechan.ccc.common.core.model.CurrencyType.NZD.toString() -> nzd
    com.oztechan.ccc.common.core.model.CurrencyType.OMR.toString() -> omr
    com.oztechan.ccc.common.core.model.CurrencyType.PAB.toString() -> pab
    com.oztechan.ccc.common.core.model.CurrencyType.PEN.toString() -> pen
    com.oztechan.ccc.common.core.model.CurrencyType.PGK.toString() -> pgk
    com.oztechan.ccc.common.core.model.CurrencyType.PHP.toString() -> php
    com.oztechan.ccc.common.core.model.CurrencyType.PKR.toString() -> pkr
    com.oztechan.ccc.common.core.model.CurrencyType.PLN.toString() -> pln
    com.oztechan.ccc.common.core.model.CurrencyType.PYG.toString() -> pyg
    com.oztechan.ccc.common.core.model.CurrencyType.QAR.toString() -> qar
    com.oztechan.ccc.common.core.model.CurrencyType.RON.toString() -> ron
    com.oztechan.ccc.common.core.model.CurrencyType.RSD.toString() -> rsd
    com.oztechan.ccc.common.core.model.CurrencyType.RUB.toString() -> rub
    com.oztechan.ccc.common.core.model.CurrencyType.RWF.toString() -> rwf
    com.oztechan.ccc.common.core.model.CurrencyType.SAR.toString() -> sar
    com.oztechan.ccc.common.core.model.CurrencyType.SBD.toString() -> sbd
    com.oztechan.ccc.common.core.model.CurrencyType.SCR.toString() -> scr
    com.oztechan.ccc.common.core.model.CurrencyType.SDG.toString() -> sdg
    com.oztechan.ccc.common.core.model.CurrencyType.SEK.toString() -> sek
    com.oztechan.ccc.common.core.model.CurrencyType.SGD.toString() -> sgd
    com.oztechan.ccc.common.core.model.CurrencyType.SHP.toString() -> shp
    com.oztechan.ccc.common.core.model.CurrencyType.SLL.toString() -> sll
    com.oztechan.ccc.common.core.model.CurrencyType.SOS.toString() -> sos
    com.oztechan.ccc.common.core.model.CurrencyType.SRD.toString() -> srd
    com.oztechan.ccc.common.core.model.CurrencyType.SSP.toString() -> ssp
    com.oztechan.ccc.common.core.model.CurrencyType.STD.toString() -> std
    com.oztechan.ccc.common.core.model.CurrencyType.STN.toString() -> stn
    com.oztechan.ccc.common.core.model.CurrencyType.SVC.toString() -> svc
    com.oztechan.ccc.common.core.model.CurrencyType.SYP.toString() -> syp
    com.oztechan.ccc.common.core.model.CurrencyType.SZL.toString() -> szl
    com.oztechan.ccc.common.core.model.CurrencyType.THB.toString() -> thb
    com.oztechan.ccc.common.core.model.CurrencyType.TJS.toString() -> tjs
    com.oztechan.ccc.common.core.model.CurrencyType.TMT.toString() -> tmt
    com.oztechan.ccc.common.core.model.CurrencyType.TND.toString() -> tnd
    com.oztechan.ccc.common.core.model.CurrencyType.TOP.toString() -> top
    com.oztechan.ccc.common.core.model.CurrencyType.TRY.toString() -> `try`
    com.oztechan.ccc.common.core.model.CurrencyType.TTD.toString() -> ttd
    com.oztechan.ccc.common.core.model.CurrencyType.TWD.toString() -> twd
    com.oztechan.ccc.common.core.model.CurrencyType.TZS.toString() -> tzs
    com.oztechan.ccc.common.core.model.CurrencyType.UAH.toString() -> uah
    com.oztechan.ccc.common.core.model.CurrencyType.UGX.toString() -> ugx
    com.oztechan.ccc.common.core.model.CurrencyType.USD.toString() -> usd
    com.oztechan.ccc.common.core.model.CurrencyType.UYU.toString() -> uyu
    com.oztechan.ccc.common.core.model.CurrencyType.UZS.toString() -> uzs
    com.oztechan.ccc.common.core.model.CurrencyType.VES.toString() -> ves
    com.oztechan.ccc.common.core.model.CurrencyType.VND.toString() -> vnd
    com.oztechan.ccc.common.core.model.CurrencyType.VUV.toString() -> vuv
    com.oztechan.ccc.common.core.model.CurrencyType.WST.toString() -> wst
    com.oztechan.ccc.common.core.model.CurrencyType.XAF.toString() -> xaf
    com.oztechan.ccc.common.core.model.CurrencyType.XAG.toString() -> xag
    com.oztechan.ccc.common.core.model.CurrencyType.XAU.toString() -> xau
    com.oztechan.ccc.common.core.model.CurrencyType.XCD.toString() -> xcd
    com.oztechan.ccc.common.core.model.CurrencyType.XDR.toString() -> xdr
    com.oztechan.ccc.common.core.model.CurrencyType.XOF.toString() -> xof
    com.oztechan.ccc.common.core.model.CurrencyType.XPD.toString() -> xpd
    com.oztechan.ccc.common.core.model.CurrencyType.XPF.toString() -> xpf
    com.oztechan.ccc.common.core.model.CurrencyType.XPT.toString() -> xpt
    com.oztechan.ccc.common.core.model.CurrencyType.YER.toString() -> yer
    com.oztechan.ccc.common.core.model.CurrencyType.ZAR.toString() -> zar
    com.oztechan.ccc.common.core.model.CurrencyType.ZMW.toString() -> zmw
    com.oztechan.ccc.common.core.model.CurrencyType.ZWL.toString() -> zwl
    else -> 0.0
}
