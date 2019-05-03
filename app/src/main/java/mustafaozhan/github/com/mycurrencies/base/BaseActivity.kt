package mustafaozhan.github.com.mycurrencies.base

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import de.mateware.snacky.Snacky
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.extensions.getImageResourceByName

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:37 PM on Arch Linux wit Love <3.
 */
@Suppress("TooManyFunctions")
abstract class BaseActivity : AppCompatActivity() {

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    @IdRes
    open var containerId: Int = R.id.content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutResId())
        getDefaultFragment()?.let {
            replaceFragment(it, false)
        }
    }

    open fun getDefaultFragment(): BaseFragment? = null

    protected fun setHomeAsUpEnabled(enabled: Boolean) {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(enabled)
        }
    }

    private fun addFragment(containerViewId: Int, fragment: BaseFragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(containerViewId, fragment, fragment.fragmentTag)
        ft.commit()
    }

    protected fun addFragment(fragment: BaseFragment) {
        addFragment(containerId, fragment)
    }

    fun replaceFragment(fragment: BaseFragment, withBackStack: Boolean) {
        if (withBackStack) {
            replaceFragmentWithBackStack(containerId, fragment)
        } else {
            replaceFragment(containerId, fragment)
        }
    }

    private fun replaceFragmentWithBackStack(containerViewId: Int, fragment: BaseFragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        ft.replace(containerViewId, fragment, fragment.fragmentTag)
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun replaceFragment(containerViewId: Int, fragment: BaseFragment) {
        val ft = supportFragmentManager.beginTransaction()
        if (supportFragmentManager.backStackEntryCount != 0)
            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
        ft.replace(containerViewId, fragment, fragment.fragmentTag)
        ft.commit()
    }

    fun clearBackStack() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun snacky(
        text: String,
        actionText: String = "",
        setIcon: String? = null,
        isLong: Boolean = true,
        action: () -> Unit = {}
    ) {
        Snacky.builder()
            .setBackgroundColor(ContextCompat.getColor(this, R.color.blue_grey_800))
            .setText(text)
            .setIcon(setIcon?.let { getImageResourceByName(setIcon) } ?: R.mipmap.ic_launcher)
            .setActivity(this)
            .setDuration(if (isLong) Snacky.LENGTH_LONG else Snacky.LENGTH_SHORT)
            .setActionText(actionText.toUpperCase())
            .setActionTextColor(ContextCompat.getColor(this, R.color.cyan_700))
            .setActionTextTypefaceStyle(Typeface.BOLD)
            .setActionClickListener {
                action()
            }
            .build()
            .show()
    }

    protected fun showDialog(
        title: String,
        description: String,
        positiveButton: String,
        cancelable: Boolean = true,
        function: () -> Unit = {}
    ) {
        if (!isFinishing) {
            val builder = AlertDialog
                .Builder(this, R.style.AlertDialogCustom)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(title)
                .setMessage(description)
                .setPositiveButton(positiveButton) { _, _ ->
                    function()
                }
                .setCancelable(cancelable)

            if (cancelable) {
                builder.setNegativeButton(getString(R.string.cancel), null)
            }

            builder.show()
        }
    }
}