package mustafaozhan.github.com.mycurrencies.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
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
import android.view.WindowManager
import com.google.android.gms.ads.AdRequest
import io.reactivex.plugins.RxJavaPlugins.onError
import mustafaozhan.github.com.mycurrencies.model.data.Setting
import ninja.sakib.pultusorm.core.PultusORM
import com.google.android.gms.ads.MobileAds
import mustafaozhan.github.com.mycurrencies.model.extensions.setBackgroundByName
import android.content.pm.PackageManager


class MainActivity : AppCompatActivity() {
    val currencyList = ArrayList<Currency>()
    val mAdapter = MyCurrencyAdapter(currencyList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)


        setSupportActionBar(myToolbar)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecViewCurrency.layoutManager = mLayoutManager
        mRecViewCurrency.itemAnimator = DefaultItemAnimator()
        mRecViewCurrency.adapter = mAdapter


        MobileAds.initialize(this, resources.getString(R.string.banner_ad_unit_id))
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        if (getPreferences(MODE_PRIVATE).getBoolean("is_first_run", true)) {
            init()
            getPreferences(MODE_PRIVATE).getString("default_currency", "EUR")
            getPreferences(MODE_PRIVATE).edit().putBoolean("is_first_run", false).apply()
        }


    }

    override fun onResume() {
        super.onResume()
        set()
        functionality()
    }

    private fun set() {
        val myDatabase = PultusORM("myDatabase.db", applicationContext.filesDir.absolutePath)
        val items = myDatabase.find(Setting())

        val tempList = ArrayList<String>()

        items
                .map { it -> it as Setting }
                .filter { it.isActive == "true" }
                .mapTo(tempList) { it.name.toString() }



        if (tempList.toList().lastIndex < 1)
            mSpinner.setItems("Please select at least two currency")
        else {
            mSpinner.setItems(tempList.toList())
            // mSpinner.setItems(tempList.toList())
        }
        imgBase.setBackgroundByName(mSpinner.text.toString())
    }

    private fun functionality() {

        mSpinner.setOnItemSelectedListener { _, _, _, _ ->
            val temp = eTxt.text
            eTxt.text = null
            eTxt.text = temp
            eTxt.setSelection(eTxt.text.length)
            imgBase.setBackgroundByName(mSpinner.text.toString())
        }
        mConstraintLayout.setOnClickListener {
            if (mSpinner.isActivated)
                mSpinner.collapse()
            else
                mSpinner.expand()
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
                    mAdapter.notifyDataSetChanged()
                    if (!text.isEmpty())
                        myCall.clone().enqueue(object : Callback<ResponseAll> {
                            override fun onResponse(call: Call<ResponseAll>?, response: Response<ResponseAll>?) {

                                val tempCurrency = response!!.body()!!.rates!!

                                var temp = if (text.isEmpty()) {
                                    eTxt.setText("")
                                    0.toString()
                                } else
                                    text
                                if (temp.startsWith("."))
                                    temp = "0" + text

                                val myDatabase = PultusORM("myDatabase.db", applicationContext.filesDir.absolutePath)
                                val items = myDatabase.find(Setting())

                                currencyList.clear()

                                for (it in items) {
                                    it as Setting
                                    if (it.isActive == "true") {

                                        var result = 0.0
                                        when (it.name) {
                                            "EUR" -> {
                                                result = tempCurrency.eUR?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "AUD" -> {
                                                result = tempCurrency.aUD?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "BGN" -> {
                                                result = tempCurrency.bGN?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "BRL" -> {
                                                result = tempCurrency.bRL?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "CAD" -> {
                                                result = tempCurrency.cAD?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "CHF" -> {
                                                result = tempCurrency.cHF?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "CNY" -> {
                                                result = tempCurrency.cNY?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "CZK" -> {
                                                result = tempCurrency.cZK?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "DKK" -> {
                                                result = tempCurrency.dKK?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "GBP" -> {
                                                result = tempCurrency.gBP?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "HKD" -> {
                                                result = tempCurrency.hKD?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "HRK" -> {
                                                result = tempCurrency.hRK?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "HUF" -> {
                                                result = tempCurrency.hUF?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "IDR" -> {
                                                result = tempCurrency.iDR?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "ILS" -> {
                                                result = tempCurrency.iLS?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "INR" -> {
                                                result = tempCurrency.iNR?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "JPY" -> {
                                                result = tempCurrency.jPY?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "KRW" -> {
                                                result = tempCurrency.kRW?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "MXN" -> {
                                                result = tempCurrency.mXN?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "MYR" -> {
                                                result = tempCurrency.mYR?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "NOK" -> {
                                                result = tempCurrency.nOK?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "NZD" -> {
                                                result = tempCurrency.nZD?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "PHP" -> {
                                                result = tempCurrency.pHP?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "PLN" -> {
                                                result = tempCurrency.pLN?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "RON" -> {
                                                result = tempCurrency.rON?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "RUB" -> {
                                                result = tempCurrency.rUB?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "SEK" -> {
                                                result = tempCurrency.sEK?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "SGD" -> {
                                                result = tempCurrency.sGD?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "THB" -> {
                                                result = tempCurrency.tHB?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "TRY" -> {
                                                result = tempCurrency.tRY?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "USD" -> {
                                                result = tempCurrency.uSD?.times(temp.toDouble()) ?: temp.toDouble()
                                            }
                                            "ZAR" -> {
                                                result = tempCurrency.zAR?.times(temp.toDouble()) ?: temp.toDouble()
                                            }


                                        }
                                        if (mSpinner.text != it.name)
                                            currencyList.add(Currency(it.name.toString(), result))
                                    }

                                }
                                loading.visibility = View.INVISIBLE
                                mAdapter.notifyDataSetChanged()

                            }

                            override fun onFailure(call: Call<ResponseAll>?, t: Throwable?) {
                            }
                        })


                }, { e -> onError(e) })


    }

    private fun init() {
        val myDatabase = PultusORM("myDatabase.db", applicationContext.filesDir.absolutePath)
        myDatabase.save(Setting("EUR"))
        myDatabase.save(Setting("AUD"))
        myDatabase.save(Setting("BGN"))
        myDatabase.save(Setting("BRL"))
        myDatabase.save(Setting("CAD"))
        myDatabase.save(Setting("CHF"))
        myDatabase.save(Setting("CNY"))
        myDatabase.save(Setting("CZK"))
        myDatabase.save(Setting("DKK"))
        myDatabase.save(Setting("GBP"))
        myDatabase.save(Setting("HKD"))
        myDatabase.save(Setting("HRK"))
        myDatabase.save(Setting("HUF"))
        myDatabase.save(Setting("IDR"))
        myDatabase.save(Setting("ILS"))
        myDatabase.save(Setting("INR"))
        myDatabase.save(Setting("JPY"))
        myDatabase.save(Setting("KRW"))
        myDatabase.save(Setting("MXN"))
        myDatabase.save(Setting("MYR"))
        myDatabase.save(Setting("NOK"))
        myDatabase.save(Setting("NZD"))
        myDatabase.save(Setting("PHP"))
        myDatabase.save(Setting("PLN"))
        myDatabase.save(Setting("RON"))
        myDatabase.save(Setting("RUB"))
        myDatabase.save(Setting("SEK"))
        myDatabase.save(Setting("SGD"))
        myDatabase.save(Setting("THB"))
        myDatabase.save(Setting("TRY"))
        myDatabase.save(Setting("USD"))
        myDatabase.save(Setting("ZAR"))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.feedback -> {
                val email = Intent(Intent.ACTION_SEND)
                email.type = "text/email"
                email.putExtra(Intent.EXTRA_EMAIL, arrayOf("mr.mustafa.ozhan@gmail.com"))
                email.putExtra(Intent.EXTRA_SUBJECT, "Feedback for My Currencies")
                email.putExtra(Intent.EXTRA_TEXT, "Dear Developer," + "")
                startActivity(Intent.createChooser(email, "Send Feedback:"))
                return true
            }
            R.id.support -> {
                showRateDialog()
            }
        }

        return true
    }

    private fun showRateDialog() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Support us !")
                .setMessage("Please, rate and commend to the app at Google Play Store")
                .setPositiveButton("RATE", { _, _ ->
                    var link = "market://details?id="
                    try {
                        packageManager.getPackageInfo(MainActivity@ this.packageName + ":My Currencies", 0)
                    } catch (e: PackageManager.NameNotFoundException) {
                        link = "https://play.google.com/store/apps/details?id="
                    }
                    startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse(link + packageName)))
                })
                .setNegativeButton("CANCEL", null)
        builder.show()
    }
}