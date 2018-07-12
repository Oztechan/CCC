package mustafaozhan.github.com.mycurrencies.base

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.MenuRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_main_toolbar.*
import kotlinx.android.synthetic.main.layout_settings_toolbar.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.settings.SettingsFragment


/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:38 PM on Arch Linux wit Love <3.
 */
abstract class BaseFragment : Fragment() {

    val fragmentTag: String = this.javaClass.simpleName

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    @MenuRes
    open var menuResID: Int? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initToolbar()
    }

    protected fun initToolbar() {
        when (getLayoutResId()) {
            R.layout.fragment_main -> getBaseActivity().setSupportActionBar(fragment_main_toolbar)
            R.layout.fragment_settings -> getBaseActivity().setSupportActionBar(fragment_settings_toolbar)
        }
    }

    protected fun getBaseActivity(): BaseActivity = activity as BaseActivity

    protected fun replaceFragment(fragment: BaseFragment) = getBaseActivity().replaceFragment(fragment, true)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        when (getLayoutResId()) {
            R.layout.fragment_main -> inflater!!.inflate(R.menu.fragment_main_menu, menu)
            R.layout.fragment_settings -> inflater!!.inflate(R.menu.fragment_settings_menu, menu)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.settings -> replaceFragment(SettingsFragment.newInstance())
            R.id.feedback -> sendFeedBack()
            R.id.support -> showRateDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRateDialog() {
        val builder = AlertDialog.Builder(getBaseActivity(), R.style.AlertDialogCustom)
                .setTitle("Support us !")
                .setMessage("Please, rate and commend to the app at Google Play Store")
                .setPositiveButton("RATE") { _, _ ->
                    var link = "market://details?id="
                    try {
                        getBaseActivity().packageManager.getPackageInfo(MainActivity@ getBaseActivity().packageName + ":My Currencies", 0)
                    } catch (e: PackageManager.NameNotFoundException) {
                        link = "https://play.google.com/store/apps/details?id="
                    }
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link + getBaseActivity().packageName)))
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