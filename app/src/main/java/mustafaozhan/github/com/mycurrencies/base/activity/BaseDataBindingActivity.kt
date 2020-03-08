package mustafaozhan.github.com.mycurrencies.base.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDataBindingActivity<TDataBinding : ViewDataBinding> : BaseActivity() {

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
