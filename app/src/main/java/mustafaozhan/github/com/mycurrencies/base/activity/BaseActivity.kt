package mustafaozhan.github.com.mycurrencies.base.activity

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import de.mateware.snacky.Snacky
import io.reactivex.disposables.CompositeDisposable
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.extensions.getImageResourceByName
import java.util.Locale
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:37 PM on Arch Linux wit Love <3.
 */
abstract class BaseActivity<TViewModel : BaseViewModel> : AppCompatActivity() {

    @Inject
    protected lateinit var viewModel: TViewModel

    @LayoutRes
    protected abstract fun getLayoutResId(): Int?

    @IdRes
    open var containerId: Int = R.id.content

    protected val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel.onLoaded()
        getLayoutResId()?.let {
            setContentView(it)
            replaceFragment(getDefaultFragment(), false)
        }
    }

    protected abstract fun getDefaultFragment(): Fragment?

    protected fun setHomeAsUpEnabled(enabled: Boolean) =
        supportActionBar?.setDisplayHomeAsUpEnabled(enabled)

    fun replaceFragment(fragment: Fragment?, withBackStack: Boolean) =
        fragment?.let {
            supportFragmentManager.beginTransaction().apply {

                if (supportFragmentManager.fragments.size > 0) {
                    setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                }

                replace(containerId, it, it.tag)
                if (withBackStack) addToBackStack(it.tag)
                commit()
            }
        }

    fun snacky(
        text: String,
        actionText: String = "",
        setIcon: String? = null,
        isLong: Boolean = true,
        action: () -> Unit = {}
    ) = Snacky.builder()
        .setBackgroundColor(ContextCompat.getColor(this, R.color.blue_grey_800))
        .setText(text)
        .setIcon(setIcon?.let { getImageResourceByName(setIcon) } ?: R.mipmap.ic_launcher)
        .setActivity(this)
        .setDuration(if (isLong) Snacky.LENGTH_LONG else Snacky.LENGTH_SHORT)
        .setActionText(actionText.toUpperCase(Locale.getDefault()))
        .setActionTextColor(ContextCompat.getColor(this, R.color.cyan_700))
        .setActionTextTypefaceStyle(Typeface.BOLD)
        .setActionClickListener { action() }
        .build()
        .show()

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
                .setPositiveButton(positiveButton) { _, _ -> function() }
                .setCancelable(cancelable)

            if (cancelable) {
                builder.setNegativeButton(getString(R.string.cancel), null)
            }

            builder.show()
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
