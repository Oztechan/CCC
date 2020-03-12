package mustafaozhan.github.com.mycurrencies.base.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding

abstract class BaseDataBindingActivity<TDataBinding : ViewDataBinding> : BaseActivity() {

    protected lateinit var binding: TDataBinding
    abstract fun bind(): TDataBinding
    abstract fun onBinding(dataBinding: ViewDataBinding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bind()
        onBinding(binding)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }
}
