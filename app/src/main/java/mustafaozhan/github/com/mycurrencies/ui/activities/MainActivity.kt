package mustafaozhan.github.com.mycurrencies.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.model.ResponseAll
import mustafaozhan.github.com.mycurrencies.rest.ApiClient
import mustafaozhan.github.com.mycurrencies.rest.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import mustafaozhan.github.com.mycurrencies.ui.adapters.MyCurrencyAdapter


class MainActivity : AppCompatActivity() {


//        val myCall = apiService.getByBase(mSpinner.selectedItem.toString())
//        myCall.enqueue(object : Callback<ResponseAll> {
//            override fun onResponse(call: Call<ResponseAll>?, response: Response<ResponseAll>?) {
//                var value = 0.0
//                if (eTxt.text.toString() != "")
//                    value = java.lang.Double.parseDouble(eTxt.text.toString())
//                Log.w("${eTxt.text} ${mSpinner.selectedItem} ", "${(response!!.body()!!.rates!!.tRY!! * value)} Turkish Lira")
//            }
//
//            override fun onFailure(call: Call<ResponseAll>?, t: Throwable?) {
//
//            }
//
//        })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

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