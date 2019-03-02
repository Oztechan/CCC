package mustafaozhan.github.com.mycurrencies.main.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.android.synthetic.main.activity_main.webView
import mustafaozhan.github.com.mycurrencies.BuildConfig
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseFragment
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmActivity
import mustafaozhan.github.com.mycurrencies.extensions.fadeIO
import mustafaozhan.github.com.mycurrencies.main.fragment.MainFragment
import mustafaozhan.github.com.mycurrencies.settings.SettingsFragment
import java.util.concurrent.TimeUnit

@Suppress("TooManyFunctions")
class MainActivity : BaseMvvmActivity<MainActivityViewModel>() {

    companion object {
        const val BACK_DELAY: Long = 2000
        const val CHECK_DURATION: Long = 4
        const val FB_RC_KEY_TITLE = "update_title"
        const val FB_RC_KEY_DESCRIPTION = "update_description"
        const val FB_RC_KEY_FORCE_UPDATE_VERSION = "force_update_version"
        const val FB_RC_KEY_LATEST_VERSION = "latest_version"
        const val FB_RC_KEY_URL = "update_url"
    }

    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig

    private var doubleBackToExitPressedOnce = false

    override fun getDefaultFragment(): BaseFragment = MainFragment.newInstance()

    override fun getViewModelClass(): Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAppUpdate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        when {
            supportFragmentManager.findFragmentById(containerId) is MainFragment ->
                menuInflater.inflate(R.menu.fragment_main_menu, menu)
            supportFragmentManager.findFragmentById(containerId) is SettingsFragment ->
                menuInflater.inflate(R.menu.fragment_settings_menu, menu)
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
            val ua = settings.userAgentString
            val androidOSString = settings.userAgentString.substring(ua.indexOf("("), ua.indexOf(")") + 1)
            val newUserAgent = settings.userAgentString.replace(androidOSString, "(X11; Linux x86_64)")

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
        val builder = AlertDialog
            .Builder(this, R.style.AlertDialogCustom)
            .setTitle(getString(R.string.support_us))
            .setMessage(R.string.rate_and_support)
            .setPositiveButton(getString(R.string.rate)) { _, _ ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_market_link))))
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

    private fun checkAppUpdate() {
        val versionCode = BuildConfig.VERSION_CODE

        val defaultMap = HashMap<String, Any>()
        defaultMap[FB_RC_KEY_TITLE] = "Update Available"
        defaultMap[FB_RC_KEY_DESCRIPTION] =
            "A new version of the application is available please click below to update the latest version."
        defaultMap[FB_RC_KEY_FORCE_UPDATE_VERSION] = versionCode
        defaultMap[FB_RC_KEY_LATEST_VERSION] = versionCode
        defaultMap[FB_RC_KEY_URL] = getString(R.string.app_market_link)

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.setConfigSettings(
            FirebaseRemoteConfigSettings
                .Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        )

        mFirebaseRemoteConfig.setDefaults(defaultMap)

        val fetchTask = mFirebaseRemoteConfig
            .fetch(
                if (BuildConfig.DEBUG)
                    0
                else
                    TimeUnit.HOURS.toSeconds(CHECK_DURATION))

        fetchTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                mFirebaseRemoteConfig.activateFetched()

                val title = getValue(FB_RC_KEY_TITLE, defaultMap)
                val description = getValue(FB_RC_KEY_DESCRIPTION, defaultMap)
                val forceUpdateVersion = Integer.parseInt(getValue(FB_RC_KEY_FORCE_UPDATE_VERSION, defaultMap))
                val latestAppVersion = Integer.parseInt(getValue(FB_RC_KEY_LATEST_VERSION, defaultMap))
                val updateLink = getValue(FB_RC_KEY_URL, defaultMap)

                val isCancelable = forceUpdateVersion <= versionCode

                if (latestAppVersion > versionCode) {
                    val builder = AlertDialog
                        .Builder(this, R.style.AlertDialogCustom)
                        .setTitle(title)
                        .setMessage(description)
                        .setPositiveButton(getString(R.string.update)) { _, _ ->
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(updateLink)))
                        }
                        .setCancelable(isCancelable)

                    if (isCancelable)
                        builder.setNegativeButton(getString(R.string.cancel), null)

                    builder.show()
                }
            }
        }
    }

    fun getValue(parameterKey: String, defaultMap: HashMap<String, Any>): String {
        var value = mFirebaseRemoteConfig.getString(parameterKey)
        if (TextUtils.isEmpty(value))
            value = defaultMap[parameterKey] as String

        return value
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
                }, BACK_DELAY)
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