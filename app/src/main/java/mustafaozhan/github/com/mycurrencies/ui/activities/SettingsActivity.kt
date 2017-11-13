package mustafaozhan.github.com.mycurrencies.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

class SettingsActivity : AppCompatActivity() {
    private val settingsList = ArrayList<Setting>()
    private val mAdapter = SettingsAdapter(settingsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val myDatabase = PultusORM("myDatabase.db", applicationContext.filesDir.absolutePath)

        getItems()
        selectAll.setOnClickListener {
            doAsync {
                val updater: PultusORMUpdater = PultusORMUpdater.Builder()
                        .set("isActive", "true")
                        .build()
                myDatabase.update(Setting(), updater)
                runOnUiThread {
                    getItems()
                }
            }
        }
        deSelectAll.setOnClickListener {
            doAsync {
                val updater: PultusORMUpdater = PultusORMUpdater.Builder()
                        .set("isActive", "false")
                        .build()
                myDatabase.update(Setting(), updater)
                runOnUiThread {
                    getItems()
                }
            }
        }

        val items = myDatabase.find(Setting())
        val tempList = ArrayList<String>()
        items
                .map { it -> it as Setting }
                .filter { it.isActive == "true" }
                .mapTo(tempList) { it.name.toString() }
        if (!tempList.isEmpty()) {
            mSpinnerSettings.setItems(tempList.toList())
            imgBaseSettings.setBackgroundByName(mSpinnerSettings.text.toString())
        }
        mSpinnerSettings.setOnItemSelectedListener { _, _, _, _ ->
            imgBaseSettings.setBackgroundByName(mSpinnerSettings.text.toString())
        }
        mConstraintLayoutSettings.setOnClickListener {
            if (mSpinnerSettings.isActivated)
                mSpinnerSettings.collapse()
            else
                mSpinnerSettings.expand()
        }
    }

    private fun getItems() {
        val myDatabase = PultusORM("myDatabase.db", applicationContext.filesDir.absolutePath)
        settingsList.clear()
        val items = myDatabase.find(Setting())
        items.mapTo(settingsList) { it -> it as Setting }
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecViewSettings.layoutManager = mLayoutManager
        mRecViewSettings.itemAnimator = DefaultItemAnimator()
        mRecViewSettings.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        val interstitial = InterstitialAd(this@SettingsActivity)
        interstitial.adUnitId = resources.getString(R.string.interstitial)
        val adRequest1 = AdRequest.Builder().build()
        interstitial.loadAd(adRequest1)
        interstitial.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (interstitial.isLoaded) {
                    interstitial.show()
                }
            }
        }
        finish()
    }
}
