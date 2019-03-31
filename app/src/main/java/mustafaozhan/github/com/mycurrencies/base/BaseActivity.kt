package mustafaozhan.github.com.mycurrencies.base

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import de.mateware.snacky.Snacky
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.extensions.getImageResourceByName
import java.io.File

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

    protected fun addFragment(containerViewId: Int, fragment: BaseFragment) {
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

    protected fun replaceFragmentWithBackStack(containerViewId: Int, fragment: BaseFragment) {
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

    protected fun replaceFragment(containerViewId: Int, fragment: BaseFragment) {
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

    fun snacky(text: String, actionText: String = "", setIcon: String? = null, action: () -> Unit = {}) {
        Snacky.builder()
            .setBackgroundColor(ContextCompat.getColor(this, R.color.blue_grey_800))
            .setText(text)
            .setIcon(
                if (setIcon != null) {
                    getImageResourceByName(setIcon)
                } else {
                    R.mipmap.ic_launcher
                }
            )
            .setActivity(this)
            .setDuration(Snacky.LENGTH_LONG)
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
        val builder = AlertDialog
            .Builder(this, R.style.AlertDialogCustom)
            .setIcon(R.mipmap.ic_launcher)
            .setTitle(title)
            .setMessage(description)
            .setPositiveButton(positiveButton) { _, _ ->
                function()
            }
            .setCancelable(cancelable)

        if (cancelable)
            builder.setNegativeButton(getString(R.string.cancel), null)

        builder.show()
    }

    fun clearApplicationData() {
        snacky(getString(R.string.init_app_data))
        val cacheDirectory = cacheDir
        val applicationDirectory = File(cacheDirectory.parent)
        if (applicationDirectory.exists()) {
            val fileNames = applicationDirectory.list()
            for (fileName in fileNames) {
                if (fileName != "lib") {
                    deleteFile(File(applicationDirectory, fileName))
                }
            }
        }
    }

    private fun deleteFile(file: File?): Boolean {
        var deletedAll = true
        if (file != null) {
            if (file.isDirectory) {
                val children = file.list()
                for (i in children.indices) {
                    deletedAll = deleteFile(File(file, children[i])) && deletedAll
                }
            } else {
                deletedAll = file.delete()
            }
        }

        return deletedAll
    }
}