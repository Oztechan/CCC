package mustafaozhan.github.com.mycurrencies.base

import androidx.lifecycle.ViewModelProviders

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:39 PM on Arch Linux wit Love <3.
 */
abstract class BaseMvvmActivity<VM : BaseViewModel> : BaseActivity() {

    protected abstract fun getViewModelClass(): Class<VM>

    protected val viewModel: VM by lazy { ViewModelProviders.of(this).get(getViewModelClass()) }
}