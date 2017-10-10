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
import mustafaozhan.github.com.mycurrencies.model.data.Currency
import mustafaozhan.github.com.mycurrencies.model.web.ResponseAll
import mustafaozhan.github.com.mycurrencies.rest.ApiClient
import mustafaozhan.github.com.mycurrencies.rest.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit
import mustafaozhan.github.com.mycurrencies.ui.adapters.MyCurrencyAdapter
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import io.reactivex.plugins.RxJavaPlugins.onError


class MainActivity : AppCompatActivity() {
    val currencyList = ArrayList<Currency>()
    val mAdapter = MyCurrencyAdapter(currencyList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecViewCurrency.layoutManager = mLayoutManager
        mRecViewCurrency.itemAnimator = DefaultItemAnimator()
        mRecViewCurrency.adapter = mAdapter



        init()
        set()
    }

    private fun set() {
        mSpinner.setOnItemSelectedListener { _, _, _, _ ->
            val temp = eTxt.text
            eTxt.text = null
            eTxt.text = temp
        }


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

                    loading.visibility = View.VISIBLE
                    loading.bringToFront()
                    val apiService = ApiClient.get().create(ApiInterface::class.java)
                    val myCall = apiService.getByBase(mSpinner.text.toString())
                    currencyList.clear()
                    myCall.clone().enqueue(object : Callback<ResponseAll> {
                        override fun onResponse(call: Call<ResponseAll>?, response: Response<ResponseAll>?) {

                            val tempCurrency = response!!.body()!!.rates!!

                            val temp = if (text.isEmpty())
                                0.toString()
                            else
                                text

                            currencyList.add(Currency("EUR", tempCurrency.eUR?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("AUD", tempCurrency.aUD?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("BGN", tempCurrency.bGN?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("BRL", tempCurrency.bRL?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("CAD", tempCurrency.cAD?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("CHF", tempCurrency.cHF?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("CNY", tempCurrency.cNY?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("CZK", tempCurrency.cZK?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("DKK", tempCurrency.dKK?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("GBP", tempCurrency.gBP?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("HKD", tempCurrency.hKD?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("HRK", tempCurrency.hRK?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("HUF", tempCurrency.hUF?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("IDR", tempCurrency.iDR?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("ILS", tempCurrency.iLS?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("INR", tempCurrency.iNR?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("JPY", tempCurrency.jPY?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("KRW", tempCurrency.kRW?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("MXN", tempCurrency.mXN?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("MYR", tempCurrency.mYR?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("NOK", tempCurrency.nOK?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("NZD", tempCurrency.nZD?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("PHP", tempCurrency.pHP?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("PLN", tempCurrency.pLN?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("RON", tempCurrency.rON?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("RUB", tempCurrency.rUB?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("SEK", tempCurrency.sEK?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("SGD", tempCurrency.sGD?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("THB", tempCurrency.tHB?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("TRY", tempCurrency.tRY?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("USD", tempCurrency.uSD?.times(temp.toDouble()) ?: temp.toDouble()))
                            currencyList.add(Currency("ZAR", tempCurrency.zAR?.times(temp.toDouble()) ?: temp.toDouble()))
                            loading.visibility = View.INVISIBLE
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
            R.id.settings -> Toast.makeText(this, "Under process", Toast.LENGTH_SHORT).show()
        }

        return true
    }
}