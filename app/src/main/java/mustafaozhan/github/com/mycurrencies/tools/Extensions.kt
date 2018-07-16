package mustafaozhan.github.com.mycurrencies.tools

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import mustafaozhan.github.com.mycurrencies.main.fragment.model.Rates

/**
 * Created by Mustafa Ozhan on 2018-07-16.
 */
fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
    removeObserver(observer)
    observe(owner, observer)
}

fun getResult(name: Currencies, temp: String, rate: Rates) = when (name) {
    Currencies.EUR -> rate.eUR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.AUD -> rate.aUD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BGN -> rate.bGN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.BRL -> rate.bRL?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CAD -> rate.cAD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CHF -> rate.cHF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CNY -> rate.cNY?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.CZK -> rate.cZK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.DKK -> rate.dKK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.GBP -> rate.gBP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.HKD -> rate.hKD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.HRK -> rate.hRK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.HUF -> rate.hUF?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.IDR -> rate.iDR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ILS -> rate.iLS?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.INR -> rate.iNR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.JPY -> rate.jPY?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.KRW -> rate.kRW?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MXN -> rate.mXN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.MYR -> rate.mYR?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.NOK -> rate.nOK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.NZD -> rate.nZD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.PHP -> rate.pHP?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.PLN -> rate.pLN?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.RON -> rate.rON?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.RUB -> rate.rUB?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SEK -> rate.sEK?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.SGD -> rate.sGD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.THB -> rate.tHB?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.TRY -> rate.tRY?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.USD -> rate.uSD?.times(temp.toDouble()) ?: temp.toDouble()
    Currencies.ZAR -> rate.zAR?.times(temp.toDouble()) ?: temp.toDouble()
}
