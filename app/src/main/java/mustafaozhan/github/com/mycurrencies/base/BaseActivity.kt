package mustafaozhan.github.com.mycurrencies.base

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.MenuRes
import android.support.annotation.StringRes
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.layout_main_toolbar.*
import mustafaozhan.github.com.mycurrencies.R


/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:37 PM on Arch Linux wit Love <3.
 */
abstract class BaseActivity : AppCompatActivity() {

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    @IdRes
    open var containerId: Int = R.id.content
    @StringRes
    open val screenTitleId: Int? = null
    @MenuRes
    open var menuResID: Int? = null

    private val toolbarWidget: Toolbar?
        get() = toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        initToolbar(screenTitleId)

        getDefaultFragment()?.let {
            replaceFragment(it)
        }
    }

    private fun initToolbar(title: Int?) {
        toolbarWidget?.let {
            setSupportActionBar(toolbarWidget)
            setActionBarTitle(title)
        }
    }

    fun setActionBarTitle(title: Int?) {
        title?.let {
            this.supportActionBar?.title = getString(it)
        }
    }

    open fun getDefaultFragment(): BaseFragment? = null

    protected fun setHomeAsUpEnabled(enabled: Boolean) {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(enabled)
        }
    }

    protected fun addFragment(containerViewId: Int, fragment: BaseFragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(containerViewId, fragment, fragment.fragmentTag)
        ft.commit()
    }

    protected fun addFragment(fragment: BaseFragment) {
        addFragment(containerId, fragment)
    }

    fun replaceFragment(fragment: BaseFragment) {
        replaceFragment(containerId, fragment)
    }

    protected fun replaceFragment(containerViewId: Int, fragment: BaseFragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(containerViewId, fragment, fragment.fragmentTag)
        ft.commit()
    }

    fun clearBackStack() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}