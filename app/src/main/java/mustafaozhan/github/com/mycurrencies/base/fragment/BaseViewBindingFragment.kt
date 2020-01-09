package mustafaozhan.github.com.mycurrencies.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import mustafaozhan.github.com.mycurrencies.base.viewmodel.BaseViewModel

abstract class BaseViewBindingFragment<TViewModel : BaseViewModel, TViewBinding : ViewBinding> :
    BaseFragment<TViewModel>() {

    lateinit var binding: TViewBinding

    abstract fun bind()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root
}
