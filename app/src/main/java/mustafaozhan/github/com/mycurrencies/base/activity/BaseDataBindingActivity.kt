package mustafaozhan.github.com.mycurrencies.base.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import mustafaozhan.github.com.mycurrencies.base.viewmodel.BaseViewModel

abstract class BaseDataBindingActivity<TViewModel : BaseViewModel, TDataBinding : ViewDataBinding> :
    BaseActivity<TViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
    }

    private fun bind() =
        getLayoutResId()?.let {
            DataBindingUtil
                .setContentView<TDataBinding>(
                    this,
                    it
                ).apply {
                    //    todo                setVariable(BR.viewModel, viewModel)
                }
        }
}
