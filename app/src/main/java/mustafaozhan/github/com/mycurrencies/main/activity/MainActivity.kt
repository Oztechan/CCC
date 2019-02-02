package mustafaozhan.github.com.mycurrencies.main.activity


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_main.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseFragment
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmActivity
import mustafaozhan.github.com.mycurrencies.extensions.fadeIO
import mustafaozhan.github.com.mycurrencies.main.fragment.MainFragment
import mustafaozhan.github.com.mycurrencies.settings.SettingsFragment

class MainActivity : BaseMvvmActivity<MainActivityViewModel>() {

    private var doubleBackToExitPressedOnce = false

    override fun getDefaultFragment(): BaseFragment = MainFragment.newInstance()

    override fun getViewModelClass(): Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        when {
            supportFragmentManager.findFragmentById(containerId) is MainFragment -> menuInflater.inflate(R.menu.fragment_main_menu, menu)
            supportFragmentManager.findFragmentById(containerId) is SettingsFragment -> menuInflater.inflate(R.menu.fragment_settings_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.settings -> replaceFragment(SettingsFragment.newInstance(), true)
            R.id.feedback -> sendFeedBack()
            R.id.support -> showRateDialog()
            R.id.onGithub -> showGithub()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showGithub() {
        webView.apply {
            var newUserAgent: String? = settings.userAgentString
            try {
                val ua = settings.userAgentString
                val androidOSString = settings.userAgentString.substring(ua.indexOf("("), ua.indexOf(")") + 1)
                newUserAgent = settings.userAgentString.replace(androidOSString, "(X11; Linux x86_64)")
            } catch (e: Exception) {
                e.printStackTrace()
                Crashlytics.logException(e)
            }

            settings.apply {
                loadWithOverviewMode = true
                javaScriptEnabled = true
                useWideViewPort = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
                userAgentString = newUserAgent
            }
            loadUrl(getString(R.string.github_url))
            fadeIO(true)
            bringToFront()
            visibility = View.VISIBLE
        }
    }

    private fun showRateDialog() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle(getString(R.string.support_us))
                .setMessage(R.string.rate_and_support)
                .setPositiveButton(getString(R.string.rate)) { _, _ ->
                    var link = "market://details?id="
                    try {
                        applicationContext.packageManager.getPackageInfo(applicationContext.packageName + ":My Currencies", 0)
                    } catch (e: PackageManager.NameNotFoundException) {
                        link = "https://play.google.com/store/apps/details?id="
                        Crashlytics.logException(e)
                    }
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link + applicationContext.packageName)))
                }
                .setNegativeButton(getString(R.string.cancel), null)
        builder.show()
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

    override fun onBackPressed() {
        when {
            webView.visibility == View.VISIBLE -> {
                webView.apply {
                    fadeIO(false)
                    visibility = View.GONE
                }
            }
            supportFragmentManager.findFragmentById(containerId) is MainFragment -> {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    return
                }
                doubleBackToExitPressedOnce = true
                snacky(getString(R.string.click_back_again_to_exit))
                Handler().postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 2000)
            }
            else -> super.onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        webView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }
}