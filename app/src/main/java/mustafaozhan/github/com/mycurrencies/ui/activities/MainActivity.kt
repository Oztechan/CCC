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
import mustafaozhan.github.com.mycurrencies.ui.adapters.MyCurrencyAdapter
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.plugins.RxJavaPlugins.onError


class MainActivity : AppCompatActivity() {
    val currencyList = ArrayList<Currency>()
    val mAdapter = MyCurrencyAdapter(currencyList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecViewCurrency.layoutManager = mLayoutManager
//        mRecViewCurrency.itemAnimator = DefaultItemAnimator()
        mRecViewCurrency.adapter = mAdapter



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

                    myCall.clone().enqueue(object : Callback<ResponseAll> {
                        override fun onResponse(call: Call<ResponseAll>?, response: Response<ResponseAll>?) {

                            currencyList.clear()
                            val temp = if (text.isEmpty())
                                1.toString()
                            else
                                text
                            currencyList.add(Currency("EUR", response?.body()?.rates!!.eUR ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("AUD", response.body()?.rates!!.aUD ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("BGN", response.body()?.rates!!.bGN ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("BRL", response.body()?.rates!!.bRL ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("CAD", response.body()?.rates!!.cAD ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("CHF", response.body()?.rates!!.cHF ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("CNY", response.body()?.rates!!.cNY ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("CZK", response.body()?.rates!!.cZK ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("DKK", response.body()?.rates!!.dKK ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("GBP", response.body()?.rates!!.gBP ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("HKD", response.body()?.rates!!.hKD ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("HRK", response.body()?.rates!!.hRK ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("HUF", response.body()?.rates!!.hUF ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("IDR", response.body()?.rates!!.iDR ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("ILS", response.body()?.rates!!.iLS ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("INR", response.body()?.rates!!.iNR ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("JPY", response.body()?.rates!!.jPY ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("KRW", response.body()?.rates!!.kRW ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("MXN", response.body()?.rates!!.mXN ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("MYR", response.body()?.rates!!.mYR ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("NOK", response.body()?.rates!!.nOK ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("NZD", response.body()?.rates!!.nZD ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("PHP", response.body()?.rates!!.pHP ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("PLN", response.body()?.rates!!.pLN ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("RON", response.body()?.rates!!.rON ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("RUB", response.body()?.rates!!.rUB ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("SEK", response.body()?.rates!!.sEK ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("SGD", response.body()?.rates!!.sGD ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("THB", response.body()?.rates!!.tHB ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("TRY", response.body()?.rates!!.tRY ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("USD", response.body()?.rates!!.uSD ?: 1 * temp.toDouble()))
                            currencyList.add(Currency("ZAR", response.body()?.rates!!.zAR ?: 1 * temp.toDouble()))

                            mAdapter.notifyDataSetChanged()

                        }

                        override fun onFailure(call: Call<ResponseAll>?, t: Throwable?) {
                        }
                    })


                }, { e -> onError(e) })


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