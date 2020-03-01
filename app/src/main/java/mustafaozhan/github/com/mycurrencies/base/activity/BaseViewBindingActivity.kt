package mustafaozhan.github.com.mycurrencies.base.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import mustafaozhan.github.com.mycurrencies.base.viewmodel.BaseViewModel

abstract class BaseViewBindingActivity<TViewModel : BaseViewModel, TViewBinding : ViewBinding> :
    BaseActivity<TViewModel>() {

    protected lateinit var binding: TViewBinding

    abstract fun bind()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        setContentView(binding.root)
    }
}
