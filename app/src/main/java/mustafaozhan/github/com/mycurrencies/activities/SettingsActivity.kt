package mustafaozhan.github.com.mycurrencies.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_settings.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.model.data.Setting
import mustafaozhan.github.com.mycurrencies.adapters.SettingsAdapter
import ninja.sakib.pultusorm.core.*
import org.jetbrains.anko.doAsync
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import mustafaozhan.github.com.mycurrencies.interfaces.SettingsAdapterCallback
import mustafaozhan.github.com.mycurrencies.utils.putStringPreferences
import mustafaozhan.github.com.mycurrencies.utils.setBackgroundByName
import android.widget.Toast
import android.R.attr.orientation
import android.content.res.Configuration


class SettingsActivity : AppCompatActivity(), SettingsAdapterCallback {

    private val settingsList = ArrayList<Setting>()
    private val spinnerList = ArrayList<String>()
    private val mAdapter = SettingsAdapter(settingsList, this)
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
        val base = mustafaozhan.github.com.mycurrencies.utils.getStringPreferences(applicationContext, "base_currency", "EUR")
        spinnerList.clear()
        myDatabase!!.find(Setting())
                .map { it -> it as Setting }
                .filter { it.isActive == "true" }
                .mapTo(spinnerList) { it.name.toString() }
        if (spinnerList.contains(base)) {
            spinnerList.remove(base)
            spinnerList.add(0, base)
        }
        if (!spinnerList.isEmpty()) {
            mSpinnerSettings.setItems(spinnerList.toList())
            imgBaseSettings.setBackgroundByName(mSpinnerSettings.text.toString())
        }
    }

    private fun setListeners() {
        mSpinnerSettings.setOnItemSelectedListener { _, _, _, _ ->
            putStringPreferences(applicationContext, "base_currency", mSpinnerSettings.text.toString())
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
                    getSpinnerList()
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
                    spinnerList.clear()
                    mSpinnerSettings.setItems("")
                    imgBaseSettings.setBackgroundByName("transparent")
                    getSettingList()
                    getSpinnerList()
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            mRecViewSettings.layoutManager =  GridLayoutManager(applicationContext, 2)
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            mRecViewSettings.layoutManager =  GridLayoutManager(applicationContext, 1)
        }
    }

    private fun getSettingList() {
        settingsList.clear()
        val items = myDatabase!!.find(Setting())
        items.mapTo(settingsList) { it -> it as Setting }

        mRecViewSettings.layoutManager =  GridLayoutManager(applicationContext, 1)
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

    override fun onSettingsUpdated() {
        if (spinnerList.size == 1) {
            mSpinnerSettings.setItems("")
            imgBaseSettings.setBackgroundByName("transparent")
        }
        getSpinnerList()
    }
}
