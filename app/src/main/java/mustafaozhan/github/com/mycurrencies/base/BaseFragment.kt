package mustafaozhan.github.com.mycurrencies.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.MenuRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_main_toolbar.fragment_main_toolbar
import kotlinx.android.synthetic.main.layout_settings_toolbar.fragment_settings_toolbar
import mustafaozhan.github.com.mycurrencies.R

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:38 PM on Arch Linux wit Love <3.
 */
abstract class BaseFragment : Fragment() {

    val fragmentTag: String = this.javaClass.simpleName

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    @MenuRes
    open var menuResID: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    protected fun initToolbar() {
        when (getLayoutResId()) {
            R.layout.fragment_main -> getBaseActivity().setSupportActionBar(fragment_main_toolbar)
            R.layout.fragment_settings -> getBaseActivity().setSupportActionBar(fragment_settings_toolbar)
        }
    }

    protected fun getBaseActivity(): BaseActivity = activity as BaseActivity

    protected fun snacky(
        text: String,
        actionText: String = "",
        setIcon: String? = null,
        action: () -> Unit = {}
    ) = getBaseActivity().snacky(text, actionText, setIcon, action)

    protected fun clearAppData() = getBaseActivity().clearApplicationData()
}