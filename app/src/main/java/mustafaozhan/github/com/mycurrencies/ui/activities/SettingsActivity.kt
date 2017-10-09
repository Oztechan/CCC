package mustafaozhan.github.com.mycurrencies.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_settings.*

import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.model.Setting
import mustafaozhan.github.com.mycurrencies.ui.adapters.SettingsAdapter

class SettingsActivity : AppCompatActivity() {
    private val settingsList = ArrayList<Setting>()
    private val mAdapter = SettingsAdapter(settingsList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecViewSettings.layoutManager = mLayoutManager
        mRecViewSettings.itemAnimator = DefaultItemAnimator()
        mRecViewSettings.adapter = mAdapter



        settingsList.add(Setting("EUR",true)) 
        settingsList.add(Setting("AUD",true))
        settingsList.add(Setting("BGN",true))
        settingsList.add(Setting("BRL",true))
        settingsList.add(Setting("CAD",true))
        settingsList.add(Setting("CHF",true)) 
        settingsList.add(Setting("CNY",true)) 
        settingsList.add(Setting("CZK",true)) 
        settingsList.add(Setting("DKK",true)) 
        settingsList.add(Setting("GBP",true))
        settingsList.add(Setting("HKD",true))
        settingsList.add(Setting("HRK",true)) 
        settingsList.add(Setting("HUF",true)) 
        settingsList.add(Setting("IDR",true)) 
        settingsList.add(Setting("ILS",true))
        settingsList.add(Setting("INR",true)) 
        settingsList.add(Setting("JPY",true)) 
        settingsList.add(Setting("KRW",true)) 
        settingsList.add(Setting("MXN",true)) 
        settingsList.add(Setting("MYR",true))
        settingsList.add(Setting("NOK",true))
        settingsList.add(Setting("NZD",true))
        settingsList.add(Setting("PHP",true))
        settingsList.add(Setting("PLN",true)) 
        settingsList.add(Setting("RON",true)) 
        settingsList.add(Setting("RUB",true)) 
        settingsList.add(Setting("SEK",true)) 
        settingsList.add(Setting("SGD",true)) 
        settingsList.add(Setting("THB",true))
        settingsList.add(Setting("TRY",true))
        settingsList.add(Setting("USD",true)) 
        settingsList.add(Setting("ZAR",true)) 

        mAdapter.notifyDataSetChanged()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
