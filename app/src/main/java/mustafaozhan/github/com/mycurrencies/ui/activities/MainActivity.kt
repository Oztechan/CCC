package mustafaozhan.github.com.mycurrencies.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import android.view.Menu
import android.view.MenuItem
import mustafaozhan.github.com.mycurrencies.R
import kotlinx.android.synthetic.main.activity_main.*
import mustafaozhan.github.com.mycurrencies.model.ResponseAll
import mustafaozhan.github.com.mycurrencies.rest.ApiClient
import mustafaozhan.github.com.mycurrencies.rest.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        set()
    }

    private fun set() {

        val apiService = ApiClient.get().create(ApiInterface::class.java)




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

                    val myCall = apiService.getByBase(mSpinner.text.toString())
                    myCall.enqueue(object : Callback<ResponseAll> {
                        override fun onResponse(call: Call<ResponseAll>?, response: Response<ResponseAll>?) {
                            var value = 0.0
                            if (eTxt.text.toString() != "")
                                value = java.lang.Double.parseDouble(eTxt.text.toString())
                            Log.e("${eTxt.text} ${mSpinner.text} ", "${(response!!.body()!!.rates!!.tRY!! * value)} Turkish Lira")
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