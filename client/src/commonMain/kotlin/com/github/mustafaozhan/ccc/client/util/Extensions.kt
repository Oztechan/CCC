/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("TooManyFunctions")

package com.github.mustafaozhan.ccc.client.util

import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.CurrencyType
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.ccc.common.util.nowAsInstant
import com.github.mustafaozhan.ccc.common.util.nowAsLong
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

private const val BIGGEST_DIGIT = 9

expect fun Double.getFormatted(): String

@Suppress("unused")
fun Any?.toUnit() = Unit

@Suppress("unused")
fun Any?.unitOrNull() = if (this == null) null else Unit

fun Long.isWeekPassed(): Boolean {
    return nowAsLong() - this >= WEEK
}

fun Long.isRewardExpired(): Boolean {
    return nowAsLong() - this >= VIDEO_REWARD
}

fun Long.toInstant() = Instant.fromEpochMilliseconds(this)

fun Long.toDateString(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = toInstant().toDateString(timeZone)

fun Instant.toDateString(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = toLocalDateTime(timeZone).run {
    "${hour.doubleDigits()}:${minute.doubleDigits()} " +
        "${dayOfMonth.doubleDigits()}.${monthNumber.doubleDigits()}.${year.doubleDigits()}"
}

fun Int.doubleDigits() = if (this <= BIGGEST_DIGIT) "0$this" else "$this"

fun CurrencyResponse.toRates(): Rates {
    val rate = rates
    rate.base = base
    rate.date = nowAsInstant().toDateString()
    return rate
}

fun Rates?.calculateResult(name: String, value: String?) =
    this?.whetherNot { value.isNullOrEmpty() }
        ?.getConversionByName(name)
        ?.times(value?.toSupportedCharacters()?.toStandardDigits()?.toDouble() ?: 0.0)
        ?: 0.0

fun String.toSupportedCharacters() =
    replace(",", ".")
        .replace("٫", ".")
        .replace(" ", "")
        .replace("−", "-")

fun String.isEmptyOrNullString() = isEmpty() || equals("null", true)

fun String.toStandardDigits(): String {
    val builder = StringBuilder()
    forEach { char ->
        char.toString().toIntOrNull()
            ?.whether { it >= 0 }
            ?.let { builder.append(it) }
            ?: run { builder.append(char) }
    }
    return builder.toString()
}

fun Currency.getCurrencyConversionByRate(base: String, rate: Rates?) =
    "1 $base = " + "${rate?.getConversionByName(name)} ${getVariablesOneLine()}"

fun List<Currency>?.toValidList(currentBase: String) =
    this?.filter {
        it.name != currentBase &&
            it.isActive &&
            it.rate.toString() != "NaN" &&
            it.rate.toString() != "0.0"
    } ?: mutableListOf()

@Suppress("MagicNumber")
fun RemoveAdType.calculateAdRewardEnd(startDate: Long = nowAsLong()) = when (this) {
    RemoveAdType.VIDEO -> startDate.toInstant().plus(
        3,
        DateTimeUnit.DAY,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
    RemoveAdType.MONTH -> startDate.toInstant().plus(
        1,
        DateTimeUnit.MONTH,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
    RemoveAdType.QUARTER -> startDate.toInstant().plus(
        3,
        DateTimeUnit.MONTH,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
    RemoveAdType.HALF_YEAR -> startDate.toInstant().plus(
        6,
        DateTimeUnit.MONTH,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
    RemoveAdType.YEAR -> startDate.toInstant().plus(
        1,
        DateTimeUnit.YEAR,
        TimeZone.currentSystemDefault()
    ).toEpochMilliseconds()
}

@Suppress("ComplexMethod", "LongMethod")
fun Rates.getConversionByName(name: String) = when (name.capitalize()) {
    CurrencyType.AED.toString() -> aED
    CurrencyType.AFN.toString() -> aFN
    CurrencyType.ALL.toString() -> aLL
    CurrencyType.AMD.toString() -> aMD
    CurrencyType.ANG.toString() -> aNG
    CurrencyType.AOA.toString() -> aOA
    CurrencyType.ARS.toString() -> aRS
    CurrencyType.AUD.toString() -> aUD
    CurrencyType.AWG.toString() -> aWG
    CurrencyType.AZN.toString() -> aZN
    CurrencyType.BAM.toString() -> bAM
    CurrencyType.BBD.toString() -> bBD
    CurrencyType.BDT.toString() -> bDT
    CurrencyType.BGN.toString() -> bGN
    CurrencyType.BHD.toString() -> bHD
    CurrencyType.BIF.toString() -> bIF
    CurrencyType.BMD.toString() -> bMD
    CurrencyType.BND.toString() -> bND
    CurrencyType.BOB.toString() -> bOB
    CurrencyType.BRL.toString() -> bRL
    CurrencyType.BSD.toString() -> bSD
    CurrencyType.BTC.toString() -> bTC
    CurrencyType.BTN.toString() -> bTN
    CurrencyType.BWP.toString() -> bWP
    CurrencyType.BYN.toString() -> bYN
    CurrencyType.BZD.toString() -> bZD
    CurrencyType.CAD.toString() -> cAD
    CurrencyType.CDF.toString() -> cDF
    CurrencyType.CHF.toString() -> cHF
    CurrencyType.CLF.toString() -> cLF
    CurrencyType.CLP.toString() -> cLP
    CurrencyType.CNH.toString() -> cNH
    CurrencyType.CNY.toString() -> cNY
    CurrencyType.COP.toString() -> cOP
    CurrencyType.CRC.toString() -> cRC
    CurrencyType.CUC.toString() -> cUC
    CurrencyType.CUP.toString() -> cUP
    CurrencyType.CVE.toString() -> cVE
    CurrencyType.CZK.toString() -> cZK
    CurrencyType.DJF.toString() -> dJF
    CurrencyType.DKK.toString() -> dKK
    CurrencyType.DOP.toString() -> dOP
    CurrencyType.DZD.toString() -> dZD
    CurrencyType.EGP.toString() -> eGP
    CurrencyType.ERN.toString() -> eRN
    CurrencyType.ETB.toString() -> eTB
    CurrencyType.EUR.toString() -> eUR
    CurrencyType.FJD.toString() -> fJD
    CurrencyType.FKP.toString() -> fKP
    CurrencyType.GBP.toString() -> gBP
    CurrencyType.GEL.toString() -> gEL
    CurrencyType.GGP.toString() -> gGP
    CurrencyType.GHS.toString() -> gHS
    CurrencyType.GIP.toString() -> gIP
    CurrencyType.GMD.toString() -> gMD
    CurrencyType.GNF.toString() -> gNF
    CurrencyType.GTQ.toString() -> gTQ
    CurrencyType.GYD.toString() -> gYD
    CurrencyType.HKD.toString() -> hKD
    CurrencyType.HNL.toString() -> hNL
    CurrencyType.HRK.toString() -> hRK
    CurrencyType.HTG.toString() -> hTG
    CurrencyType.HUF.toString() -> hUF
    CurrencyType.IDR.toString() -> iDR
    CurrencyType.ILS.toString() -> iLS
    CurrencyType.IMP.toString() -> iMP
    CurrencyType.INR.toString() -> iNR
    CurrencyType.IQD.toString() -> iQD
    CurrencyType.IRR.toString() -> iRR
    CurrencyType.ISK.toString() -> iSK
    CurrencyType.JEP.toString() -> jEP
    CurrencyType.JMD.toString() -> jMD
    CurrencyType.JOD.toString() -> jOD
    CurrencyType.JPY.toString() -> jPY
    CurrencyType.KES.toString() -> kES
    CurrencyType.KGS.toString() -> kGS
    CurrencyType.KHR.toString() -> kHR
    CurrencyType.KMF.toString() -> kMF
    CurrencyType.KPW.toString() -> kPW
    CurrencyType.KRW.toString() -> kRW
    CurrencyType.KWD.toString() -> kWD
    CurrencyType.KYD.toString() -> kYD
    CurrencyType.KZT.toString() -> kZT
    CurrencyType.LAK.toString() -> lAK
    CurrencyType.LBP.toString() -> lBP
    CurrencyType.LKR.toString() -> lKR
    CurrencyType.LRD.toString() -> lRD
    CurrencyType.LSL.toString() -> lSL
    CurrencyType.LYD.toString() -> lYD
    CurrencyType.MAD.toString() -> mAD
    CurrencyType.MDL.toString() -> mDL
    CurrencyType.MGA.toString() -> mGA
    CurrencyType.MKD.toString() -> mKD
    CurrencyType.MMK.toString() -> mMK
    CurrencyType.MNT.toString() -> mNT
    CurrencyType.MOP.toString() -> mOP
    CurrencyType.MRO.toString() -> mRO
    CurrencyType.MRU.toString() -> mRU
    CurrencyType.MUR.toString() -> mUR
    CurrencyType.MVR.toString() -> mVR
    CurrencyType.MWK.toString() -> mWK
    CurrencyType.MXN.toString() -> mXN
    CurrencyType.MYR.toString() -> mYR
    CurrencyType.MZN.toString() -> mZN
    CurrencyType.NAD.toString() -> nAD
    CurrencyType.NGN.toString() -> nGN
    CurrencyType.NIO.toString() -> nIO
    CurrencyType.NOK.toString() -> nOK
    CurrencyType.NPR.toString() -> nPR
    CurrencyType.NZD.toString() -> nZD
    CurrencyType.OMR.toString() -> oMR
    CurrencyType.PAB.toString() -> pAB
    CurrencyType.PEN.toString() -> pEN
    CurrencyType.PGK.toString() -> pGK
    CurrencyType.PHP.toString() -> pHP
    CurrencyType.PKR.toString() -> pKR
    CurrencyType.PLN.toString() -> pLN
    CurrencyType.PYG.toString() -> pYG
    CurrencyType.QAR.toString() -> qAR
    CurrencyType.RON.toString() -> rON
    CurrencyType.RSD.toString() -> rSD
    CurrencyType.RUB.toString() -> rUB
    CurrencyType.RWF.toString() -> rWF
    CurrencyType.SAR.toString() -> sAR
    CurrencyType.SBD.toString() -> sBD
    CurrencyType.SCR.toString() -> sCR
    CurrencyType.SDG.toString() -> sDG
    CurrencyType.SEK.toString() -> sEK
    CurrencyType.SGD.toString() -> sGD
    CurrencyType.SHP.toString() -> sHP
    CurrencyType.SLL.toString() -> sLL
    CurrencyType.SOS.toString() -> sOS
    CurrencyType.SRD.toString() -> sRD
    CurrencyType.SSP.toString() -> sSP
    CurrencyType.STD.toString() -> sTD
    CurrencyType.STN.toString() -> sTN
    CurrencyType.SVC.toString() -> sVC
    CurrencyType.SYP.toString() -> sYP
    CurrencyType.SZL.toString() -> sZL
    CurrencyType.THB.toString() -> tHB
    CurrencyType.TJS.toString() -> tJS
    CurrencyType.TMT.toString() -> tMT
    CurrencyType.TND.toString() -> tND
    CurrencyType.TOP.toString() -> tOP
    CurrencyType.TRY.toString() -> tRY
    CurrencyType.TTD.toString() -> tTD
    CurrencyType.TWD.toString() -> tWD
    CurrencyType.TZS.toString() -> tZS
    CurrencyType.UAH.toString() -> uAH
    CurrencyType.UGX.toString() -> uGX
    CurrencyType.USD.toString() -> uSD
    CurrencyType.UYU.toString() -> uYU
    CurrencyType.UZS.toString() -> uZS
    CurrencyType.VES.toString() -> vES
    CurrencyType.VND.toString() -> vND
    CurrencyType.VUV.toString() -> vUV
    CurrencyType.WST.toString() -> wST
    CurrencyType.XAF.toString() -> xAF
    CurrencyType.XAG.toString() -> xAG
    CurrencyType.XAU.toString() -> xAU
    CurrencyType.XCD.toString() -> xCD
    CurrencyType.XDR.toString() -> xDR
    CurrencyType.XOF.toString() -> xOF
    CurrencyType.XPD.toString() -> xPD
    CurrencyType.XPF.toString() -> xPF
    CurrencyType.XPT.toString() -> xPT
    CurrencyType.YER.toString() -> yER
    CurrencyType.ZAR.toString() -> zAR
    CurrencyType.ZMW.toString() -> zMW
    CurrencyType.ZWL.toString() -> zWL
    else -> 0.0
}
