package mustafaozhan.github.com.mycurrencies.main.activity


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseFragment
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmActivity
import mustafaozhan.github.com.mycurrencies.main.fragment.MainFragment
import mustafaozhan.github.com.mycurrencies.settings.SettingsFragment

class MainActivity : BaseMvvmActivity<MainActivityViewModel>() {

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
        var newUserAgent: String? = webView.settings.userAgentString
        try {
            val ua = webView.settings.userAgentString
            val androidOSString = webView.settings.userAgentString.substring(ua.indexOf("("), ua.indexOf(")") + 1)
            newUserAgent = webView.settings.userAgentString.replace(androidOSString, "(X11; Linux x86_64)")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.userAgentString = newUserAgent
        webView.loadUrl("https://github.com/mustafaozhan/CurrencyConverterCalculator")
        webView.bringToFront()
        webView.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (webView.visibility == View.VISIBLE)
            webView.visibility = View.GONE
        else
            super.onBackPressed()

    }

    private fun showRateDialog() {
        val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Support us !")
                .setMessage("Please, rate and commend to the app at Google Play Store")
                .setPositiveButton("RATE") { _, _ ->
                    var link = "market://details?id="
                    try {
                        applicationContext.packageManager.getPackageInfo(MainActivity@ applicationContext.packageName + ":My Currencies", 0)
                    } catch (e: PackageManager.NameNotFoundException) {
                        link = "https://play.google.com/store/apps/details?id="
                    }
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link + applicationContext.packageName)))
                }
                .setNegativeButton("CANCEL", null)
        builder.show()
    }

    private fun sendFeedBack() {
        val email = Intent(Intent.ACTION_SEND)
        email.type = "text/email"
        email.putExtra(Intent.EXTRA_EMAIL, arrayOf("mr.mustafa.ozhan@gmail.com"))
        email.putExtra(Intent.EXTRA_SUBJECT, "Feedback for My Currencies")
        email.putExtra(Intent.EXTRA_TEXT, "Dear Developer," + "")
        startActivity(Intent.createChooser(email, "Send Feedback:"))
    }

}