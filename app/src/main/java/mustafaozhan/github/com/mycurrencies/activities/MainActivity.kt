package mustafaozhan.github.com.mycurrencies.activities

import android.annotation.SuppressLint
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
import mustafaozhan.github.com.mycurrencies.adapters.MyCurrencyAdapter
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.WindowManager
import com.google.android.gms.ads.AdRequest
import io.reactivex.plugins.RxJavaPlugins.onError
import mustafaozhan.github.com.mycurrencies.model.data.Setting
import ninja.sakib.pultusorm.core.PultusORM
import com.google.android.gms.ads.MobileAds
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.android.synthetic.main.keyboard_content.*
import mustafaozhan.github.com.mycurrencies.model.web.Rates
import mustafaozhan.github.com.mycurrencies.utils.getStringPreferences
import mustafaozhan.github.com.mycurrencies.utils.putStringPreferences
import mustafaozhan.github.com.mycurrencies.utils.setBackgroundByName
import org.mariuszgromada.math.mxparser.Expression


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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            eTxt.showSoftInputOnFocus = false
        else
            eTxt.setTextIsSelectable(true)

        loadAd()
        setListeners()
        if (getPreferences(MODE_PRIVATE).getBoolean("is_first_run", true)) {
            putStringPreferences(applicationContext, "base_currency", "EUR")
            init()
            getPreferences(MODE_PRIVATE).edit().putBoolean("is_first_run", false).apply()
        }
    }

    override fun onResume() {
        setSpinner()
        super.onResume()
    }

    @SuppressLint("SetTextI18n")
    private fun setListeners() {
        mSpinner.setOnItemSelectedListener { _, _, _, _ ->
            refreshEditText()
            imgBase.setBackgroundByName(mSpinner.text.toString())
        }
        mConstraintLayout.setOnClickListener {
            if (mSpinner.isActivated)
                mSpinner.collapse()
            else
                mSpinner.expand()
        }
        btnSeven.setOnClickListener { eTxt.setText(eTxt.text.toString() + "7") }
        btnEight.setOnClickListener { eTxt.setText(eTxt.text.toString() + "8") }
        btnNine.setOnClickListener { eTxt.setText(eTxt.text.toString() + "9") }
        btnDivide.setOnClickListener { eTxt.setText(eTxt.text.toString() + "/") }
        btnFour.setOnClickListener { eTxt.setText(eTxt.text.toString() + "4") }
        btnFive.setOnClickListener { eTxt.setText(eTxt.text.toString() + "5") }
        btnSix.setOnClickListener { eTxt.setText(eTxt.text.toString() + "6") }
        btnMultiply.setOnClickListener { eTxt.setText(eTxt.text.toString() + "*") }
        btnOne.setOnClickListener { eTxt.setText(eTxt.text.toString() + "1") }
        btnTwo.setOnClickListener { eTxt.setText(eTxt.text.toString() + "2") }
        btnThree.setOnClickListener { eTxt.setText(eTxt.text.toString() + "3") }
        btnMinus.setOnClickListener { eTxt.setText(eTxt.text.toString() + "-") }
        btnDot.setOnClickListener { eTxt.setText(eTxt.text.toString() + ".") }
        btnZero.setOnClickListener { eTxt.setText(eTxt.text.toString() + "0") }
        btnPercent.setOnClickListener { eTxt.setText(eTxt.text.toString() + "%") }
        btnPlus.setOnClickListener { eTxt.setText(eTxt.text.toString() + "+") }
        btnDoubleZero.setOnClickListener { eTxt.setText(eTxt.text.toString() + "000") }
        btnAc.setOnClickListener {
            eTxt.setText("")
            txtResult.text = ""
        }
        btnDelete.setOnClickListener {
            if (eTxt.text.toString() != "")
                eTxt.setText(eTxt.text.toString().substring(0, eTxt.text.toString().length - 1))
        }


    }

    private fun refreshEditText() {
        val temp = eTxt.text
        eTxt.text = null
        eTxt.text = temp
        eTxt.setSelection(eTxt.text.length)
    }

    private fun setSpinner() {
        val base = getStringPreferences(applicationContext, "base_currency", "EUR")
        val tempList = ArrayList<String>()
        myDatabase!!.find(Setting())
                .map { it -> it as Setting }
                .filter { it.isActive == "true" }
                .mapTo(tempList) { it.name.toString() }

        if (tempList.contains(base)) {
            tempList.remove(base)
            tempList.add(0, base)
        }
        if (tempList.toList().lastIndex < 1) {
            mSpinner.setItems("Select at least two currency from Settings")
            imgBase.setBackgroundByName("transparent")
            currencyList.clear()
            mAdapter.notifyDataSetChanged()
        } else {
            mSpinner.setItems(tempList.toList())
            imgBase.setBackgroundByName(mSpinner.text.toString())
            functionality()
        }
    }

    private fun functionality() {
        refreshEditText()
        Observable.create(Observable.OnSubscribe<String> { subscriber ->
            eTxt.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = subscriber.onNext(s.toString())
            })
        }).debounce(1000, TimeUnit.MILLISECONDS)
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
                            @SuppressLint("SetTextI18n")
                            override fun onResponse(call: Call<ResponseAll>?, response: Response<ResponseAll>?) {
                                var temp = if (text.isEmpty()) {
                                    eTxt.setText("")
                                    loading.visibility = View.INVISIBLE
                                    0.toString()
                                } else
                                    text
                                if (temp.startsWith("."))
                                    temp = "0$text"

                                val items = myDatabase!!.find(Setting())
                                currencyList.clear()
                                val calculatedValue = calculate(temp)
                                if (calculatedValue != "" && calculatedValue != "NaN")
                                    txtResult.text = "=    $calculatedValue"
                                else
                                    txtResult.text = ""

                                try {
                                    if (calculatedValue != "NaN")
                                        for (it in items) {
                                            it as Setting
                                            if (it.isActive == "true") {
                                                val result: Double = getResult(it.name!!, calculatedValue, response!!.body()!!.rates!!)
                                                if (mSpinner.text != it.name)
                                                    currencyList.add(Currency(it.name.toString(), result))
                                            }
                                        }
                                } catch (e: Exception) {
                                    e.stackTrace
                                }
                                loading.visibility = View.INVISIBLE
                                mAdapter.notifyDataSetChanged()
                            }

                            override fun onFailure(call: Call<ResponseAll>?, t: Throwable?) {}
                        }) else
                        loading.visibility = View.INVISIBLE
                }, { e -> onError(e) })
    }

    private fun calculate(text: String?): String {
        var result: String? = null

        if (text != null) {
            result = if (text.contains("%"))
                Expression(text.replace("%", "/100*")).calculate().toString()
            else
                Expression(text).calculate().toString()
        }

        return result.toString()
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


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> startActivity(Intent(applicationContext, SettingsActivity::class.java))
            R.id.feedback -> sendFeedBack()
            R.id.support -> showRateDialog()
        }
        return true
    }

    private fun sendFeedBack() {
        val email = Intent(Intent.ACTION_SEND)
        email.type = "text/email"
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf("mr.mustafa.ozhan@gmail.com"))
        email.putExtra(Intent.EXTRA_SUBJECT, "Feedback for My Currencies")
        email.putExtra(Intent.EXTRA_TEXT, "Dear Developer," + "")
        startActivity(Intent.createChooser(email, "Send Feedback:"))
    }

    private fun loadAd() {
        MobileAds.initialize(applicationContext, resources.getString(R.string.banner_ad_unit_id))
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}