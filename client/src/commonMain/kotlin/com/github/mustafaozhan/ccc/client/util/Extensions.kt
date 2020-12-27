/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
@file:Suppress("TooManyFunctions")

package com.github.mustafaozhan.ccc.client.util

import com.github.mustafaozhan.ccc.client.model.Currency
import com.github.mustafaozhan.ccc.common.model.CurrencyResponse
import com.github.mustafaozhan.ccc.common.model.Rates
import com.github.mustafaozhan.scopemob.whether
import com.github.mustafaozhan.scopemob.whetherNot
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

expect fun Double.getFormatted(): String

@Suppress("unused")
fun Any?.toUnit() = Unit

fun Long.isWeekPassed(): Boolean {
    return Clock.System.now().toEpochMilliseconds() - this >= WEEK
}

fun Long.isRewardExpired(): Boolean {
    return Clock.System.now().toEpochMilliseconds() - this >= AD_EXPIRATION
}

fun Instant.formatToString(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = toLocalDateTime(timeZone).run {
    "$hour:$minute $dayOfMonth.$monthNumber.$year"
}

fun CurrencyResponse.toRates(): Rates {
    val rate = rates
    rate.base = base
    rate.date = Clock.System.now().formatToString()
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

@Suppress("ComplexMethod", "LongMethod")
fun Rates.getConversionByName(name: String) = when (name.capitalize()) {
    "AED" -> aED
    "AFN" -> aFN
    "ALL" -> aLL
    "AMD" -> aMD
    "ANG" -> aNG
    "AOA" -> aOA
    "ARS" -> aRS
    "AUD" -> aUD
    "AWG" -> aWG
    "AZN" -> aZN
    "BAM" -> bAM
    "BBD" -> bBD
    "BDT" -> bDT
    "BGN" -> bGN
    "BHD" -> bHD
    "BIF" -> bIF
    "BMD" -> bMD
    "BND" -> bND
    "BOB" -> bOB
    "BRL" -> bRL
    "BSD" -> bSD
    "BTN" -> bTC
    "BWP" -> bTN
    "BYN" -> bWP
    "BYR" -> bYN
    "BZD" -> bYR
    "CAD" -> bZD
    "CDF" -> cAD
    "CHF" -> cDF
    "CLF" -> cHF
    "CLP" -> cLF
    "CNY" -> cLP
    "COP" -> cNY
    "CRC" -> cOP
    "BTC" -> cRC
    "CUC" -> cUC
    "CUP" -> cUP
    "CVE" -> cVE
    "CZK" -> cZK
    "DJF" -> dJF
    "DKK" -> dKK
    "DOP" -> dOP
    "DZD" -> dZD
    "EGP" -> eGP
    "ERN" -> eRN
    "ETB" -> eTB
    "EUR" -> eUR
    "FJD" -> fJD
    "FKP" -> fKP
    "GBP" -> gBP
    "GEL" -> gEL
    "GGP" -> gGP
    "GHS" -> gHS
    "GIP" -> gIP
    "GMD" -> gMD
    "GNF" -> gNF
    "GTQ" -> gTQ
    "GYD" -> gYD
    "HKD" -> hKD
    "HNL" -> hNL
    "HRK" -> hRK
    "HTG" -> hTG
    "HUF" -> hUF
    "IDR" -> iDR
    "ILS" -> iLS
    "IMP" -> iMP
    "INR" -> iNR
    "IQD" -> iQD
    "IRR" -> iRR
    "ISK" -> iSK
    "JEP" -> jEP
    "JMD" -> jMD
    "JOD" -> jOD
    "JPY" -> jPY
    "KES" -> kES
    "KGS" -> kGS
    "KHR" -> kHR
    "KMF" -> kMF
    "KPW" -> kPW
    "KRW" -> kRW
    "KWD" -> kWD
    "KYD" -> kYD
    "KZT" -> kZT
    "LAK" -> lAK
    "LBP" -> lBP
    "LKR" -> lKR
    "LRD" -> lRD
    "LSL" -> lSL
    "LTL" -> lTL
    "LVL" -> lVL
    "LYD" -> lYD
    "MAD" -> mAD
    "MDL" -> mDL
    "MGA" -> mGA
    "MKD" -> mKD
    "MMK" -> mMK
    "MNT" -> mNT
    "MOP" -> mOP
    "MRO" -> mRO
    "MUR" -> mUR
    "MVR" -> mVR
    "MWK" -> mWK
    "MXN" -> mXN
    "MYR" -> mYR
    "MZN" -> mZN
    "NAD" -> nAD
    "NGN" -> nGN
    "NIO" -> nIO
    "NOK" -> nOK
    "NPR" -> nPR
    "NZD" -> nZD
    "OMR" -> oMR
    "PAB" -> pAB
    "PEN" -> pEN
    "PGK" -> pGK
    "PHP" -> pHP
    "PKR" -> pKR
    "PLN" -> pLN
    "PYG" -> pYG
    "QAR" -> qAR
    "RON" -> rON
    "RSD" -> rSD
    "RUB" -> rUB
    "RWF" -> rWF
    "SAR" -> sAR
    "SBD" -> sBD
    "SCR" -> sCR
    "SDG" -> sDG
    "SEK" -> sEK
    "SGD" -> sGD
    "SHP" -> sHP
    "SLL" -> sLL
    "SOS" -> sOS
    "SRD" -> sRD
    "STD" -> sTD
    "SVC" -> sVC
    "SYP" -> sYP
    "SZL" -> sZL
    "THB" -> tHB
    "TJS" -> tJS
    "TMT" -> tMT
    "TND" -> tND
    "TOP" -> tOP
    "TRY" -> tRY
    "TTD" -> tTD
    "TWD" -> tWD
    "TZS" -> tZS
    "UAH" -> uAH
    "UGX" -> uGX
    "USD" -> uSD
    "UYU" -> uYU
    "UZS" -> uZS
    "VEF" -> vEF
    "VES" -> vES
    "VND" -> vND
    "VUV" -> vUV
    "WST" -> wST
    "XAF" -> xAF
    "XAG" -> xAG
    "XAU" -> xAU
    "XCD" -> xCD
    "XDR" -> xDR
    "XOF" -> xOF
    "XPF" -> xPF
    "YER" -> yER
    "ZAR" -> zAR
    "ZMK" -> zMK
    "ZMW" -> zMW
    "ZWL" -> zWL
    else -> 0.0
}
