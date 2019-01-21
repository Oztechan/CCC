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

fun getResult(name: String, value: String, rate: Rates?): Double {
    try {
        when (name) {
            Currencies.AED.toString() -> return rate?.AED?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.AFN.toString() -> return rate?.AFN?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.ALL.toString() -> return rate?.ALL?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.AMD.toString() -> return rate?.AMD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.ANG.toString() -> return rate?.ANG?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.AOA.toString() -> return rate?.AOA?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.ARS.toString() -> return rate?.ARS?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.AUD.toString() -> return rate?.AUD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.AWG.toString() -> return rate?.AWG?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.AZN.toString() -> return rate?.AZN?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BAM.toString() -> return rate?.BAM?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BBD.toString() -> return rate?.BBD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BDT.toString() -> return rate?.BDT?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BGN.toString() -> return rate?.BGN?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BHD.toString() -> return rate?.BHD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BIF.toString() -> return rate?.BIF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BMD.toString() -> return rate?.BMD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BND.toString() -> return rate?.BND?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BOB.toString() -> return rate?.BOB?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BRL.toString() -> return rate?.BRL?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BSD.toString() -> return rate?.BSD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BTC.toString() -> return rate?.BTC?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BTN.toString() -> return rate?.BTN?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BWP.toString() -> return rate?.BWP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BYN.toString() -> return rate?.BYN?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BYR.toString() -> return rate?.BYR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.BZD.toString() -> return rate?.BZD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.CAD.toString() -> return rate?.CAD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.CDF.toString() -> return rate?.CDF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.CHF.toString() -> return rate?.CHF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.CLF.toString() -> return rate?.CLF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.CLP.toString() -> return rate?.CLP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.CNY.toString() -> return rate?.CNY?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.COP.toString() -> return rate?.COP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.CRC.toString() -> return rate?.CRC?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.CUC.toString() -> return rate?.CUC?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.CUP.toString() -> return rate?.CUP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.CVE.toString() -> return rate?.CVE?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.CZK.toString() -> return rate?.CZK?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.DJF.toString() -> return rate?.DJF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.DKK.toString() -> return rate?.DKK?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.DOP.toString() -> return rate?.DOP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.DZD.toString() -> return rate?.DZD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.EGP.toString() -> return rate?.EGP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.ERN.toString() -> return rate?.ERN?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.ETB.toString() -> return rate?.ETB?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.EUR.toString() -> return rate?.EUR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.FJD.toString() -> return rate?.FJD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.FKP.toString() -> return rate?.FKP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.GBP.toString() -> return rate?.GBP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.GEL.toString() -> return rate?.GEL?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.GGP.toString() -> return rate?.GGP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.GHS.toString() -> return rate?.GHS?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.GIP.toString() -> return rate?.GIP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.GMD.toString() -> return rate?.GMD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.GNF.toString() -> return rate?.GNF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.GTQ.toString() -> return rate?.GTQ?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.GYD.toString() -> return rate?.GYD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.HKD.toString() -> return rate?.HKD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.HNL.toString() -> return rate?.HNL?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.HRK.toString() -> return rate?.HRK?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.HTG.toString() -> return rate?.HTG?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.HUF.toString() -> return rate?.HUF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.IDR.toString() -> return rate?.IDR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.ILS.toString() -> return rate?.ILS?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.IMP.toString() -> return rate?.IMP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.INR.toString() -> return rate?.INR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.IQD.toString() -> return rate?.IQD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.IRR.toString() -> return rate?.IRR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.ISK.toString() -> return rate?.ISK?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.JEP.toString() -> return rate?.JEP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.JMD.toString() -> return rate?.JMD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.JOD.toString() -> return rate?.JOD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.JPY.toString() -> return rate?.JPY?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.KES.toString() -> return rate?.KES?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.KGS.toString() -> return rate?.KGS?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.KHR.toString() -> return rate?.KHR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.KMF.toString() -> return rate?.KMF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.KPW.toString() -> return rate?.KPW?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.KRW.toString() -> return rate?.KRW?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.KWD.toString() -> return rate?.KWD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.KYD.toString() -> return rate?.KYD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.KZT.toString() -> return rate?.KZT?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.LAK.toString() -> return rate?.LAK?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.LBP.toString() -> return rate?.LBP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.LKR.toString() -> return rate?.LKR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.LRD.toString() -> return rate?.LRD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.LSL.toString() -> return rate?.LSL?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.LTL.toString() -> return rate?.LTL?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.LVL.toString() -> return rate?.LVL?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.LYD.toString() -> return rate?.LYD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MAD.toString() -> return rate?.MAD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MDL.toString() -> return rate?.MDL?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MGA.toString() -> return rate?.MGA?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MKD.toString() -> return rate?.MKD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MMK.toString() -> return rate?.MMK?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MNT.toString() -> return rate?.MNT?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MOP.toString() -> return rate?.MOP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MRO.toString() -> return rate?.MRO?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MUR.toString() -> return rate?.MUR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MVR.toString() -> return rate?.MVR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MWK.toString() -> return rate?.MWK?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MXN.toString() -> return rate?.MXN?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MYR.toString() -> return rate?.MYR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.MZN.toString() -> return rate?.MZN?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.NAD.toString() -> return rate?.NAD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.NGN.toString() -> return rate?.NGN?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.NIO.toString() -> return rate?.NIO?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.NOK.toString() -> return rate?.NOK?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.NPR.toString() -> return rate?.NPR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.NZD.toString() -> return rate?.NZD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.OMR.toString() -> return rate?.OMR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.PAB.toString() -> return rate?.PAB?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.PEN.toString() -> return rate?.PEN?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.PGK.toString() -> return rate?.PGK?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.PHP.toString() -> return rate?.PHP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.PKR.toString() -> return rate?.PKR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.PLN.toString() -> return rate?.PLN?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.PYG.toString() -> return rate?.PYG?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.QAR.toString() -> return rate?.QAR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.RON.toString() -> return rate?.RON?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.RSD.toString() -> return rate?.RSD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.RUB.toString() -> return rate?.RUB?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.RWF.toString() -> return rate?.RWF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SAR.toString() -> return rate?.SAR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SBD.toString() -> return rate?.SBD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SCR.toString() -> return rate?.SCR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SDG.toString() -> return rate?.SDG?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SEK.toString() -> return rate?.SEK?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SGD.toString() -> return rate?.SGD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SHP.toString() -> return rate?.SHP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SLL.toString() -> return rate?.SLL?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SOS.toString() -> return rate?.SOS?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SRD.toString() -> return rate?.SRD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.STD.toString() -> return rate?.STD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SVC.toString() -> return rate?.SVC?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SYP.toString() -> return rate?.SYP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.SZL.toString() -> return rate?.SZL?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.THB.toString() -> return rate?.THB?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.TJS.toString() -> return rate?.TJS?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.TMT.toString() -> return rate?.TMT?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.TND.toString() -> return rate?.TND?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.TOP.toString() -> return rate?.TOP?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.TRY.toString() -> return rate?.TRY?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.TTD.toString() -> return rate?.TTD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.TWD.toString() -> return rate?.TWD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.TZS.toString() -> return rate?.TZS?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.UAH.toString() -> return rate?.UAH?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.UGX.toString() -> return rate?.UGX?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.USD.toString() -> return rate?.USD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.UYU.toString() -> return rate?.UYU?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.UZS.toString() -> return rate?.UZS?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.VEF.toString() -> return rate?.VEF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.VND.toString() -> return rate?.VND?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.VUV.toString() -> return rate?.VUV?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.WST.toString() -> return rate?.WST?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.XAF.toString() -> return rate?.XAF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.XAG.toString() -> return rate?.XAG?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.XAU.toString() -> return rate?.XAU?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.XCD.toString() -> return rate?.XCD?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.XDR.toString() -> return rate?.XDR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.XOF.toString() -> return rate?.XOF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.XPF.toString() -> return rate?.XPF?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.YER.toString() -> return rate?.YER?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.ZAR.toString() -> return rate?.ZAR?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.ZMK.toString() -> return rate?.ZMK?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.ZMW.toString() -> return rate?.ZMW?.times(value.toDouble())
                    ?: value.toDouble()
            Currencies.ZWL.toString() -> return rate?.ZWL?.times(value.toDouble())
                    ?: value.toDouble()
            else -> return 0.0
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Crashlytics.logException(e)
        return 0.0
    }
}

fun CurrencyDao.insertInitialCurrencies() {
    this.apply {
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































