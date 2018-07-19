package mustafaozhan.github.com.mycurrencies.base

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import mustafaozhan.github.com.mycurrencies.R
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import com.crashlytics.android.core.CrashlyticsCore
import mustafaozhan.github.com.mycurrencies.BuildConfig


/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:37 PM on Arch Linux wit Love <3.
 */
abstract class BaseActivity : AppCompatActivity() {

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    @IdRes
    open var containerId: Int = R.id.content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        Fabric.with(this, Crashlytics.Builder().core(core).build())

        setContentView(getLayoutResId())
        getDefaultFragment()?.let {
            replaceFragment(it, false)
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

    fun replaceFragment(fragment: BaseFragment, withBackStack: Boolean) {
        if (withBackStack) {
            replaceFragmentWithBackStack(containerId, fragment)
        } else {
            replaceFragment(containerId, fragment)
        }
    }

    protected fun replaceFragmentWithBackStack(containerViewId: Int, fragment: BaseFragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        ft.replace(containerViewId, fragment, fragment.fragmentTag)
        ft.addToBackStack(null)
        ft.commit()
    }

    protected fun replaceFragment(containerViewId: Int, fragment: BaseFragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
        ft.replace(containerViewId, fragment, fragment.fragmentTag)
        ft.commit()
    }

    fun clearBackStack() {
        if (supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}