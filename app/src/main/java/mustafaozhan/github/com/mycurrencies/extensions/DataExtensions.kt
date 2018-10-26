package mustafaozhan.github.com.mycurrencies.extensions

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
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

fun getResult(name: String, temp: String, rate: Rates?) = when (name) {
    Currencies.AED.toString() -> rate?.AED?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.AFN.toString() -> rate?.AFN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ALL.toString() -> rate?.ALL?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.AMD.toString() -> rate?.AMD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ANG.toString() -> rate?.ANG?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.AOA.toString() -> rate?.AOA?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ARS.toString() -> rate?.ARS?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.AUD.toString() -> rate?.AUD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.AWG.toString() -> rate?.AWG?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.AZN.toString() -> rate?.AZN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BAM.toString() -> rate?.BAM?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BBD.toString() -> rate?.BBD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BDT.toString() -> rate?.BDT?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BGN.toString() -> rate?.BGN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BHD.toString() -> rate?.BHD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BIF.toString() -> rate?.BIF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BMD.toString() -> rate?.BMD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BND.toString() -> rate?.BND?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BOB.toString() -> rate?.BOB?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BRL.toString() -> rate?.BRL?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BSD.toString() -> rate?.BSD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BTC.toString() -> rate?.BTC?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BTN.toString() -> rate?.BTN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BWP.toString() -> rate?.BWP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BYN.toString() -> rate?.BYN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BYR.toString() -> rate?.BYR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BZD.toString() -> rate?.BZD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CAD.toString() -> rate?.CAD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CDF.toString() -> rate?.CDF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CHF.toString() -> rate?.CHF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CLF.toString() -> rate?.CLF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CLP.toString() -> rate?.CLP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CNY.toString() -> rate?.CNY?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.COP.toString() -> rate?.COP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CRC.toString() -> rate?.CRC?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CUC.toString() -> rate?.CUC?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CUP.toString() -> rate?.CUP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CVE.toString() -> rate?.CVE?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CZK.toString() -> rate?.CZK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.DJF.toString() -> rate?.DJF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.DKK.toString() -> rate?.DKK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.DOP.toString() -> rate?.DOP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.DZD.toString() -> rate?.DZD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.EGP.toString() -> rate?.EGP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ERN.toString() -> rate?.ERN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ETB.toString() -> rate?.ETB?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.EUR.toString() -> rate?.EUR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.FJD.toString() -> rate?.FJD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.FKP.toString() -> rate?.FKP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.GBP.toString() -> rate?.GBP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.GEL.toString() -> rate?.GEL?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.GGP.toString() -> rate?.GGP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.GHS.toString() -> rate?.GHS?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.GIP.toString() -> rate?.GIP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.GMD.toString() -> rate?.GMD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.GNF.toString() -> rate?.GNF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.GTQ.toString() -> rate?.GTQ?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.GYD.toString() -> rate?.GYD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.HKD.toString() -> rate?.HKD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.HNL.toString() -> rate?.HNL?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.HRK.toString() -> rate?.HRK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.HTG.toString() -> rate?.HTG?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.HUF.toString() -> rate?.HUF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.IDR.toString() -> rate?.IDR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ILS.toString() -> rate?.ILS?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.IMP.toString() -> rate?.IMP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.INR.toString() -> rate?.INR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.IQD.toString() -> rate?.IQD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.IRR.toString() -> rate?.IRR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ISK.toString() -> rate?.ISK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.JEP.toString() -> rate?.JEP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.JMD.toString() -> rate?.JMD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.JOD.toString() -> rate?.JOD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.JPY.toString() -> rate?.JPY?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.KES.toString() -> rate?.KES?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.KGS.toString() -> rate?.KGS?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.KHR.toString() -> rate?.KHR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.KMF.toString() -> rate?.KMF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.KPW.toString() -> rate?.KPW?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.KRW.toString() -> rate?.KRW?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.KWD.toString() -> rate?.KWD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.KYD.toString() -> rate?.KYD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.KZT.toString() -> rate?.KZT?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.LAK.toString() -> rate?.LAK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.LBP.toString() -> rate?.LBP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.LKR.toString() -> rate?.LKR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.LRD.toString() -> rate?.LRD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.LSL.toString() -> rate?.LSL?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.LTL.toString() -> rate?.LTL?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.LVL.toString() -> rate?.LVL?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.LYD.toString() -> rate?.LYD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MAD.toString() -> rate?.MAD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MDL.toString() -> rate?.MDL?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MGA.toString() -> rate?.MGA?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MKD.toString() -> rate?.MKD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MMK.toString() -> rate?.MMK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MNT.toString() -> rate?.MNT?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MOP.toString() -> rate?.MOP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MRO.toString() -> rate?.MRO?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MUR.toString() -> rate?.MUR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MVR.toString() -> rate?.MVR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MWK.toString() -> rate?.MWK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MXN.toString() -> rate?.MXN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MYR.toString() -> rate?.MYR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MZN.toString() -> rate?.MZN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.NAD.toString() -> rate?.NAD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.NGN.toString() -> rate?.NGN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.NIO.toString() -> rate?.NIO?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.NOK.toString() -> rate?.NOK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.NPR.toString() -> rate?.NPR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.NZD.toString() -> rate?.NZD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.OMR.toString() -> rate?.OMR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.PAB.toString() -> rate?.PAB?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.PEN.toString() -> rate?.PEN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.PGK.toString() -> rate?.PGK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.PHP.toString() -> rate?.PHP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.PKR.toString() -> rate?.PKR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.PLN.toString() -> rate?.PLN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.PYG.toString() -> rate?.PYG?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.QAR.toString() -> rate?.QAR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.RON.toString() -> rate?.RON?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.RSD.toString() -> rate?.RSD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.RUB.toString() -> rate?.RUB?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.RWF.toString() -> rate?.RWF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SAR.toString() -> rate?.SAR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SBD.toString() -> rate?.SBD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SCR.toString() -> rate?.SCR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SDG.toString() -> rate?.SDG?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SEK.toString() -> rate?.SEK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SGD.toString() -> rate?.SGD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SHP.toString() -> rate?.SHP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SLL.toString() -> rate?.SLL?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SOS.toString() -> rate?.SOS?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SRD.toString() -> rate?.SRD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.STD.toString() -> rate?.STD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SVC.toString() -> rate?.SVC?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SYP.toString() -> rate?.SYP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SZL.toString() -> rate?.SZL?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.THB.toString() -> rate?.THB?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.TJS.toString() -> rate?.TJS?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.TMT.toString() -> rate?.TMT?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.TND.toString() -> rate?.TND?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.TOP.toString() -> rate?.TOP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.TRY.toString() -> rate?.TRY?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.TTD.toString() -> rate?.TTD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.TWD.toString() -> rate?.TWD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.TZS.toString() -> rate?.TZS?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.UAH.toString() -> rate?.UAH?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.UGX.toString() -> rate?.UGX?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.USD.toString() -> rate?.USD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.UYU.toString() -> rate?.UYU?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.UZS.toString() -> rate?.UZS?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.VEF.toString() -> rate?.VEF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.VND.toString() -> rate?.VND?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.VUV.toString() -> rate?.VUV?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.WST.toString() -> rate?.WST?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.XAF.toString() -> rate?.XAF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.XAG.toString() -> rate?.XAG?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.XAU.toString() -> rate?.XAU?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.XCD.toString() -> rate?.XCD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.XDR.toString() -> rate?.XDR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.XOF.toString() -> rate?.XOF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.XPF.toString() -> rate?.XPF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.YER.toString() -> rate?.YER?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ZAR.toString() -> rate?.ZAR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ZMK.toString() -> rate?.ZMK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ZMW.toString() -> rate?.ZMW?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ZWL.toString() -> rate?.ZWL?.times(temp.toDouble()) ?: temp.toDouble()

    else -> 0.0
}

fun CurrencyDao.insertInitialCurrencies() {
    this.apply {
        insertCurrency(Currency("EUR"))
        insertCurrency(Currency("AUD"))
        insertCurrency(Currency("BGN"))
        insertCurrency(Currency("BRL"))
        insertCurrency(Currency("CAD"))
        insertCurrency(Currency("CHF"))
        insertCurrency(Currency("CNY"))
        insertCurrency(Currency("CZK"))
        insertCurrency(Currency("DKK"))
        insertCurrency(Currency("GBP"))
        insertCurrency(Currency("HKD"))
        insertCurrency(Currency("HRK"))
        insertCurrency(Currency("HUF"))
        insertCurrency(Currency("IDR"))
        insertCurrency(Currency("ILS"))
        insertCurrency(Currency("INR"))
        insertCurrency(Currency("JPY"))
        insertCurrency(Currency("KRW"))
        insertCurrency(Currency("MXN"))
        insertCurrency(Currency("MYR"))
        insertCurrency(Currency("NOK"))
        insertCurrency(Currency("NZD"))
        insertCurrency(Currency("PHP"))
        insertCurrency(Currency("PLN"))
        insertCurrency(Currency("RON"))
        insertCurrency(Currency("RUB"))
        insertCurrency(Currency("SEK"))
        insertCurrency(Currency("SGD"))
        insertCurrency(Currency("THB"))
        insertCurrency(Currency("TRY"))
        insertCurrency(Currency("USD"))
        insertCurrency(Currency("ZAR"))
        insertCurrency(Currency("EUR"))
        insertCurrency(Currency("AUD"))
        insertCurrency(Currency("BGN"))
        insertCurrency(Currency("BRL"))
        insertCurrency(Currency("CAD"))
        insertCurrency(Currency("CHF"))
        insertCurrency(Currency("CNY"))
        insertCurrency(Currency("CZK"))
        insertCurrency(Currency("DKK"))
        insertCurrency(Currency("GBP"))
        insertCurrency(Currency("HKD"))
        insertCurrency(Currency("HRK"))
        insertCurrency(Currency("HUF"))
        insertCurrency(Currency("IDR"))
        insertCurrency(Currency("ILS"))
        insertCurrency(Currency("INR"))
        insertCurrency(Currency("JPY"))
        insertCurrency(Currency("KRW"))
        insertCurrency(Currency("MXN"))
        insertCurrency(Currency("MYR"))
        insertCurrency(Currency("NOK"))
        insertCurrency(Currency("NZD"))
        insertCurrency(Currency("PHP"))
        insertCurrency(Currency("PLN"))
        insertCurrency(Currency("RON"))
        insertCurrency(Currency("RUB"))
        insertCurrency(Currency("SEK"))
        insertCurrency(Currency("SGD"))
        insertCurrency(Currency("THB"))
        insertCurrency(Currency("TRY"))
        insertCurrency(Currency("USD"))
        insertCurrency(Currency("ZAR"))
        insertCurrency(Currency("EUR"))
        insertCurrency(Currency("AUD"))
        insertCurrency(Currency("BGN"))
        insertCurrency(Currency("BRL"))
        insertCurrency(Currency("CAD"))
        insertCurrency(Currency("CHF"))
        insertCurrency(Currency("CNY"))
        insertCurrency(Currency("CZK"))
        insertCurrency(Currency("DKK"))
        insertCurrency(Currency("GBP"))
        insertCurrency(Currency("HKD"))
        insertCurrency(Currency("HRK"))
        insertCurrency(Currency("HUF"))
        insertCurrency(Currency("IDR"))
        insertCurrency(Currency("ILS"))
        insertCurrency(Currency("INR"))
        insertCurrency(Currency("JPY"))
        insertCurrency(Currency("KRW"))
        insertCurrency(Currency("MXN"))
        insertCurrency(Currency("MYR"))
        insertCurrency(Currency("NOK"))
        insertCurrency(Currency("NZD"))
        insertCurrency(Currency("PHP"))
        insertCurrency(Currency("PLN"))
        insertCurrency(Currency("RON"))
        insertCurrency(Currency("RUB"))
        insertCurrency(Currency("SEK"))
        insertCurrency(Currency("SGD"))
        insertCurrency(Currency("THB"))
        insertCurrency(Currency("TRY"))
        insertCurrency(Currency("USD"))
        insertCurrency(Currency("ZAR"))
        insertCurrency(Currency("EUR"))
        insertCurrency(Currency("AUD"))
        insertCurrency(Currency("BGN"))
        insertCurrency(Currency("BRL"))
        insertCurrency(Currency("CAD"))
        insertCurrency(Currency("CHF"))
        insertCurrency(Currency("CNY"))
        insertCurrency(Currency("CZK"))
        insertCurrency(Currency("DKK"))
        insertCurrency(Currency("GBP"))
        insertCurrency(Currency("HKD"))
        insertCurrency(Currency("HRK"))
        insertCurrency(Currency("HUF"))
        insertCurrency(Currency("IDR"))
        insertCurrency(Currency("ILS"))
        insertCurrency(Currency("INR"))
        insertCurrency(Currency("JPY"))
        insertCurrency(Currency("KRW"))
        insertCurrency(Currency("MXN"))
        insertCurrency(Currency("MYR"))
        insertCurrency(Currency("NOK"))
        insertCurrency(Currency("NZD"))
        insertCurrency(Currency("PHP"))
        insertCurrency(Currency("PLN"))
        insertCurrency(Currency("RON"))
        insertCurrency(Currency("RUB"))
        insertCurrency(Currency("SEK"))
        insertCurrency(Currency("SGD"))
        insertCurrency(Currency("THB"))
        insertCurrency(Currency("TRY"))
        insertCurrency(Currency("USD"))
        insertCurrency(Currency("ZAR"))
        insertCurrency(Currency("EUR"))
        insertCurrency(Currency("AUD"))
        insertCurrency(Currency("BGN"))
        insertCurrency(Currency("BRL"))
        insertCurrency(Currency("CAD"))
        insertCurrency(Currency("CHF"))
        insertCurrency(Currency("CNY"))
        insertCurrency(Currency("CZK"))
        insertCurrency(Currency("DKK"))
        insertCurrency(Currency("GBP"))
        insertCurrency(Currency("HKD"))
        insertCurrency(Currency("HRK"))
        insertCurrency(Currency("HUF"))
        insertCurrency(Currency("IDR"))
        insertCurrency(Currency("ILS"))
        insertCurrency(Currency("INR"))
        insertCurrency(Currency("JPY"))
        insertCurrency(Currency("KRW"))
        insertCurrency(Currency("MXN"))
        insertCurrency(Currency("MYR"))
        insertCurrency(Currency("NOK"))
        insertCurrency(Currency("NZD"))
        insertCurrency(Currency("PHP"))
        insertCurrency(Currency("PLN"))
        insertCurrency(Currency("RON"))
        insertCurrency(Currency("RUB"))
        insertCurrency(Currency("SEK"))
        insertCurrency(Currency("SGD"))
        insertCurrency(Currency("THB"))
        insertCurrency(Currency("TRY"))
        insertCurrency(Currency("USD"))
        insertCurrency(Currency("ZAR"))
        insertCurrency(Currency("EUR"))
        insertCurrency(Currency("AUD"))
        insertCurrency(Currency("BGN"))
        insertCurrency(Currency("BRL"))
        insertCurrency(Currency("CAD"))
        insertCurrency(Currency("CHF"))
        insertCurrency(Currency("CNY"))
        insertCurrency(Currency("CZK"))
        insertCurrency(Currency("DKK"))
        insertCurrency(Currency("GBP"))
        insertCurrency(Currency("HKD"))
        insertCurrency(Currency("HRK"))
        insertCurrency(Currency("HUF"))
        insertCurrency(Currency("IDR"))
        insertCurrency(Currency("ILS"))
        insertCurrency(Currency("INR"))
        insertCurrency(Currency("JPY"))
        insertCurrency(Currency("KRW"))
        insertCurrency(Currency("MXN"))
        insertCurrency(Currency("MYR"))
        insertCurrency(Currency("NOK"))
        insertCurrency(Currency("NZD"))
        insertCurrency(Currency("PHP"))
        insertCurrency(Currency("PLN"))
        insertCurrency(Currency("RON"))
        insertCurrency(Currency("RUB"))
        insertCurrency(Currency("SEK"))
        insertCurrency(Currency("SGD"))
        insertCurrency(Currency("THB"))
        insertCurrency(Currency("TRY"))
        insertCurrency(Currency("USD"))
        insertCurrency(Currency("ZAR"))

    }
}


fun CurrencyResponse.toOfflineRates() = OfflineRates(this.rates?.findBase().toString(),
        this.rates?.AED,
        this.rates?.AFN,
        this.rates?.ALL,
        this.rates?.AMD,
        this.rates?.ANG,
        this.rates?.AOA,
        this.rates?.ARS,
        this.rates?.AUD,
        this.rates?.AWG,
        this.rates?.AZN,
        this.rates?.BAM,
        this.rates?.BBD,
        this.rates?.BDT,
        this.rates?.BGN,
        this.rates?.BHD,
        this.rates?.BIF,
        this.rates?.BMD,
        this.rates?.BND,
        this.rates?.BOB,
        this.rates?.BRL,
        this.rates?.BSD,
        this.rates?.BTC,
        this.rates?.BTN,
        this.rates?.BWP,
        this.rates?.BYN,
        this.rates?.BYR,
        this.rates?.BZD,
        this.rates?.CAD,
        this.rates?.CDF,
        this.rates?.CHF,
        this.rates?.CLF,
        this.rates?.CLP,
        this.rates?.CNY,
        this.rates?.COP,
        this.rates?.CRC,
        this.rates?.CUC,
        this.rates?.CUP,
        this.rates?.CVE,
        this.rates?.CZK,
        this.rates?.DJF,
        this.rates?.DKK,
        this.rates?.DOP,
        this.rates?.DZD,
        this.rates?.EGP,
        this.rates?.ERN,
        this.rates?.ETB,
        this.rates?.EUR,
        this.rates?.FJD,
        this.rates?.FKP,
        this.rates?.GBP,
        this.rates?.GEL,
        this.rates?.GGP,
        this.rates?.GHS,
        this.rates?.GIP,
        this.rates?.GMD,
        this.rates?.GNF,
        this.rates?.GTQ,
        this.rates?.GYD,
        this.rates?.HKD,
        this.rates?.HNL,
        this.rates?.HRK,
        this.rates?.HTG,
        this.rates?.HUF,
        this.rates?.IDR,
        this.rates?.ILS,
        this.rates?.IMP,
        this.rates?.INR,
        this.rates?.IQD,
        this.rates?.IRR,
        this.rates?.ISK,
        this.rates?.JEP,
        this.rates?.JMD,
        this.rates?.JOD,
        this.rates?.JPY,
        this.rates?.KES,
        this.rates?.KGS,
        this.rates?.KHR,
        this.rates?.KMF,
        this.rates?.KPW,
        this.rates?.KRW,
        this.rates?.KWD,
        this.rates?.KYD,
        this.rates?.KZT,
        this.rates?.LAK,
        this.rates?.LBP,
        this.rates?.LKR,
        this.rates?.LRD,
        this.rates?.LSL,
        this.rates?.LTL,
        this.rates?.LVL,
        this.rates?.LYD,
        this.rates?.MAD,
        this.rates?.MDL,
        this.rates?.MGA,
        this.rates?.MKD,
        this.rates?.MMK,
        this.rates?.MNT,
        this.rates?.MOP,
        this.rates?.MRO,
        this.rates?.MUR,
        this.rates?.MVR,
        this.rates?.MWK,
        this.rates?.MXN,
        this.rates?.MYR,
        this.rates?.MZN,
        this.rates?.NAD,
        this.rates?.NGN,
        this.rates?.NIO,
        this.rates?.NOK,
        this.rates?.NPR,
        this.rates?.NZD,
        this.rates?.OMR,
        this.rates?.PAB,
        this.rates?.PEN,
        this.rates?.PGK,
        this.rates?.PHP,
        this.rates?.PKR,
        this.rates?.PLN,
        this.rates?.PYG,
        this.rates?.QAR,
        this.rates?.RON,
        this.rates?.RSD,
        this.rates?.RUB,
        this.rates?.RWF,
        this.rates?.SAR,
        this.rates?.SBD,
        this.rates?.SCR,
        this.rates?.SDG,
        this.rates?.SEK,
        this.rates?.SGD,
        this.rates?.SHP,
        this.rates?.SLL,
        this.rates?.SOS,
        this.rates?.SRD,
        this.rates?.STD,
        this.rates?.SVC,
        this.rates?.SYP,
        this.rates?.SZL,
        this.rates?.THB,
        this.rates?.TJS,
        this.rates?.TMT,
        this.rates?.TND,
        this.rates?.TOP,
        this.rates?.TRY,
        this.rates?.TTD,
        this.rates?.TWD,
        this.rates?.TZS,
        this.rates?.UAH,
        this.rates?.UGX,
        this.rates?.USD,
        this.rates?.UYU,
        this.rates?.UZS,
        this.rates?.VEF,
        this.rates?.VND,
        this.rates?.VUV,
        this.rates?.WST,
        this.rates?.XAF,
        this.rates?.XAG,
        this.rates?.XAU,
        this.rates?.XCD,
        this.rates?.XDR,
        this.rates?.XOF,
        this.rates?.XPF,
        this.rates?.YER,
        this.rates?.ZAR,
        this.rates?.ZMK,
        this.rates?.ZMW,
        this.rates?.ZWL

)


fun OfflineRates.getRates(): Rates? = Rates(
        this.AED,
        this.AFN,
        this.ALL,
        this.AMD,
        this.ANG,
        this.AOA,
        this.ARS,
        this.AUD,
        this.AWG,
        this.AZN,
        this.BAM,
        this.BBD,
        this.BDT,
        this.BGN,
        this.BHD,
        this.BIF,
        this.BMD,
        this.BND,
        this.BOB,
        this.BRL,
        this.BSD,
        this.BTC,
        this.BTN,
        this.BWP,
        this.BYN,
        this.BYR,
        this.BZD,
        this.CAD,
        this.CDF,
        this.CHF,
        this.CLF,
        this.CLP,
        this.CNY,
        this.COP,
        this.CRC,
        this.CUC,
        this.CUP,
        this.CVE,
        this.CZK,
        this.DJF,
        this.DKK,
        this.DOP,
        this.DZD,
        this.EGP,
        this.ERN,
        this.ETB,
        this.EUR,
        this.FJD,
        this.FKP,
        this.GBP,
        this.GEL,
        this.GGP,
        this.GHS,
        this.GIP,
        this.GMD,
        this.GNF,
        this.GTQ,
        this.GYD,
        this.HKD,
        this.HNL,
        this.HRK,
        this.HTG,
        this.HUF,
        this.IDR,
        this.ILS,
        this.IMP,
        this.INR,
        this.IQD,
        this.IRR,
        this.ISK,
        this.JEP,
        this.JMD,
        this.JOD,
        this.JPY,
        this.KES,
        this.KGS,
        this.KHR,
        this.KMF,
        this.KPW,
        this.KRW,
        this.KWD,
        this.KYD,
        this.KZT,
        this.LAK,
        this.LBP,
        this.LKR,
        this.LRD,
        this.LSL,
        this.LTL,
        this.LVL,
        this.LYD,
        this.MAD,
        this.MDL,
        this.MGA,
        this.MKD,
        this.MMK,
        this.MNT,
        this.MOP,
        this.MRO,
        this.MUR,
        this.MVR,
        this.MWK,
        this.MXN,
        this.MYR,
        this.MZN,
        this.NAD,
        this.NGN,
        this.NIO,
        this.NOK,
        this.NPR,
        this.NZD,
        this.OMR,
        this.PAB,
        this.PEN,
        this.PGK,
        this.PHP,
        this.PKR,
        this.PLN,
        this.PYG,
        this.QAR,
        this.RON,
        this.RSD,
        this.RUB,
        this.RWF,
        this.SAR,
        this.SBD,
        this.SCR,
        this.SDG,
        this.SEK,
        this.SGD,
        this.SHP,
        this.SLL,
        this.SOS,
        this.SRD,
        this.STD,
        this.SVC,
        this.SYP,
        this.SZL,
        this.THB,
        this.TJS,
        this.TMT,
        this.TND,
        this.TOP,
        this.TRY,
        this.TTD,
        this.TWD,
        this.TZS,
        this.UAH,
        this.UGX,
        this.USD,
        this.UYU,
        this.UZS,
        this.VEF,
        this.VND,
        this.VUV,
        this.WST,
        this.XAF,
        this.XAG,
        this.XAU,
        this.XCD,
        this.XDR,
        this.XOF,
        this.XPF,
        this.YER,
        this.ZAR,
        this.ZMK,
        this.ZMW,
        this.ZWL
)

fun Rates.findBase(): String {
    return when {
        this.AED == 1.0 -> "AED"
        this.AFN == 1.0 -> "AFN"
        this.ALL == 1.0 -> "ALL"
        this.AMD == 1.0 -> "AMD"
        this.ANG == 1.0 -> "ANG"
        this.AOA == 1.0 -> "AOA"
        this.ARS == 1.0 -> "ARS"
        this.AUD == 1.0 -> "AUD"
        this.AWG == 1.0 -> "AWG"
        this.AZN == 1.0 -> "AZN"
        this.BAM == 1.0 -> "BAM"
        this.BBD == 1.0 -> "BBD"
        this.BDT == 1.0 -> "BDT"
        this.BGN == 1.0 -> "BGN"
        this.BHD == 1.0 -> "BHD"
        this.BIF == 1.0 -> "BIF"
        this.BMD == 1.0 -> "BMD"
        this.BND == 1.0 -> "BND"
        this.BOB == 1.0 -> "BOB"
        this.BRL == 1.0 -> "BRL"
        this.BSD == 1.0 -> "BSD"
        this.BTC == 1.0 -> "BTC"
        this.BTN == 1.0 -> "BTN"
        this.BWP == 1.0 -> "BWP"
        this.BYN == 1.0 -> "BYN"
        this.BYR == 1.0 -> "BYR"
        this.BZD == 1.0 -> "BZD"
        this.CAD == 1.0 -> "CAD"
        this.CDF == 1.0 -> "CDF"
        this.CHF == 1.0 -> "CHF"
        this.CLF == 1.0 -> "CLF"
        this.CLP == 1.0 -> "CLP"
        this.CNY == 1.0 -> "CNY"
        this.COP == 1.0 -> "COP"
        this.CRC == 1.0 -> "CRC"
        this.CUC == 1.0 -> "CUC"
        this.CUP == 1.0 -> "CUP"
        this.CVE == 1.0 -> "CVE"
        this.CZK == 1.0 -> "CZK"
        this.DJF == 1.0 -> "DJF"
        this.DKK == 1.0 -> "DKK"
        this.DOP == 1.0 -> "DOP"
        this.DZD == 1.0 -> "DZD"
        this.EGP == 1.0 -> "EGP"
        this.ERN == 1.0 -> "ERN"
        this.ETB == 1.0 -> "ETB"
        this.EUR == 1.0 -> "EUR"
        this.FJD == 1.0 -> "FJD"
        this.FKP == 1.0 -> "FKP"
        this.GBP == 1.0 -> "GBP"
        this.GEL == 1.0 -> "GEL"
        this.GGP == 1.0 -> "GGP"
        this.GHS == 1.0 -> "GHS"
        this.GIP == 1.0 -> "GIP"
        this.GMD == 1.0 -> "GMD"
        this.GNF == 1.0 -> "GNF"
        this.GTQ == 1.0 -> "GTQ"
        this.GYD == 1.0 -> "GYD"
        this.HKD == 1.0 -> "HKD"
        this.HNL == 1.0 -> "HNL"
        this.HRK == 1.0 -> "HRK"
        this.HTG == 1.0 -> "HTG"
        this.HUF == 1.0 -> "HUF"
        this.IDR == 1.0 -> "IDR"
        this.ILS == 1.0 -> "ILS"
        this.IMP == 1.0 -> "IMP"
        this.INR == 1.0 -> "INR"
        this.IQD == 1.0 -> "IQD"
        this.IRR == 1.0 -> "IRR"
        this.ISK == 1.0 -> "ISK"
        this.JEP == 1.0 -> "JEP"
        this.JMD == 1.0 -> "JMD"
        this.JOD == 1.0 -> "JOD"
        this.JPY == 1.0 -> "JPY"
        this.KES == 1.0 -> "KES"
        this.KGS == 1.0 -> "KGS"
        this.KHR == 1.0 -> "KHR"
        this.KMF == 1.0 -> "KMF"
        this.KPW == 1.0 -> "KPW"
        this.KRW == 1.0 -> "KRW"
        this.KWD == 1.0 -> "KWD"
        this.KYD == 1.0 -> "KYD"
        this.KZT == 1.0 -> "KZT"
        this.LAK == 1.0 -> "LAK"
        this.LBP == 1.0 -> "LBP"
        this.LKR == 1.0 -> "LKR"
        this.LRD == 1.0 -> "LRD"
        this.LSL == 1.0 -> "LSL"
        this.LTL == 1.0 -> "LTL"
        this.LVL == 1.0 -> "LVL"
        this.LYD == 1.0 -> "LYD"
        this.MAD == 1.0 -> "MAD"
        this.MDL == 1.0 -> "MDL"
        this.MGA == 1.0 -> "MGA"
        this.MKD == 1.0 -> "MKD"
        this.MMK == 1.0 -> "MMK"
        this.MNT == 1.0 -> "MNT"
        this.MOP == 1.0 -> "MOP"
        this.MRO == 1.0 -> "MRO"
        this.MUR == 1.0 -> "MUR"
        this.MVR == 1.0 -> "MVR"
        this.MWK == 1.0 -> "MWK"
        this.MXN == 1.0 -> "MXN"
        this.MYR == 1.0 -> "MYR"
        this.MZN == 1.0 -> "MZN"
        this.NAD == 1.0 -> "NAD"
        this.NGN == 1.0 -> "NGN"
        this.NIO == 1.0 -> "NIO"
        this.NOK == 1.0 -> "NOK"
        this.NPR == 1.0 -> "NPR"
        this.NZD == 1.0 -> "NZD"
        this.OMR == 1.0 -> "OMR"
        this.PAB == 1.0 -> "PAB"
        this.PEN == 1.0 -> "PEN"
        this.PGK == 1.0 -> "PGK"
        this.PHP == 1.0 -> "PHP"
        this.PKR == 1.0 -> "PKR"
        this.PLN == 1.0 -> "PLN"
        this.PYG == 1.0 -> "PYG"
        this.QAR == 1.0 -> "QAR"
        this.RON == 1.0 -> "RON"
        this.RSD == 1.0 -> "RSD"
        this.RUB == 1.0 -> "RUB"
        this.RWF == 1.0 -> "RWF"
        this.SAR == 1.0 -> "SAR"
        this.SBD == 1.0 -> "SBD"
        this.SCR == 1.0 -> "SCR"
        this.SDG == 1.0 -> "SDG"
        this.SEK == 1.0 -> "SEK"
        this.SGD == 1.0 -> "SGD"
        this.SHP == 1.0 -> "SHP"
        this.SLL == 1.0 -> "SLL"
        this.SOS == 1.0 -> "SOS"
        this.SRD == 1.0 -> "SRD"
        this.STD == 1.0 -> "STD"
        this.SVC == 1.0 -> "SVC"
        this.SYP == 1.0 -> "SYP"
        this.SZL == 1.0 -> "SZL"
        this.THB == 1.0 -> "THB"
        this.TJS == 1.0 -> "TJS"
        this.TMT == 1.0 -> "TMT"
        this.TND == 1.0 -> "TND"
        this.TOP == 1.0 -> "TOP"
        this.TRY == 1.0 -> "TRY"
        this.TTD == 1.0 -> "TTD"
        this.TWD == 1.0 -> "TWD"
        this.TZS == 1.0 -> "TZS"
        this.UAH == 1.0 -> "UAH"
        this.UGX == 1.0 -> "UGX"
        this.USD == 1.0 -> "USD"
        this.UYU == 1.0 -> "UYU"
        this.UZS == 1.0 -> "UZS"
        this.VEF == 1.0 -> "VEF"
        this.VND == 1.0 -> "VND"
        this.VUV == 1.0 -> "VUV"
        this.WST == 1.0 -> "WST"
        this.XAF == 1.0 -> "XAF"
        this.XAG == 1.0 -> "XAG"
        this.XAU == 1.0 -> "XAU"
        this.XCD == 1.0 -> "XCD"
        this.XDR == 1.0 -> "XDR"
        this.XOF == 1.0 -> "XOF"
        this.XPF == 1.0 -> "XPF"
        this.YER == 1.0 -> "YER"
        this.ZAR == 1.0 -> "ZAR"
        this.ZMK == 1.0 -> "ZMK"
        this.ZMW == 1.0 -> "ZMW"
        this.ZWL == 1.0 -> "ZWL"


        this.AED == null -> "AED"
        this.AFN == null -> "AFN"
        this.ALL == null -> "ALL"
        this.AMD == null -> "AMD"
        this.ANG == null -> "ANG"
        this.AOA == null -> "AOA"
        this.ARS == null -> "ARS"
        this.AUD == null -> "AUD"
        this.AWG == null -> "AWG"
        this.AZN == null -> "AZN"
        this.BAM == null -> "BAM"
        this.BBD == null -> "BBD"
        this.BDT == null -> "BDT"
        this.BGN == null -> "BGN"
        this.BHD == null -> "BHD"
        this.BIF == null -> "BIF"
        this.BMD == null -> "BMD"
        this.BND == null -> "BND"
        this.BOB == null -> "BOB"
        this.BRL == null -> "BRL"
        this.BSD == null -> "BSD"
        this.BTC == null -> "BTC"
        this.BTN == null -> "BTN"
        this.BWP == null -> "BWP"
        this.BYN == null -> "BYN"
        this.BYR == null -> "BYR"
        this.BZD == null -> "BZD"
        this.CAD == null -> "CAD"
        this.CDF == null -> "CDF"
        this.CHF == null -> "CHF"
        this.CLF == null -> "CLF"
        this.CLP == null -> "CLP"
        this.CNY == null -> "CNY"
        this.COP == null -> "COP"
        this.CRC == null -> "CRC"
        this.CUC == null -> "CUC"
        this.CUP == null -> "CUP"
        this.CVE == null -> "CVE"
        this.CZK == null -> "CZK"
        this.DJF == null -> "DJF"
        this.DKK == null -> "DKK"
        this.DOP == null -> "DOP"
        this.DZD == null -> "DZD"
        this.EGP == null -> "EGP"
        this.ERN == null -> "ERN"
        this.ETB == null -> "ETB"
        this.EUR == null -> "EUR"
        this.FJD == null -> "FJD"
        this.FKP == null -> "FKP"
        this.GBP == null -> "GBP"
        this.GEL == null -> "GEL"
        this.GGP == null -> "GGP"
        this.GHS == null -> "GHS"
        this.GIP == null -> "GIP"
        this.GMD == null -> "GMD"
        this.GNF == null -> "GNF"
        this.GTQ == null -> "GTQ"
        this.GYD == null -> "GYD"
        this.HKD == null -> "HKD"
        this.HNL == null -> "HNL"
        this.HRK == null -> "HRK"
        this.HTG == null -> "HTG"
        this.HUF == null -> "HUF"
        this.IDR == null -> "IDR"
        this.ILS == null -> "ILS"
        this.IMP == null -> "IMP"
        this.INR == null -> "INR"
        this.IQD == null -> "IQD"
        this.IRR == null -> "IRR"
        this.ISK == null -> "ISK"
        this.JEP == null -> "JEP"
        this.JMD == null -> "JMD"
        this.JOD == null -> "JOD"
        this.JPY == null -> "JPY"
        this.KES == null -> "KES"
        this.KGS == null -> "KGS"
        this.KHR == null -> "KHR"
        this.KMF == null -> "KMF"
        this.KPW == null -> "KPW"
        this.KRW == null -> "KRW"
        this.KWD == null -> "KWD"
        this.KYD == null -> "KYD"
        this.KZT == null -> "KZT"
        this.LAK == null -> "LAK"
        this.LBP == null -> "LBP"
        this.LKR == null -> "LKR"
        this.LRD == null -> "LRD"
        this.LSL == null -> "LSL"
        this.LTL == null -> "LTL"
        this.LVL == null -> "LVL"
        this.LYD == null -> "LYD"
        this.MAD == null -> "MAD"
        this.MDL == null -> "MDL"
        this.MGA == null -> "MGA"
        this.MKD == null -> "MKD"
        this.MMK == null -> "MMK"
        this.MNT == null -> "MNT"
        this.MOP == null -> "MOP"
        this.MRO == null -> "MRO"
        this.MUR == null -> "MUR"
        this.MVR == null -> "MVR"
        this.MWK == null -> "MWK"
        this.MXN == null -> "MXN"
        this.MYR == null -> "MYR"
        this.MZN == null -> "MZN"
        this.NAD == null -> "NAD"
        this.NGN == null -> "NGN"
        this.NIO == null -> "NIO"
        this.NOK == null -> "NOK"
        this.NPR == null -> "NPR"
        this.NZD == null -> "NZD"
        this.OMR == null -> "OMR"
        this.PAB == null -> "PAB"
        this.PEN == null -> "PEN"
        this.PGK == null -> "PGK"
        this.PHP == null -> "PHP"
        this.PKR == null -> "PKR"
        this.PLN == null -> "PLN"
        this.PYG == null -> "PYG"
        this.QAR == null -> "QAR"
        this.RON == null -> "RON"
        this.RSD == null -> "RSD"
        this.RUB == null -> "RUB"
        this.RWF == null -> "RWF"
        this.SAR == null -> "SAR"
        this.SBD == null -> "SBD"
        this.SCR == null -> "SCR"
        this.SDG == null -> "SDG"
        this.SEK == null -> "SEK"
        this.SGD == null -> "SGD"
        this.SHP == null -> "SHP"
        this.SLL == null -> "SLL"
        this.SOS == null -> "SOS"
        this.SRD == null -> "SRD"
        this.STD == null -> "STD"
        this.SVC == null -> "SVC"
        this.SYP == null -> "SYP"
        this.SZL == null -> "SZL"
        this.THB == null -> "THB"
        this.TJS == null -> "TJS"
        this.TMT == null -> "TMT"
        this.TND == null -> "TND"
        this.TOP == null -> "TOP"
        this.TRY == null -> "TRY"
        this.TTD == null -> "TTD"
        this.TWD == null -> "TWD"
        this.TZS == null -> "TZS"
        this.UAH == null -> "UAH"
        this.UGX == null -> "UGX"
        this.USD == null -> "USD"
        this.UYU == null -> "UYU"
        this.UZS == null -> "UZS"
        this.VEF == null -> "VEF"
        this.VND == null -> "VND"
        this.VUV == null -> "VUV"
        this.WST == null -> "WST"
        this.XAF == null -> "XAF"
        this.XAG == null -> "XAG"
        this.XAU == null -> "XAU"
        this.XCD == null -> "XCD"
        this.XDR == null -> "XDR"
        this.XOF == null -> "XOF"
        this.XPF == null -> "XPF"
        this.YER == null -> "YER"
        this.ZAR == null -> "ZAR"
        this.ZMK == null -> "ZMK"
        this.ZMW == null -> "ZMW"
        this.ZWL == null -> "ZWL"

        else -> "NULL"
    }
}































