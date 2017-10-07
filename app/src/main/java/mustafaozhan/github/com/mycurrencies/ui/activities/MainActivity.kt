package mustafaozhan.github.com.mycurrencies.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import android.view.Menu
import android.view.MenuItem
import mustafaozhan.github.com.mycurrencies.R
import kotlinx.android.synthetic.main.activity_main.*
import mustafaozhan.github.com.mycurrencies.model.Currency
import mustafaozhan.github.com.mycurrencies.model.ResponseAll
import mustafaozhan.github.com.mycurrencies.rest.ApiClient
import mustafaozhan.github.com.mycurrencies.rest.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    val currencyList: ArrayList<Currency>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        set()
    }

    private fun set() {

        val apiService = ApiClient.get().create(ApiInterface::class.java)
        val myCall = apiService.getByBase(mSpinner.text.toString())



        Observable.create(Observable.OnSubscribe<String> { subscriber ->
            eTxt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit

                override fun beforeTextChanged(s: CharSequence?,
                                               start: Int, count: Int,
                                               after: Int) = Unit

                override fun onTextChanged(s: CharSequence,
                                           start: Int, before: Int,
                                           count: Int)
                        = subscriber.onNext(s.toString())
            })
        }).debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ text ->

                    myCall.enqueue(object : Callback<ResponseAll> {
                        override fun onResponse(call: Call<ResponseAll>?, response: Response<ResponseAll>?) {

                            currencyList?.add(Currency("EUR", response?.body()?.rates!!.eUR!!*text.toDouble()))
                            currencyList?.add(Currency("AUD", response?.body()?.rates!!.aUD!!*text.toDouble()))
                            currencyList?.add(Currency("BGN", response?.body()?.rates!!.bGN!!*text.toDouble()))
                            currencyList?.add(Currency("BRL", response?.body()?.rates!!.bRL!!*text.toDouble()))
                            currencyList?.add(Currency("CAD", response?.body()?.rates!!.cAD!!*text.toDouble()))
                            currencyList?.add(Currency("CHF", response?.body()?.rates!!.cHF!!*text.toDouble()))
                            currencyList?.add(Currency("CNY", response?.body()?.rates!!.cNY!!*text.toDouble()))
                            currencyList?.add(Currency("CZK", response?.body()?.rates!!.cZK!!*text.toDouble()))
                            currencyList?.add(Currency("DKK", response?.body()?.rates!!.dKK!!*text.toDouble()))
                            currencyList?.add(Currency("GBP", response?.body()?.rates!!.gBP!!*text.toDouble()))
                            currencyList?.add(Currency("HKD", response?.body()?.rates!!.hKD!!*text.toDouble()))
                            currencyList?.add(Currency("HRK", response?.body()?.rates!!.hRK!!*text.toDouble()))
                            currencyList?.add(Currency("HUF", response?.body()?.rates!!.hUF!!*text.toDouble()))
                            currencyList?.add(Currency("IDR", response?.body()?.rates!!.iDR!!*text.toDouble()))
                            currencyList?.add(Currency("ILS", response?.body()?.rates!!.iLS!!*text.toDouble()))
                            currencyList?.add(Currency("INR", response?.body()?.rates!!.iNR!!*text.toDouble()))
                            currencyList?.add(Currency("JPY", response?.body()?.rates!!.jPY!!*text.toDouble()))
                            currencyList?.add(Currency("KRW", response?.body()?.rates!!.kRW!!*text.toDouble()))
                            currencyList?.add(Currency("MXN", response?.body()?.rates!!.mXN!!*text.toDouble()))
                            currencyList?.add(Currency("MYR", response?.body()?.rates!!.mYR!!*text.toDouble()))
                            currencyList?.add(Currency("NOK", response?.body()?.rates!!.nOK!!*text.toDouble()))
                            currencyList?.add(Currency("NZD", response?.body()?.rates!!.nZD!!*text.toDouble()))
                            currencyList?.add(Currency("PHP", response?.body()?.rates!!.pHP!!*text.toDouble()))
                            currencyList?.add(Currency("PLN", response?.body()?.rates!!.pLN!!*text.toDouble()))
                            currencyList?.add(Currency("RON", response?.body()?.rates!!.rON!!*text.toDouble()))
                            currencyList?.add(Currency("RUB", response?.body()?.rates!!.rUB!!*text.toDouble()))
                            currencyList?.add(Currency("SEK", response?.body()?.rates!!.sEK!!*text.toDouble()))
                            currencyList?.add(Currency("SGD", response?.body()?.rates!!.sGD!!*text.toDouble()))
                            currencyList?.add(Currency("THB", response?.body()?.rates!!.tHB!!*text.toDouble()))
                            currencyList?.add(Currency("TRY", response?.body()?.rates!!.tRY!!*text.toDouble()))
                            currencyList?.add(Currency("USD", response?.body()?.rates!!.uSD!!*text.toDouble()))
                            currencyList?.add(Currency("ZAR", response?.body()?.rates!!.zAR!!*text.toDouble()))


                        }

                        override fun onFailure(call: Call<ResponseAll>?, t: Throwable?) {
                        }
                    })


                })


    }

    private fun init() {
        mSpinner.setItems("EUR", "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK",
                "GBP", "HKD", "HRK", "HUF", "IDR", "ILS", "INR", "JPY", "KRW", "MXN",
                "MYR", "NOK", "NZD", "PHP", "PLN", "RON", "RUB", "SEK", "SGD", "THB",
                "TRY", "USD", "ZAR")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {


        }

        return true
    }
}