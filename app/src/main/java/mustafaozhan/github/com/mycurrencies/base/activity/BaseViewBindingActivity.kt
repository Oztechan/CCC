package mustafaozhan.github.com.mycurrencies.base.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding

abstract class BaseViewBindingActivity<TViewBinding : ViewBinding> : BaseActivity() {

    protected lateinit var binding: TViewBinding

    abstract fun bind()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
        setContentView(binding.root)
    }
}
