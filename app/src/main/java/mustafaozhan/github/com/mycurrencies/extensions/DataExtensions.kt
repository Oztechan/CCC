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
    Currencies.EUR.toString() -> rate?.eUR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.AUD.toString() -> rate?.aUD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BGN.toString() -> rate?.bGN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BRL.toString() -> rate?.bRL?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CAD.toString() -> rate?.cAD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CHF.toString() -> rate?.cHF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CNY.toString() -> rate?.cNY?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CZK.toString() -> rate?.cZK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.DKK.toString() -> rate?.dKK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.GBP.toString() -> rate?.gBP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.HKD.toString() -> rate?.hKD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.HRK.toString() -> rate?.hRK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.HUF.toString() -> rate?.hUF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.IDR.toString() -> rate?.iDR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ILS.toString() -> rate?.iLS?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.INR.toString() -> rate?.iNR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.JPY.toString() -> rate?.jPY?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.KRW.toString() -> rate?.kRW?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MXN.toString() -> rate?.mXN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MYR.toString() -> rate?.mYR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.NOK.toString() -> rate?.nOK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.NZD.toString() -> rate?.nZD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.PHP.toString() -> rate?.pHP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.PLN.toString() -> rate?.pLN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.RON.toString() -> rate?.rON?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.RUB.toString() -> rate?.rUB?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SEK.toString() -> rate?.sEK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SGD.toString() -> rate?.sGD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.THB.toString() -> rate?.tHB?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.TRY.toString() -> rate?.tRY?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.USD.toString() -> rate?.uSD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ZAR.toString() -> rate?.zAR?.times(temp.toDouble()) ?: temp.toDouble()
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
    }
}


fun CurrencyResponse.toOfflineRates() = OfflineRates(this.rates?.findBase().toString(),
        this.rates?.eUR,
        this.rates?.aUD,
        this.rates?.bGN,
        this.rates?.bRL,
        this.rates?.cAD,
        this.rates?.cHF,
        this.rates?.cNY,
        this.rates?.cZK,
        this.rates?.dKK,
        this.rates?.gBP,
        this.rates?.hKD,
        this.rates?.hRK,
        this.rates?.hUF,
        this.rates?.iDR,
        this.rates?.iLS,
        this.rates?.iNR,
        this.rates?.jPY,
        this.rates?.kRW,
        this.rates?.mXN,
        this.rates?.mYR,
        this.rates?.nOK,
        this.rates?.nZD,
        this.rates?.pHP,
        this.rates?.pLN,
        this.rates?.rON,
        this.rates?.rUB,
        this.rates?.sEK,
        this.rates?.sGD,
        this.rates?.tHB,
        this.rates?.tRY,
        this.rates?.uSD,
        this.rates?.zAR
)


fun OfflineRates.getRates(): Rates? = Rates(
        this.eUR,
        this.aUD,
        this.bGN,
        this.bRL,
        this.cAD,
        this.cHF,
        this.cNY,
        this.cZK,
        this.dKK,
        this.gBP,
        this.hKD,
        this.hRK,
        this.hUF,
        this.iDR,
        this.iLS,
        this.iNR,
        this.jPY,
        this.kRW,
        this.mXN,
        this.mYR,
        this.nOK,
        this.nZD,
        this.pHP,
        this.pLN,
        this.rON,
        this.rUB,
        this.sEK,
        this.sGD,
        this.tHB,
        this.tRY,
        this.uSD,
        this.zAR
)

fun Rates.findBase(): String {
    return when {
        this.eUR == 1.0 -> "EUR"
        this.eUR == 1.0 -> "EUR"
        this.aUD == 1.0 -> "AUD"
        this.bGN == 1.0 -> "BGN"
        this.bRL == 1.0 -> "BRL"
        this.cAD == 1.0 -> "CAD"
        this.cHF == 1.0 -> "CHF"
        this.cNY == 1.0 -> "CNY"
        this.cZK == 1.0 -> "CZK"
        this.dKK == 1.0 -> "DKK"
        this.gBP == 1.0 -> "GBP"
        this.hKD == 1.0 -> "HKD"
        this.hRK == 1.0 -> "HRK"
        this.hUF == 1.0 -> "HUF"
        this.iDR == 1.0 -> "IDR"
        this.iLS == 1.0 -> "ILS"
        this.iNR == 1.0 -> "INR"
        this.jPY == 1.0 -> "JPY"
        this.kRW == 1.0 -> "KRW"
        this.mXN == 1.0 -> "MXN"
        this.mYR == 1.0 -> "MYR"
        this.nOK == 1.0 -> "NOK"
        this.nZD == 1.0 -> "NZD"
        this.pHP == 1.0 -> "PHP"
        this.pLN == 1.0 -> "PLN"
        this.rON == 1.0 -> "RON"
        this.rUB == 1.0 -> "RUB"
        this.sEK == 1.0 -> "SEK"
        this.sGD == 1.0 -> "SGD"
        this.tHB == 1.0 -> "THB"
        this.tRY == 1.0 -> "TRY"
        this.uSD == 1.0 -> "USD"
        this.zAR == 1.0 -> "ZAR"

        this.eUR == null -> "EUR"
        this.eUR == null -> "EUR"
        this.aUD == null -> "AUD"
        this.bGN == null -> "BGN"
        this.bRL == null -> "BRL"
        this.cAD == null -> "CAD"
        this.cHF == null -> "CHF"
        this.cNY == null -> "CNY"
        this.cZK == null -> "CZK"
        this.dKK == null -> "DKK"
        this.gBP == null -> "GBP"
        this.hKD == null -> "HKD"
        this.hRK == null -> "HRK"
        this.hUF == null -> "HUF"
        this.iDR == null -> "IDR"
        this.iLS == null -> "ILS"
        this.iNR == null -> "INR"
        this.jPY == null -> "JPY"
        this.kRW == null -> "KRW"
        this.mXN == null -> "MXN"
        this.mYR == null -> "MYR"
        this.nOK == null -> "NOK"
        this.nZD == null -> "NZD"
        this.pHP == null -> "PHP"
        this.pLN == null -> "PLN"
        this.rON == null -> "RON"
        this.rUB == null -> "RUB"
        this.sEK == null -> "SEK"
        this.sGD == null -> "SGD"
        this.tHB == null -> "THB"
        this.tRY == null -> "TRY"
        this.uSD == null -> "USD"
        this.zAR == null -> "ZAR"

        else -> "NULL"
    }
}































