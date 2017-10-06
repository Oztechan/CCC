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


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {


    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val apiService = ApiClient.get().create(ApiInterface::class.java)

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()



      //  mSpinner.onItemSelectedListener = this
        val baseList = ArrayList<String>()
        baseList.add("EUR")
        baseList.add("AUD")
        baseList.add("BGN")
        baseList.add("BRL")
        baseList.add("CAD")
        baseList.add("CHF")
        baseList.add("CNY")
        baseList.add("CZK")
        baseList.add("DKK")
        baseList.add("GBP")
        baseList.add("HKD")
        baseList.add("HRK")
        baseList.add("HUF")
        baseList.add("IDR")
        baseList.add("ILS")
        baseList.add("INR")
        baseList.add("JPY")
        baseList.add("KRW")
        baseList.add("MXN")
        baseList.add("MYR")
        baseList.add("NOK")
        baseList.add("NZD")
        baseList.add("PHP")
        baseList.add("PLN")
        baseList.add("RON")
        baseList.add("RUB")
        baseList.add("SEK")
        baseList.add("SGD")
        baseList.add("THB")
        baseList.add("TRY")
        baseList.add("USD")
        baseList.add("ZAR")
        val dataAdapterDepartment = ArrayAdapter(this, android.R.layout.simple_spinner_item, baseList)
        dataAdapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpinner.setAdapter(dataAdapterDepartment)

        mRecViewCurrency.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager?
        val adapter = MyCurrencyAdapter(baseList)
        mRecViewCurrency.adapter = adapter


    }

    private fun init() {

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