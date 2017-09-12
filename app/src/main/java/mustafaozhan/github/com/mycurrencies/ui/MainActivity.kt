package mustafaozhan.github.com.mycurrencies.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val apiService = ApiClient.get().create(ApiInterface::class.java)

        val myCall = apiService.getByBase(mSpinner.selectedItem.toString())
        myCall.enqueue(object : Callback<ResponseAll> {
            override fun onResponse(call: Call<ResponseAll>?, response: Response<ResponseAll>?) {
                var value: Double = 0.0
                if (eTxt.text.toString() != "")
                    value = java.lang.Double.parseDouble(eTxt.text.toString())
                Log.w("${eTxt.text} ${mSpinner.selectedItem} ", "${(response!!.body()!!.rates!!.tRY!! * value)} Turkish Lira")
            }

            override fun onFailure(call: Call<ResponseAll>?, t: Throwable?) {

            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        mSpinner.onItemSelectedListener = this
        val categoriesDepartment = ArrayList<String>()
        categoriesDepartment.add("EUR")
        categoriesDepartment.add("AUD")
        categoriesDepartment.add("BGN")
        categoriesDepartment.add("BRL")
        categoriesDepartment.add("CAD")
        categoriesDepartment.add("CHF")
        categoriesDepartment.add("CNY")
        categoriesDepartment.add("CZK")
        categoriesDepartment.add("DKK")
        categoriesDepartment.add("GBP")
        categoriesDepartment.add("HKD")
        categoriesDepartment.add("HRK")
        categoriesDepartment.add("HUF")
        categoriesDepartment.add("IDR")
        categoriesDepartment.add("ILS")
        categoriesDepartment.add("INR")
        categoriesDepartment.add("JPY")
        categoriesDepartment.add("KRW")
        categoriesDepartment.add("MXN")
        categoriesDepartment.add("MYR")
        categoriesDepartment.add("NOK")
        categoriesDepartment.add("NZD")
        categoriesDepartment.add("PHP")
        categoriesDepartment.add("PLN")
        categoriesDepartment.add("RON")
        categoriesDepartment.add("RUB")
        categoriesDepartment.add("SEK")
        categoriesDepartment.add("SGD")
        categoriesDepartment.add("THB")
        categoriesDepartment.add("TRY")
        categoriesDepartment.add("USD")
        categoriesDepartment.add("ZAR")
        val dataAdapterDepartment = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriesDepartment)
        dataAdapterDepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpinner.adapter = dataAdapterDepartment


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
//                startActivity(Intent(this@MainActivity, MainActivity::class.java))
            }
            else -> {
            }
        }

        return true
    }
}