package mustafaozhan.github.com.mycurrencies.extensions

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.crashlytics.android.Crashlytics
import mustafaozhan.github.com.mycurrencies.main.fragment.model.CurrencyResponse
import mustafaozhan.github.com.mycurrencies.main.fragment.model.Rates
import mustafaozhan.github.com.mycurrencies.room.dao.CurrencyDao
import mustafaozhan.github.com.mycurrencies.room.model.Currency
import mustafaozhan.github.com.mycurrencies.room.model.OfflineRates
import mustafaozhan.github.com.mycurrencies.tools.Currencies

/**
 * Created by Mustafa Ozhan on 2018-07-20.
 */
fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

fun calculateResultByCurrency(name: String, value: String, rate: Rates?): Double {
    try {
        return when (Currencies.valueOf(name)) {
            Currencies.AED -> rate?.AED?.times(value.toDouble()) ?: value.toDouble()
            Currencies.AFN -> rate?.AFN?.times(value.toDouble()) ?: value.toDouble()
            Currencies.ALL -> rate?.ALL?.times(value.toDouble()) ?: value.toDouble()
            Currencies.AMD -> rate?.AMD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.ANG -> rate?.ANG?.times(value.toDouble()) ?: value.toDouble()
            Currencies.AOA -> rate?.AOA?.times(value.toDouble()) ?: value.toDouble()
            Currencies.ARS -> rate?.ARS?.times(value.toDouble()) ?: value.toDouble()
            Currencies.AUD -> rate?.AUD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.AWG -> rate?.AWG?.times(value.toDouble()) ?: value.toDouble()
            Currencies.AZN -> rate?.AZN?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BAM -> rate?.BAM?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BBD -> rate?.BBD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BDT -> rate?.BDT?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BGN -> rate?.BGN?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BHD -> rate?.BHD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BIF -> rate?.BIF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BMD -> rate?.BMD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BND -> rate?.BND?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BOB -> rate?.BOB?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BRL -> rate?.BRL?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BSD -> rate?.BSD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BTC -> rate?.BTC?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BTN -> rate?.BTN?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BWP -> rate?.BWP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BYN -> rate?.BYN?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BYR -> rate?.BYR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.BZD -> rate?.BZD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.CAD -> rate?.CAD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.CDF -> rate?.CDF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.CHF -> rate?.CHF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.CLF -> rate?.CLF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.CLP -> rate?.CLP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.CNY -> rate?.CNY?.times(value.toDouble()) ?: value.toDouble()
            Currencies.COP -> rate?.COP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.CRC -> rate?.CRC?.times(value.toDouble()) ?: value.toDouble()
            Currencies.CUC -> rate?.CUC?.times(value.toDouble()) ?: value.toDouble()
            Currencies.CUP -> rate?.CUP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.CVE -> rate?.CVE?.times(value.toDouble()) ?: value.toDouble()
            Currencies.CZK -> rate?.CZK?.times(value.toDouble()) ?: value.toDouble()
            Currencies.DJF -> rate?.DJF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.DKK -> rate?.DKK?.times(value.toDouble()) ?: value.toDouble()
            Currencies.DOP -> rate?.DOP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.DZD -> rate?.DZD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.EGP -> rate?.EGP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.ERN -> rate?.ERN?.times(value.toDouble()) ?: value.toDouble()
            Currencies.ETB -> rate?.ETB?.times(value.toDouble()) ?: value.toDouble()
            Currencies.EUR -> rate?.EUR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.FJD -> rate?.FJD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.FKP -> rate?.FKP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.GBP -> rate?.GBP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.GEL -> rate?.GEL?.times(value.toDouble()) ?: value.toDouble()
            Currencies.GGP -> rate?.GGP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.GHS -> rate?.GHS?.times(value.toDouble()) ?: value.toDouble()
            Currencies.GIP -> rate?.GIP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.GMD -> rate?.GMD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.GNF -> rate?.GNF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.GTQ -> rate?.GTQ?.times(value.toDouble()) ?: value.toDouble()
            Currencies.GYD -> rate?.GYD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.HKD -> rate?.HKD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.HNL -> rate?.HNL?.times(value.toDouble()) ?: value.toDouble()
            Currencies.HRK -> rate?.HRK?.times(value.toDouble()) ?: value.toDouble()
            Currencies.HTG -> rate?.HTG?.times(value.toDouble()) ?: value.toDouble()
            Currencies.HUF -> rate?.HUF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.IDR -> rate?.IDR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.ILS -> rate?.ILS?.times(value.toDouble()) ?: value.toDouble()
            Currencies.IMP -> rate?.IMP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.INR -> rate?.INR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.IQD -> rate?.IQD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.IRR -> rate?.IRR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.ISK -> rate?.ISK?.times(value.toDouble()) ?: value.toDouble()
            Currencies.JEP -> rate?.JEP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.JMD -> rate?.JMD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.JOD -> rate?.JOD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.JPY -> rate?.JPY?.times(value.toDouble()) ?: value.toDouble()
            Currencies.KES -> rate?.KES?.times(value.toDouble()) ?: value.toDouble()
            Currencies.KGS -> rate?.KGS?.times(value.toDouble()) ?: value.toDouble()
            Currencies.KHR -> rate?.KHR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.KMF -> rate?.KMF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.KPW -> rate?.KPW?.times(value.toDouble()) ?: value.toDouble()
            Currencies.KRW -> rate?.KRW?.times(value.toDouble()) ?: value.toDouble()
            Currencies.KWD -> rate?.KWD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.KYD -> rate?.KYD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.KZT -> rate?.KZT?.times(value.toDouble()) ?: value.toDouble()
            Currencies.LAK -> rate?.LAK?.times(value.toDouble()) ?: value.toDouble()
            Currencies.LBP -> rate?.LBP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.LKR -> rate?.LKR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.LRD -> rate?.LRD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.LSL -> rate?.LSL?.times(value.toDouble()) ?: value.toDouble()
            Currencies.LTL -> rate?.LTL?.times(value.toDouble()) ?: value.toDouble()
            Currencies.LVL -> rate?.LVL?.times(value.toDouble()) ?: value.toDouble()
            Currencies.LYD -> rate?.LYD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MAD -> rate?.MAD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MDL -> rate?.MDL?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MGA -> rate?.MGA?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MKD -> rate?.MKD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MMK -> rate?.MMK?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MNT -> rate?.MNT?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MOP -> rate?.MOP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MRO -> rate?.MRO?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MUR -> rate?.MUR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MVR -> rate?.MVR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MWK -> rate?.MWK?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MXN -> rate?.MXN?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MYR -> rate?.MYR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.MZN -> rate?.MZN?.times(value.toDouble()) ?: value.toDouble()
            Currencies.NAD -> rate?.NAD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.NGN -> rate?.NGN?.times(value.toDouble()) ?: value.toDouble()
            Currencies.NIO -> rate?.NIO?.times(value.toDouble()) ?: value.toDouble()
            Currencies.NOK -> rate?.NOK?.times(value.toDouble()) ?: value.toDouble()
            Currencies.NPR -> rate?.NPR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.NZD -> rate?.NZD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.OMR -> rate?.OMR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.PAB -> rate?.PAB?.times(value.toDouble()) ?: value.toDouble()
            Currencies.PEN -> rate?.PEN?.times(value.toDouble()) ?: value.toDouble()
            Currencies.PGK -> rate?.PGK?.times(value.toDouble()) ?: value.toDouble()
            Currencies.PHP -> rate?.PHP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.PKR -> rate?.PKR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.PLN -> rate?.PLN?.times(value.toDouble()) ?: value.toDouble()
            Currencies.PYG -> rate?.PYG?.times(value.toDouble()) ?: value.toDouble()
            Currencies.QAR -> rate?.QAR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.RON -> rate?.RON?.times(value.toDouble()) ?: value.toDouble()
            Currencies.RSD -> rate?.RSD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.RUB -> rate?.RUB?.times(value.toDouble()) ?: value.toDouble()
            Currencies.RWF -> rate?.RWF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SAR -> rate?.SAR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SBD -> rate?.SBD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SCR -> rate?.SCR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SDG -> rate?.SDG?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SEK -> rate?.SEK?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SGD -> rate?.SGD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SHP -> rate?.SHP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SLL -> rate?.SLL?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SOS -> rate?.SOS?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SRD -> rate?.SRD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.STD -> rate?.STD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SVC -> rate?.SVC?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SYP -> rate?.SYP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.SZL -> rate?.SZL?.times(value.toDouble()) ?: value.toDouble()
            Currencies.THB -> rate?.THB?.times(value.toDouble()) ?: value.toDouble()
            Currencies.TJS -> rate?.TJS?.times(value.toDouble()) ?: value.toDouble()
            Currencies.TMT -> rate?.TMT?.times(value.toDouble()) ?: value.toDouble()
            Currencies.TND -> rate?.TND?.times(value.toDouble()) ?: value.toDouble()
            Currencies.TOP -> rate?.TOP?.times(value.toDouble()) ?: value.toDouble()
            Currencies.TRY -> rate?.TRY?.times(value.toDouble()) ?: value.toDouble()
            Currencies.TTD -> rate?.TTD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.TWD -> rate?.TWD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.TZS -> rate?.TZS?.times(value.toDouble()) ?: value.toDouble()
            Currencies.UAH -> rate?.UAH?.times(value.toDouble()) ?: value.toDouble()
            Currencies.UGX -> rate?.UGX?.times(value.toDouble()) ?: value.toDouble()
            Currencies.USD -> rate?.USD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.UYU -> rate?.UYU?.times(value.toDouble()) ?: value.toDouble()
            Currencies.UZS -> rate?.UZS?.times(value.toDouble()) ?: value.toDouble()
            Currencies.VEF -> rate?.VEF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.VND -> rate?.VND?.times(value.toDouble()) ?: value.toDouble()
            Currencies.VUV -> rate?.VUV?.times(value.toDouble()) ?: value.toDouble()
            Currencies.WST -> rate?.WST?.times(value.toDouble()) ?: value.toDouble()
            Currencies.XAF -> rate?.XAF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.XAG -> rate?.XAG?.times(value.toDouble()) ?: value.toDouble()
            Currencies.XAU -> rate?.XAU?.times(value.toDouble()) ?: value.toDouble()
            Currencies.XCD -> rate?.XCD?.times(value.toDouble()) ?: value.toDouble()
            Currencies.XDR -> rate?.XDR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.XOF -> rate?.XOF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.XPF -> rate?.XPF?.times(value.toDouble()) ?: value.toDouble()
            Currencies.YER -> rate?.YER?.times(value.toDouble()) ?: value.toDouble()
            Currencies.ZAR -> rate?.ZAR?.times(value.toDouble()) ?: value.toDouble()
            Currencies.ZMK -> rate?.ZMK?.times(value.toDouble()) ?: value.toDouble()
            Currencies.ZMW -> rate?.ZMW?.times(value.toDouble()) ?: value.toDouble()
            Currencies.ZWL -> rate?.ZWL?.times(value.toDouble()) ?: value.toDouble()
            else -> 0.0
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Crashlytics.logException(e)
        return 0.0
    }
}

fun CurrencyDao.insertInitialCurrencies() {
    apply {
        insertCurrency(Currency("AED"))
        insertCurrency(Currency("AFN"))
        insertCurrency(Currency("ALL"))
        insertCurrency(Currency("AMD"))
        insertCurrency(Currency("ANG"))
        insertCurrency(Currency("AOA"))
        insertCurrency(Currency("ARS"))
        insertCurrency(Currency("AUD"))
        insertCurrency(Currency("AWG"))
        insertCurrency(Currency("AZN"))
        insertCurrency(Currency("BAM"))
        insertCurrency(Currency("BBD"))
        insertCurrency(Currency("BDT"))
        insertCurrency(Currency("BGN"))
        insertCurrency(Currency("BHD"))
        insertCurrency(Currency("BIF"))
        insertCurrency(Currency("BMD"))
        insertCurrency(Currency("BND"))
        insertCurrency(Currency("BOB"))
        insertCurrency(Currency("BRL"))
        insertCurrency(Currency("BSD"))
        insertCurrency(Currency("BTC"))
        insertCurrency(Currency("BTN"))
        insertCurrency(Currency("BWP"))
        insertCurrency(Currency("BYN"))
        insertCurrency(Currency("BYR"))
        insertCurrency(Currency("BZD"))
        insertCurrency(Currency("CAD"))
        insertCurrency(Currency("CDF"))
        insertCurrency(Currency("CHF"))
        insertCurrency(Currency("CLF"))
        insertCurrency(Currency("CLP"))
        insertCurrency(Currency("CNY"))
        insertCurrency(Currency("COP"))
        insertCurrency(Currency("CRC"))
        insertCurrency(Currency("CUC"))
        insertCurrency(Currency("CUP"))
        insertCurrency(Currency("CVE"))
        insertCurrency(Currency("CZK"))
        insertCurrency(Currency("DJF"))
        insertCurrency(Currency("DKK"))
        insertCurrency(Currency("DOP"))
        insertCurrency(Currency("DZD"))
        insertCurrency(Currency("EGP"))
        insertCurrency(Currency("ERN"))
        insertCurrency(Currency("ETB"))
        insertCurrency(Currency("EUR"))
        insertCurrency(Currency("FJD"))
        insertCurrency(Currency("FKP"))
        insertCurrency(Currency("GBP"))
        insertCurrency(Currency("GEL"))
        insertCurrency(Currency("GGP"))
        insertCurrency(Currency("GHS"))
        insertCurrency(Currency("GIP"))
        insertCurrency(Currency("GMD"))
        insertCurrency(Currency("GNF"))
        insertCurrency(Currency("GTQ"))
        insertCurrency(Currency("GYD"))
        insertCurrency(Currency("HKD"))
        insertCurrency(Currency("HNL"))
        insertCurrency(Currency("HRK"))
        insertCurrency(Currency("HTG"))
        insertCurrency(Currency("HUF"))
        insertCurrency(Currency("IDR"))
        insertCurrency(Currency("ILS"))
        insertCurrency(Currency("IMP"))
        insertCurrency(Currency("INR"))
        insertCurrency(Currency("IQD"))
        insertCurrency(Currency("IRR"))
        insertCurrency(Currency("ISK"))
        insertCurrency(Currency("JEP"))
        insertCurrency(Currency("JMD"))
        insertCurrency(Currency("JOD"))
        insertCurrency(Currency("JPY"))
        insertCurrency(Currency("KES"))
        insertCurrency(Currency("KGS"))
        insertCurrency(Currency("KHR"))
        insertCurrency(Currency("KMF"))
        insertCurrency(Currency("KPW"))
        insertCurrency(Currency("KRW"))
        insertCurrency(Currency("KWD"))
        insertCurrency(Currency("KYD"))
        insertCurrency(Currency("KZT"))
        insertCurrency(Currency("LAK"))
        insertCurrency(Currency("LBP"))
        insertCurrency(Currency("LKR"))
        insertCurrency(Currency("LRD"))
        insertCurrency(Currency("LSL"))
        insertCurrency(Currency("LTL"))
        insertCurrency(Currency("LVL"))
        insertCurrency(Currency("LYD"))
        insertCurrency(Currency("MAD"))
        insertCurrency(Currency("MDL"))
        insertCurrency(Currency("MGA"))
        insertCurrency(Currency("MKD"))
        insertCurrency(Currency("MMK"))
        insertCurrency(Currency("MNT"))
        insertCurrency(Currency("MOP"))
        insertCurrency(Currency("MRO"))
        insertCurrency(Currency("MUR"))
        insertCurrency(Currency("MVR"))
        insertCurrency(Currency("MWK"))
        insertCurrency(Currency("MXN"))
        insertCurrency(Currency("MYR"))
        insertCurrency(Currency("MZN"))
        insertCurrency(Currency("NAD"))
        insertCurrency(Currency("NGN"))
        insertCurrency(Currency("NIO"))
        insertCurrency(Currency("NOK"))
        insertCurrency(Currency("NPR"))
        insertCurrency(Currency("NZD"))
        insertCurrency(Currency("OMR"))
        insertCurrency(Currency("PAB"))
        insertCurrency(Currency("PEN"))
        insertCurrency(Currency("PGK"))
        insertCurrency(Currency("PHP"))
        insertCurrency(Currency("PKR"))
        insertCurrency(Currency("PLN"))
        insertCurrency(Currency("PYG"))
        insertCurrency(Currency("QAR"))
        insertCurrency(Currency("RON"))
        insertCurrency(Currency("RSD"))
        insertCurrency(Currency("RUB"))
        insertCurrency(Currency("RWF"))
        insertCurrency(Currency("SAR"))
        insertCurrency(Currency("SBD"))
        insertCurrency(Currency("SCR"))
        insertCurrency(Currency("SDG"))
        insertCurrency(Currency("SEK"))
        insertCurrency(Currency("SGD"))
        insertCurrency(Currency("SHP"))
        insertCurrency(Currency("SLL"))
        insertCurrency(Currency("SOS"))
        insertCurrency(Currency("SRD"))
        insertCurrency(Currency("STD"))
        insertCurrency(Currency("SVC"))
        insertCurrency(Currency("SYP"))
        insertCurrency(Currency("SZL"))
        insertCurrency(Currency("THB"))
        insertCurrency(Currency("TJS"))
        insertCurrency(Currency("TMT"))
        insertCurrency(Currency("TND"))
        insertCurrency(Currency("TOP"))
        insertCurrency(Currency("TRY"))
        insertCurrency(Currency("TTD"))
        insertCurrency(Currency("TWD"))
        insertCurrency(Currency("TZS"))
        insertCurrency(Currency("UAH"))
        insertCurrency(Currency("UGX"))
        insertCurrency(Currency("USD"))
        insertCurrency(Currency("UYU"))
        insertCurrency(Currency("UZS"))
        insertCurrency(Currency("VEF"))
        insertCurrency(Currency("VND"))
        insertCurrency(Currency("VUV"))
        insertCurrency(Currency("WST"))
        insertCurrency(Currency("XAF"))
        insertCurrency(Currency("XAG"))
        insertCurrency(Currency("XAU"))
        insertCurrency(Currency("XCD"))
        insertCurrency(Currency("XDR"))
        insertCurrency(Currency("XOF"))
        insertCurrency(Currency("XPF"))
        insertCurrency(Currency("YER"))
        insertCurrency(Currency("ZAR"))
        insertCurrency(Currency("ZMK"))
        insertCurrency(Currency("ZMW"))
        insertCurrency(Currency("ZWL"))


    }
}


fun CurrencyResponse.toOfflineRates() = OfflineRates(rates?.findBase().toString(),
        rates?.AED,
        rates?.AFN,
        rates?.ALL,
        rates?.AMD,
        rates?.ANG,
        rates?.AOA,
        rates?.ARS,
        rates?.AUD,
        rates?.AWG,
        rates?.AZN,
        rates?.BAM,
        rates?.BBD,
        rates?.BDT,
        rates?.BGN,
        rates?.BHD,
        rates?.BIF,
        rates?.BMD,
        rates?.BND,
        rates?.BOB,
        rates?.BRL,
        rates?.BSD,
        rates?.BTC,
        rates?.BTN,
        rates?.BWP,
        rates?.BYN,
        rates?.BYR,
        rates?.BZD,
        rates?.CAD,
        rates?.CDF,
        rates?.CHF,
        rates?.CLF,
        rates?.CLP,
        rates?.CNY,
        rates?.COP,
        rates?.CRC,
        rates?.CUC,
        rates?.CUP,
        rates?.CVE,
        rates?.CZK,
        rates?.DJF,
        rates?.DKK,
        rates?.DOP,
        rates?.DZD,
        rates?.EGP,
        rates?.ERN,
        rates?.ETB,
        rates?.EUR,
        rates?.FJD,
        rates?.FKP,
        rates?.GBP,
        rates?.GEL,
        rates?.GGP,
        rates?.GHS,
        rates?.GIP,
        rates?.GMD,
        rates?.GNF,
        rates?.GTQ,
        rates?.GYD,
        rates?.HKD,
        rates?.HNL,
        rates?.HRK,
        rates?.HTG,
        rates?.HUF,
        rates?.IDR,
        rates?.ILS,
        rates?.IMP,
        rates?.INR,
        rates?.IQD,
        rates?.IRR,
        rates?.ISK,
        rates?.JEP,
        rates?.JMD,
        rates?.JOD,
        rates?.JPY,
        rates?.KES,
        rates?.KGS,
        rates?.KHR,
        rates?.KMF,
        rates?.KPW,
        rates?.KRW,
        rates?.KWD,
        rates?.KYD,
        rates?.KZT,
        rates?.LAK,
        rates?.LBP,
        rates?.LKR,
        rates?.LRD,
        rates?.LSL,
        rates?.LTL,
        rates?.LVL,
        rates?.LYD,
        rates?.MAD,
        rates?.MDL,
        rates?.MGA,
        rates?.MKD,
        rates?.MMK,
        rates?.MNT,
        rates?.MOP,
        rates?.MRO,
        rates?.MUR,
        rates?.MVR,
        rates?.MWK,
        rates?.MXN,
        rates?.MYR,
        rates?.MZN,
        rates?.NAD,
        rates?.NGN,
        rates?.NIO,
        rates?.NOK,
        rates?.NPR,
        rates?.NZD,
        rates?.OMR,
        rates?.PAB,
        rates?.PEN,
        rates?.PGK,
        rates?.PHP,
        rates?.PKR,
        rates?.PLN,
        rates?.PYG,
        rates?.QAR,
        rates?.RON,
        rates?.RSD,
        rates?.RUB,
        rates?.RWF,
        rates?.SAR,
        rates?.SBD,
        rates?.SCR,
        rates?.SDG,
        rates?.SEK,
        rates?.SGD,
        rates?.SHP,
        rates?.SLL,
        rates?.SOS,
        rates?.SRD,
        rates?.STD,
        rates?.SVC,
        rates?.SYP,
        rates?.SZL,
        rates?.THB,
        rates?.TJS,
        rates?.TMT,
        rates?.TND,
        rates?.TOP,
        rates?.TRY,
        rates?.TTD,
        rates?.TWD,
        rates?.TZS,
        rates?.UAH,
        rates?.UGX,
        rates?.USD,
        rates?.UYU,
        rates?.UZS,
        rates?.VEF,
        rates?.VND,
        rates?.VUV,
        rates?.WST,
        rates?.XAF,
        rates?.XAG,
        rates?.XAU,
        rates?.XCD,
        rates?.XDR,
        rates?.XOF,
        rates?.XPF,
        rates?.YER,
        rates?.ZAR,
        rates?.ZMK,
        rates?.ZMW,
        rates?.ZWL

)


fun OfflineRates.getRates(): Rates? = Rates(
        AED,
        AFN,
        ALL,
        AMD,
        ANG,
        AOA,
        ARS,
        AUD,
        AWG,
        AZN,
        BAM,
        BBD,
        BDT,
        BGN,
        BHD,
        BIF,
        BMD,
        BND,
        BOB,
        BRL,
        BSD,
        BTC,
        BTN,
        BWP,
        BYN,
        BYR,
        BZD,
        CAD,
        CDF,
        CHF,
        CLF,
        CLP,
        CNY,
        COP,
        CRC,
        CUC,
        CUP,
        CVE,
        CZK,
        DJF,
        DKK,
        DOP,
        DZD,
        EGP,
        ERN,
        ETB,
        EUR,
        FJD,
        FKP,
        GBP,
        GEL,
        GGP,
        GHS,
        GIP,
        GMD,
        GNF,
        GTQ,
        GYD,
        HKD,
        HNL,
        HRK,
        HTG,
        HUF,
        IDR,
        ILS,
        IMP,
        INR,
        IQD,
        IRR,
        ISK,
        JEP,
        JMD,
        JOD,
        JPY,
        KES,
        KGS,
        KHR,
        KMF,
        KPW,
        KRW,
        KWD,
        KYD,
        KZT,
        LAK,
        LBP,
        LKR,
        LRD,
        LSL,
        LTL,
        LVL,
        LYD,
        MAD,
        MDL,
        MGA,
        MKD,
        MMK,
        MNT,
        MOP,
        MRO,
        MUR,
        MVR,
        MWK,
        MXN,
        MYR,
        MZN,
        NAD,
        NGN,
        NIO,
        NOK,
        NPR,
        NZD,
        OMR,
        PAB,
        PEN,
        PGK,
        PHP,
        PKR,
        PLN,
        PYG,
        QAR,
        RON,
        RSD,
        RUB,
        RWF,
        SAR,
        SBD,
        SCR,
        SDG,
        SEK,
        SGD,
        SHP,
        SLL,
        SOS,
        SRD,
        STD,
        SVC,
        SYP,
        SZL,
        THB,
        TJS,
        TMT,
        TND,
        TOP,
        TRY,
        TTD,
        TWD,
        TZS,
        UAH,
        UGX,
        USD,
        UYU,
        UZS,
        VEF,
        VND,
        VUV,
        WST,
        XAF,
        XAG,
        XAU,
        XCD,
        XDR,
        XOF,
        XPF,
        YER,
        ZAR,
        ZMK,
        ZMW,
        ZWL
)

fun Rates.findBase(): String {
    return when {
        AED == 1.0 || AED == null -> "AED"
        AFN == 1.0 || AFN == null -> "AFN"
        ALL == 1.0 || ALL == null -> "ALL"
        AMD == 1.0 || AMD == null -> "AMD"
        ANG == 1.0 || ANG == null -> "ANG"
        AOA == 1.0 || AOA == null -> "AOA"
        ARS == 1.0 || ARS == null -> "ARS"
        AUD == 1.0 || AUD == null -> "AUD"
        AWG == 1.0 || AWG == null -> "AWG"
        AZN == 1.0 || AZN == null -> "AZN"
        BAM == 1.0 || BAM == null -> "BAM"
        BBD == 1.0 || BBD == null -> "BBD"
        BDT == 1.0 || BDT == null -> "BDT"
        BGN == 1.0 || BGN == null -> "BGN"
        BHD == 1.0 || BHD == null -> "BHD"
        BIF == 1.0 || BIF == null -> "BIF"
        BMD == 1.0 || BMD == null -> "BMD"
        BND == 1.0 || BND == null -> "BND"
        BOB == 1.0 || BOB == null -> "BOB"
        BRL == 1.0 || BRL == null -> "BRL"
        BSD == 1.0 || BSD == null -> "BSD"
        BTC == 1.0 || BTC == null -> "BTC"
        BTN == 1.0 || BTN == null -> "BTN"
        BWP == 1.0 || BWP == null -> "BWP"
        BYN == 1.0 || BYN == null -> "BYN"
        BYR == 1.0 || BYR == null -> "BYR"
        BZD == 1.0 || BZD == null -> "BZD"
        CAD == 1.0 || CAD == null -> "CAD"
        CDF == 1.0 || CDF == null -> "CDF"
        CHF == 1.0 || CHF == null -> "CHF"
        CLF == 1.0 || CLF == null -> "CLF"
        CLP == 1.0 || CLP == null -> "CLP"
        CNY == 1.0 || CNY == null -> "CNY"
        COP == 1.0 || COP == null -> "COP"
        CRC == 1.0 || CRC == null -> "CRC"
        CUC == 1.0 || CUC == null -> "CUC"
        CUP == 1.0 || CUP == null -> "CUP"
        CVE == 1.0 || CVE == null -> "CVE"
        CZK == 1.0 || CZK == null -> "CZK"
        DJF == 1.0 || DJF == null -> "DJF"
        DKK == 1.0 || DKK == null -> "DKK"
        DOP == 1.0 || DOP == null -> "DOP"
        DZD == 1.0 || DZD == null -> "DZD"
        EGP == 1.0 || EGP == null -> "EGP"
        ERN == 1.0 || ERN == null -> "ERN"
        ETB == 1.0 || ETB == null -> "ETB"
        EUR == 1.0 || EUR == null -> "EUR"
        FJD == 1.0 || FJD == null -> "FJD"
        FKP == 1.0 || FKP == null -> "FKP"
        GBP == 1.0 || GBP == null -> "GBP"
        GEL == 1.0 || GEL == null -> "GEL"
        GGP == 1.0 || GGP == null -> "GGP"
        GHS == 1.0 || GHS == null -> "GHS"
        GIP == 1.0 || GIP == null -> "GIP"
        GMD == 1.0 || GMD == null -> "GMD"
        GNF == 1.0 || GNF == null -> "GNF"
        GTQ == 1.0 || GTQ == null -> "GTQ"
        GYD == 1.0 || GYD == null -> "GYD"
        HKD == 1.0 || HKD == null -> "HKD"
        HNL == 1.0 || HNL == null -> "HNL"
        HRK == 1.0 || HRK == null -> "HRK"
        HTG == 1.0 || HTG == null -> "HTG"
        HUF == 1.0 || HUF == null -> "HUF"
        IDR == 1.0 || IDR == null -> "IDR"
        ILS == 1.0 || ILS == null -> "ILS"
        IMP == 1.0 || IMP == null -> "IMP"
        INR == 1.0 || INR == null -> "INR"
        IQD == 1.0 || IQD == null -> "IQD"
        IRR == 1.0 || IRR == null -> "IRR"
        ISK == 1.0 || ISK == null -> "ISK"
        JEP == 1.0 || JEP == null -> "JEP"
        JMD == 1.0 || JMD == null -> "JMD"
        JOD == 1.0 || JOD == null -> "JOD"
        JPY == 1.0 || JPY == null -> "JPY"
        KES == 1.0 || KES == null -> "KES"
        KGS == 1.0 || KGS == null -> "KGS"
        KHR == 1.0 || KHR == null -> "KHR"
        KMF == 1.0 || KMF == null -> "KMF"
        KPW == 1.0 || KPW == null -> "KPW"
        KRW == 1.0 || KRW == null -> "KRW"
        KWD == 1.0 || KWD == null -> "KWD"
        KYD == 1.0 || KYD == null -> "KYD"
        KZT == 1.0 || KZT == null -> "KZT"
        LAK == 1.0 || LAK == null -> "LAK"
        LBP == 1.0 || LBP == null -> "LBP"
        LKR == 1.0 || LKR == null -> "LKR"
        LRD == 1.0 || LRD == null -> "LRD"
        LSL == 1.0 || LSL == null -> "LSL"
        LTL == 1.0 || LTL == null -> "LTL"
        LVL == 1.0 || LVL == null -> "LVL"
        LYD == 1.0 || LYD == null -> "LYD"
        MAD == 1.0 || MAD == null -> "MAD"
        MDL == 1.0 || MDL == null -> "MDL"
        MGA == 1.0 || MGA == null -> "MGA"
        MKD == 1.0 || MKD == null -> "MKD"
        MMK == 1.0 || MMK == null -> "MMK"
        MNT == 1.0 || MNT == null -> "MNT"
        MOP == 1.0 || MOP == null -> "MOP"
        MRO == 1.0 || MRO == null -> "MRO"
        MUR == 1.0 || MUR == null -> "MUR"
        MVR == 1.0 || MVR == null -> "MVR"
        MWK == 1.0 || MWK == null -> "MWK"
        MXN == 1.0 || MXN == null -> "MXN"
        MYR == 1.0 || MYR == null -> "MYR"
        MZN == 1.0 || MZN == null -> "MZN"
        NAD == 1.0 || NAD == null -> "NAD"
        NGN == 1.0 || NGN == null -> "NGN"
        NIO == 1.0 || NIO == null -> "NIO"
        NOK == 1.0 || NOK == null -> "NOK"
        NPR == 1.0 || NPR == null -> "NPR"
        NZD == 1.0 || NZD == null -> "NZD"
        OMR == 1.0 || OMR == null -> "OMR"
        PAB == 1.0 || PAB == null -> "PAB"
        PEN == 1.0 || PEN == null -> "PEN"
        PGK == 1.0 || PGK == null -> "PGK"
        PHP == 1.0 || PHP == null -> "PHP"
        PKR == 1.0 || PKR == null -> "PKR"
        PLN == 1.0 || PLN == null -> "PLN"
        PYG == 1.0 || PYG == null -> "PYG"
        QAR == 1.0 || QAR == null -> "QAR"
        RON == 1.0 || RON == null -> "RON"
        RSD == 1.0 || RSD == null -> "RSD"
        RUB == 1.0 || RUB == null -> "RUB"
        RWF == 1.0 || RWF == null -> "RWF"
        SAR == 1.0 || SAR == null -> "SAR"
        SBD == 1.0 || SBD == null -> "SBD"
        SCR == 1.0 || SCR == null -> "SCR"
        SDG == 1.0 || SDG == null -> "SDG"
        SEK == 1.0 || SEK == null -> "SEK"
        SGD == 1.0 || SGD == null -> "SGD"
        SHP == 1.0 || SHP == null -> "SHP"
        SLL == 1.0 || SLL == null -> "SLL"
        SOS == 1.0 || SOS == null -> "SOS"
        SRD == 1.0 || SRD == null -> "SRD"
        STD == 1.0 || STD == null -> "STD"
        SVC == 1.0 || SVC == null -> "SVC"
        SYP == 1.0 || SYP == null -> "SYP"
        SZL == 1.0 || SZL == null -> "SZL"
        THB == 1.0 || THB == null -> "THB"
        TJS == 1.0 || TJS == null -> "TJS"
        TMT == 1.0 || TMT == null -> "TMT"
        TND == 1.0 || TND == null -> "TND"
        TOP == 1.0 || TOP == null -> "TOP"
        TRY == 1.0 || TRY == null -> "TRY"
        TTD == 1.0 || TTD == null -> "TTD"
        TWD == 1.0 || TWD == null -> "TWD"
        TZS == 1.0 || TZS == null -> "TZS"
        UAH == 1.0 || UAH == null -> "UAH"
        UGX == 1.0 || UGX == null -> "UGX"
        USD == 1.0 || USD == null -> "USD"
        UYU == 1.0 || UYU == null -> "UYU"
        UZS == 1.0 || UZS == null -> "UZS"
        VEF == 1.0 || VEF == null -> "VEF"
        VND == 1.0 || VND == null -> "VND"
        VUV == 1.0 || VUV == null -> "VUV"
        WST == 1.0 || WST == null -> "WST"
        XAF == 1.0 || XAF == null -> "XAF"
        XAG == 1.0 || XAG == null -> "XAG"
        XAU == 1.0 || XAU == null -> "XAU"
        XCD == 1.0 || XCD == null -> "XCD"
        XDR == 1.0 || XDR == null -> "XDR"
        XOF == 1.0 || XOF == null -> "XOF"
        XPF == 1.0 || XPF == null -> "XPF"
        YER == 1.0 || YER == null -> "YER"
        ZAR == 1.0 || ZAR == null -> "ZAR"
        ZMK == 1.0 || ZMK == null -> "ZMK"
        ZMW == 1.0 || ZMW == null -> "ZMW"
        ZWL == 1.0 || ZWL == null -> "ZWL"

        else -> "NULL"
    }
}































