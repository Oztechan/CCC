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
import mustafaozhan.github.com.mycurrencies.model.web.Rates


class MainActivity : AppCompatActivity() {
    val currencyList = ArrayList<Currency>()
    val mAdapter = MyCurrencyAdapter(currencyList)
    private var myDatabase: PultusORM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setSupportActionBar(myToolbar)

        myDatabase = PultusORM("myDatabase.db", applicationContext.filesDir.absolutePath)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecViewCurrency.layoutManager = mLayoutManager
        mRecViewCurrency.itemAnimator = DefaultItemAnimator()
        mRecViewCurrency.adapter = mAdapter

        loadAd()
        setListeners()
        if (getPreferences(MODE_PRIVATE).getBoolean("is_first_run", true)) {
            init()
            getPreferences(MODE_PRIVATE).edit().putBoolean("is_first_run", false).apply()
        }
    }

    override fun onResume() {
        super.onResume()
        setSpinner()
        functionality()
    }

    private fun setListeners() {
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
    }

    private fun setSpinner() {
        val tempList = ArrayList<String>()
        myDatabase!!.find(Setting())
                .map { it -> it as Setting }
                .filter { it.isActive == "true" }
                .mapTo(tempList) { it.name.toString() }

        if (tempList.toList().lastIndex < 1)
            mSpinner.setItems("Please select at least two currency")
        else {
            mSpinner.setItems(tempList.toList())
            imgBase.setBackgroundByName(mSpinner.text.toString())
        }
    }

    private fun functionality() {

        Observable.create(Observable.OnSubscribe<String> { subscriber ->
            eTxt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = subscriber.onNext(s.toString())
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
                                var temp = if (text.isEmpty()) {
                                    eTxt.setText("")
                                    loading.visibility = View.INVISIBLE
                                    0.toString()
                                } else
                                    text
                                if (temp.startsWith("."))
                                    temp = "0" + text

                                val items = myDatabase!!.find(Setting())
                                currencyList.clear()

                                for (it in items) {
                                    it as Setting
                                    if (it.isActive == "true") {
                                        val result: Double = getResult(it.name!!, temp, response!!.body()!!.rates!!)
                                        if (mSpinner.text != it.name)
                                            currencyList.add(Currency(it.name.toString(), result))
                                    }
                                }
                                loading.visibility = View.INVISIBLE
                                mAdapter.notifyDataSetChanged()
                            }

                            override fun onFailure(call: Call<ResponseAll>?, t: Throwable?) {}
                        }) else
                        loading.visibility = View.INVISIBLE
                }, { e -> onError(e) })
    }

    private fun getResult(name: String, temp: String, rate: Rates): Double {
        when (name) {
            "EUR" -> return (rate.eUR?.times(temp.toDouble()) ?: temp.toDouble())
            "AUD" -> return (rate.aUD?.times(temp.toDouble()) ?: temp.toDouble())
            "BGN" -> return (rate.bGN?.times(temp.toDouble()) ?: temp.toDouble())
            "BRL" -> return (rate.bRL?.times(temp.toDouble()) ?: temp.toDouble())
            "CAD" -> return (rate.cAD?.times(temp.toDouble()) ?: temp.toDouble())
            "CHF" -> return (rate.cHF?.times(temp.toDouble()) ?: temp.toDouble())
            "CNY" -> return (rate.cNY?.times(temp.toDouble()) ?: temp.toDouble())
            "CZK" -> return (rate.cZK?.times(temp.toDouble()) ?: temp.toDouble())
            "DKK" -> return (rate.dKK?.times(temp.toDouble()) ?: temp.toDouble())
            "GBP" -> return (rate.gBP?.times(temp.toDouble()) ?: temp.toDouble())
            "HKD" -> return (rate.hKD?.times(temp.toDouble()) ?: temp.toDouble())
            "HRK" -> return (rate.hRK?.times(temp.toDouble()) ?: temp.toDouble())
            "HUF" -> return (rate.hUF?.times(temp.toDouble()) ?: temp.toDouble())
            "IDR" -> return (rate.iDR?.times(temp.toDouble()) ?: temp.toDouble())
            "ILS" -> return (rate.iLS?.times(temp.toDouble()) ?: temp.toDouble())
            "INR" -> return (rate.iNR?.times(temp.toDouble()) ?: temp.toDouble())
            "JPY" -> return (rate.jPY?.times(temp.toDouble()) ?: temp.toDouble())
            "KRW" -> return (rate.kRW?.times(temp.toDouble()) ?: temp.toDouble())
            "MXN" -> return (rate.mXN?.times(temp.toDouble()) ?: temp.toDouble())
            "MYR" -> return (rate.mYR?.times(temp.toDouble()) ?: temp.toDouble())
            "NOK" -> return (rate.nOK?.times(temp.toDouble()) ?: temp.toDouble())
            "NZD" -> return (rate.nZD?.times(temp.toDouble()) ?: temp.toDouble())
            "PHP" -> return (rate.pHP?.times(temp.toDouble()) ?: temp.toDouble())
            "PLN" -> return (rate.pLN?.times(temp.toDouble()) ?: temp.toDouble())
            "RON" -> return (rate.rON?.times(temp.toDouble()) ?: temp.toDouble())
            "RUB" -> return (rate.rUB?.times(temp.toDouble()) ?: temp.toDouble())
            "SEK" -> return (rate.sEK?.times(temp.toDouble()) ?: temp.toDouble())
            "SGD" -> return (rate.sGD?.times(temp.toDouble()) ?: temp.toDouble())
            "THB" -> return (rate.tHB?.times(temp.toDouble()) ?: temp.toDouble())
            "TRY" -> return (rate.tRY?.times(temp.toDouble()) ?: temp.toDouble())
            "USD" -> return (rate.uSD?.times(temp.toDouble()) ?: temp.toDouble())
            "ZAR" -> return (rate.zAR?.times(temp.toDouble()) ?: temp.toDouble())
            else -> return 0.0
        }
    }

    private fun init() {
        myDatabase?.save(Setting("EUR"))
        myDatabase?.save(Setting("AUD"))
        myDatabase?.save(Setting("BGN"))
        myDatabase?.save(Setting("BRL"))
        myDatabase?.save(Setting("CAD"))
        myDatabase?.save(Setting("CHF"))
        myDatabase?.save(Setting("CNY"))
        myDatabase?.save(Setting("CZK"))
        myDatabase?.save(Setting("DKK"))
        myDatabase?.save(Setting("GBP"))
        myDatabase?.save(Setting("HKD"))
        myDatabase?.save(Setting("HRK"))
        myDatabase?.save(Setting("HUF"))
        myDatabase?.save(Setting("IDR"))
        myDatabase?.save(Setting("ILS"))
        myDatabase?.save(Setting("INR"))
        myDatabase?.save(Setting("JPY"))
        myDatabase?.save(Setting("KRW"))
        myDatabase?.save(Setting("MXN"))
        myDatabase?.save(Setting("MYR"))
        myDatabase?.save(Setting("NOK"))
        myDatabase?.save(Setting("NZD"))
        myDatabase?.save(Setting("PHP"))
        myDatabase?.save(Setting("PLN"))
        myDatabase?.save(Setting("RON"))
        myDatabase?.save(Setting("RUB"))
        myDatabase?.save(Setting("SEK"))
        myDatabase?.save(Setting("SGD"))
        myDatabase?.save(Setting("THB"))
        myDatabase?.save(Setting("TRY"))
        myDatabase?.save(Setting("USD"))
        myDatabase?.save(Setting("ZAR"))
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
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link + packageName)))
                })
                .setNegativeButton("CANCEL", null)
        builder.show()
    }

    private fun loadAd() {
        MobileAds.initialize(this, resources.getString(R.string.banner_ad_unit_id))
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
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
}