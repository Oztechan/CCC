package mustafaozhan.github.com.mycurrencies.base

import androidx.lifecycle.ViewModelProviders
import com.crashlytics.android.Crashlytics
import mustafaozhan.github.com.mycurrencies.base.activity.BaseActivity

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:39 PM on Arch Linux wit Love <3.
 */
abstract class BaseMvvmActivity<VM : BaseViewModel> : BaseActivity<VM>() {

    protected abstract fun getViewModelClass(): Class<VM>

    protected val viewModel: VM by lazy { ViewModelProviders.of(this).get(getViewModelClass()) }

    protected fun logException(t: Throwable) = Crashlytics.logException(t)
}
