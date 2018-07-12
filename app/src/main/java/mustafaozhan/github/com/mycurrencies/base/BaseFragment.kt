package mustafaozhan.github.com.mycurrencies.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:38 PM on Arch Linux wit Love <3.
 */
abstract class BaseFragment : Fragment() {
    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    protected fun getBaseActivity(): BaseActivity = activity as BaseActivity
}