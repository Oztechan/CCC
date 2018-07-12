package mustafaozhan.github.com.mycurrencies.main

import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmActivity

class MainActivity : BaseMvvmActivity<MainActivityViewModel>() {

    override fun getViewModelClass(): Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

}