package mustafaozhan.github.com.mycurrencies.ui.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_settings.*

import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.model.data.Setting
import mustafaozhan.github.com.mycurrencies.ui.adapters.SettingsAdapter
import ninja.sakib.pultusorm.core.*
import org.jetbrains.anko.doAsync
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import mustafaozhan.github.com.mycurrencies.model.extensions.setBackgroundByName
import android.text.Selection.getSelectionEnd
import android.text.Selection.getSelectionStart
import android.content.SharedPreferences


class SettingsActivity : AppCompatActivity() {
    private val settingsList = ArrayList<Setting>()
    private val spinnerList = ArrayList<String>()
    private val mAdapter = SettingsAdapter(settingsList)
    private var myDatabase: PultusORM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        myDatabase = PultusORM("myDatabase.db", applicationContext.filesDir.absolutePath)

        setListeners()
    }

    override fun onResume() {
        getSpinnerList()
        getSettingList()
        super.onResume()
    }

    private fun getSpinnerList() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val base = preferences.getString("base_currency", "EUR")
        spinnerList.clear()
        myDatabase!!.find(Setting())
                .map { it -> it as Setting }
                .filter { it.isActive == "true" }
                .mapTo(spinnerList) { it.name.toString() }
        spinnerList.remove(base)
        spinnerList.add(0, base)
        if (!spinnerList.isEmpty()) {
            mSpinnerSettings.setItems(spinnerList.toList())
            imgBaseSettings.setBackgroundByName(mSpinnerSettings.text.toString())
        }
    }

    private fun setListeners() {
        mSpinnerSettings.setOnItemSelectedListener { _, _, _, _ ->

            val editor = getPreferences(Context.MODE_PRIVATE).edit()
            editor.putString("base_currency", mSpinnerSettings.text.toString())
            editor.commit()

            imgBaseSettings.setBackgroundByName(mSpinnerSettings.text.toString())

        }
        mConstraintLayoutSettings.setOnClickListener {
            if (mSpinnerSettings.isActivated)
                mSpinnerSettings.collapse()
            else
                mSpinnerSettings.expand()
        }
        selectAll.setOnClickListener {
            doAsync {
                val updater: PultusORMUpdater = PultusORMUpdater.Builder()
                        .set("isActive", "true")
                        .build()
                myDatabase!!.update(Setting(), updater)
                runOnUiThread {
                    getSettingList()
                }
            }
        }
        deSelectAll.setOnClickListener {
            doAsync {
                val updater: PultusORMUpdater = PultusORMUpdater.Builder()
                        .set("isActive", "false")
                        .build()
                myDatabase?.update(Setting(), updater)
                runOnUiThread {
                    getSettingList()
                }
            }
        }
    }


    private fun getSettingList() {
        settingsList.clear()
        val items = myDatabase!!.find(Setting())
        items.mapTo(settingsList) { it -> it as Setting }
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecViewSettings.layoutManager = mLayoutManager
        mRecViewSettings.itemAnimator = DefaultItemAnimator()
        mRecViewSettings.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }

    private fun loadAd() {
        val interstitial = InterstitialAd(applicationContext)
        interstitial.adUnitId = resources.getString(R.string.interstitial)
        interstitial.loadAd(AdRequest.Builder().build())
        interstitial.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (interstitial.isLoaded)
                    interstitial.show()
            }
        }
    }

    override fun onBackPressed() {
        loadAd()
        finish()
    }
}
