package mustafaozhan.github.com.mycurrencies.base

import android.arch.lifecycle.ViewModelProviders

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:41 PM on Arch Linux wit Love <3.
 */
abstract class BaseMvvmFragment<VM : BaseViewModel> : BaseFragment() {

    protected abstract fun getViewModelClass(): Class<VM>

    private val viewModel: VM by lazy { ViewModelProviders.of(this).get(getViewModelClass()) }
}