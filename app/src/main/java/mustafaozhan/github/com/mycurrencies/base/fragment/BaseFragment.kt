package mustafaozhan.github.com.mycurrencies.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import mustafaozhan.github.com.mycurrencies.base.BaseViewModel
import mustafaozhan.github.com.mycurrencies.base.activity.BaseActivity
import javax.inject.Inject

/**
 * Created by Mustafa Ozhan on 7/10/18 at 9:38 PM on Arch Linux wit Love <3.
 */
abstract class BaseFragment<TViewModel : BaseViewModel> : Fragment() {

    @Inject
    protected lateinit var viewModel: TViewModel

    protected val compositeDisposable by lazy { CompositeDisposable() }

    val fragmentTag: String = this.javaClass.simpleName

    @LayoutRes
    protected abstract fun getLayoutResId(): Int

    @MenuRes
    open var menuResID: Int? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(getLayoutResId(), container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onLoaded()
        setHasOptionsMenu(true)
    }

    protected fun getBaseActivity() = activity as? BaseActivity<*>

    protected fun snacky(
        text: String?,
        actionText: String? = "",
        setIcon: String? = null,
        isLong: Boolean = true,
        action: () -> Unit = {}
    ) = getBaseActivity()?.snacky(text ?: "", actionText ?: "", setIcon, isLong, action)
}
