package mustafaozhan.github.com.mycurrencies.base.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import mustafaozhan.github.com.mycurrencies.base.viewmodel.BaseViewModel

abstract class BaseDataBindingFragment<TViewModel : BaseViewModel, TDataBinding : ViewDataBinding> :
    BaseFragment<TViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = DataBindingUtil.inflate<TDataBinding>(
        inflater,
        getLayoutResId(),
        container,
        false
    ).apply {
        //   todo     setVariable(BR.viewModel, viewModel)
    }.root
}
