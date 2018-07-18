package mustafaozhan.github.com.mycurrencies.settings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.fragment_settings.*
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmFragment
import mustafaozhan.github.com.mycurrencies.settings.adapter.SettingAdapter
import mustafaozhan.github.com.mycurrencies.tools.setBackgroundByName


/**
 * Created by Mustafa Ozhan on 2018-07-12.
 */
class SettingsFragment : BaseMvvmFragment<SettingsFragmentViewModel>() {

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    private val settingAdapter: SettingAdapter by lazy { SettingAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewModel.initData()
        setSpinner()
        initRecycler()
        setListeners()
    }

    private fun initRecycler() {
        context?.let {
            mRecViewSettings.layoutManager = LinearLayoutManager(it)
            mRecViewSettings.adapter = settingAdapter
            settingAdapter.refreshList(viewModel.currencyList, viewModel.getCurrentBase(), viewModel.getBaseCurrency())
        }
    }

    private fun setListeners() {
        mSpinnerSettings.setOnItemSelectedListener { _, _, _, _ ->
            viewModel.setBaseCurrency(mSpinnerSettings.text.toString())
            imgBaseSettings.setBackgroundByName(mSpinnerSettings.text.toString())
        }

        mConstraintLayoutSettings.setOnClickListener {
            if (mSpinnerSettings.isActivated)
                mSpinnerSettings.collapse()
            else
                mSpinnerSettings.expand()
        }
    }

    fun refreshUI() {
        setSpinner()

    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    private fun setSpinner() {
        val spinnerList = ArrayList<String>()
        viewModel.currencyList.forEach {
            spinnerList.add(it.name)
        }
        if (spinnerList.toList().lastIndex < 1) {
            mSpinnerSettings.setItems("Select at least two currency from Settings")
            imgBaseSettings.setBackgroundByName("transparent")

        } else {
            mSpinnerSettings.setItems(spinnerList.toList())
            mSpinnerSettings.selectedIndex = spinnerList.indexOf(viewModel.getBaseCurrency().toString())
            imgBaseSettings.setBackgroundByName(viewModel.getBaseCurrency().toString())
        }


    }

    override fun getViewModelClass(): Class<SettingsFragmentViewModel> = SettingsFragmentViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_settings

}