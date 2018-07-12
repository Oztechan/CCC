package mustafaozhan.github.com.mycurrencies.main.activity

import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.BaseFragment
import mustafaozhan.github.com.mycurrencies.base.BaseMvvmActivity

class MainActivity : BaseMvvmActivity<MainActivityViewModel>() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_fragment_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.settings -> {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
            true
        }
        R.id.feedback -> {
            Toast.makeText(this, "Feedback clicked", Toast.LENGTH_SHORT).show()
            true
        }
        R.id.support -> {
            Toast.makeText(this, "Support clicked", Toast.LENGTH_SHORT).show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun getDefaultFragment(): BaseFragment = mustafaozhan.github.com.mycurrencies.main.fragment.MainFragment.newInstance()

    override fun getViewModelClass(): Class<MainActivityViewModel> = MainActivityViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

}