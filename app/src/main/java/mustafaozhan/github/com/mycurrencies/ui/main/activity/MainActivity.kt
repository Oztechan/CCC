package mustafaozhan.github.com.mycurrencies.ui.main.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.github.mustafaozhan.basemob.activity.BaseActivity
import com.github.mustafaozhan.logmob.logWarning
import com.github.mustafaozhan.scopemob.whether
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.tool.checkRemoteConfig
import mustafaozhan.github.com.mycurrencies.tool.showDialog
import mustafaozhan.github.com.mycurrencies.tool.showSnacky
import mustafaozhan.github.com.mycurrencies.tool.updateBaseContextLocale
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.calculator.CalculatorFragment
import mustafaozhan.github.com.mycurrencies.ui.main.fragment.settings.SettingsFragment
import org.jetbrains.anko.contentView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("TooManyFunctions")
open class MainActivity : BaseActivity() {

    companion object {
        const val BACK_DELAY: Long = 2
        const val AD_INITIAL_DELAY: Long = 50
        const val AD_PERIOD: Long = 250
    }

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var adObservableInterval: Disposable
    private lateinit var interstitialTextAd: InterstitialAd
    private lateinit var interstitialVideoAd: InterstitialAd
    private var adVisibility = false
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(CalculatorFragment.newInstance(), false)
        checkRemoteConfig(this)
        prepareAd()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()

        when (supportFragmentManager.findFragmentById(containerId)) {
            is CalculatorFragment -> menuInflater.inflate(R.menu.fragment_main_menu, menu)
            is SettingsFragment -> menuInflater.inflate(R.menu.fragment_settings_menu, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> replaceFragment(SettingsFragment.newInstance(), true)
            R.id.feedback -> sendFeedBack()
            R.id.support -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.app_market_link))
                )
                intent.resolveActivity(packageManager)?.let {
                    showDialog(this, R.string.support_us, R.string.rate_and_support, R.string.rate) {
                        startActivity(intent)
                    }
                }
            }
            R.id.removeAds ->
                showDialog(this, R.string.remove_ads, R.string.remove_ads_text, R.string.watch) {
                    showVideoAd()
                }
            R.id.onGithub -> {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.github_url))
                )
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showVideoAd() {
        interstitialVideoAd
            .whether { isLoaded }
            ?.apply {
                mainViewModel.updateAdFreeActivation()
                show()
            }

        prepareAd()
    }

    private fun sendFeedBack() {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/email"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.mail_developer)))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject))
            putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_extra_text) + "")
            startActivity(Intent.createChooser(this, getString(R.string.mail_intent_title)))
        }
    }

    private fun prepareAd() {
        interstitialTextAd = InterstitialAd(this)
        interstitialTextAd.adUnitId = getString(R.string.interstitial_text_ad_id)
        interstitialTextAd.loadAd(AdRequest.Builder().build())

        interstitialVideoAd = InterstitialAd(this)
        interstitialVideoAd.adUnitId = getString(R.string.interstitial_video_ad_id)
        interstitialVideoAd.loadAd(AdRequest.Builder().build())
    }

    private fun ad() {
        adVisibility = true
        adObservableInterval = Observable.interval(AD_INITIAL_DELAY, AD_PERIOD, TimeUnit.SECONDS)
            .debounce(0, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                interstitialTextAd
                    .whether(
                        { isLoaded },
                        { adVisibility },
                        { mainViewModel.isRewardExpired }
                    )
                    ?.apply { show() }
                    ?: run { prepareAd() }
            }, { logWarning(it) }
            )
    }

    override fun onResume() {
        super.onResume()
        ad()
    }

    override fun onPause() {
        super.onPause()
        adObservableInterval.dispose()
        adVisibility = false
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(containerId) is CalculatorFragment) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            doubleBackToExitPressedOnce = true
            showSnacky(contentView, R.string.click_back_again_to_exit)

            Completable.complete()
                .delay(BACK_DELAY, TimeUnit.SECONDS)
                .subscribe { doubleBackToExitPressedOnce = false }
                .addTo(compositeDisposable)
        } else {
            super.onBackPressed()
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }
}
