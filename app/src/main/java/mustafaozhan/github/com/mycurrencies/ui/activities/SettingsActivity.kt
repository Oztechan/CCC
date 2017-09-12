package mustafaozhan.github.com.mycurrencies.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_settings.*

import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.ui.adapters.MySettingsAdapter

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


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

        mRecViewSettings.layoutManager = LinearLayoutManager(applicationContext, LinearLayout.VERTICAL, false)
        val adapter = MySettingsAdapter(baseList)
        mRecViewSettings.adapter = adapter

    }

    override fun onBackPressed() {
        finish()
    }
}
