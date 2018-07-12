package mustafaozhan.github.com.mycurrencies.main.fragment

import android.os.Bundle
import android.view.View
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment

/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class MainFragment: BaseMvvmFragment<MainFragmentViewModel>() {

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBarTitle(R.string.main_fragment_toolbar_title)
    }

    override fun getViewModelClass(): Class<MainFragmentViewModel> = MainFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_main

}