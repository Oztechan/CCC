package mustafaozhan.github.com.mycurrencies.slider

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_slider.btn_next
import kotlinx.android.synthetic.main.activity_slider.layout_dots
import kotlinx.android.synthetic.main.activity_slider.progress_bar
import kotlinx.android.synthetic.main.activity_slider.view_pager
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.base.activity.BaseActivity
import mustafaozhan.github.com.mycurrencies.main.activity.MainActivity
import mustafaozhan.github.com.mycurrencies.slider.adapter.ViewPagerAdapter

class SliderActivity : BaseActivity<SliderActivityViewModel>() {

    companion object {
        const val SLIDE_SIZE = 4
        const val TEXT_SIZE = 36f
    }

    override fun getDefaultFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        addBottomDots(0)
        changeStatusBarColor()
        setListeners()
    }

    private fun setListeners() {
        view_pager?.apply {
            adapter = ViewPagerAdapter(applicationContext)
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageSelected(position: Int) {
                    addBottomDots(position)

                    btn_next.text = if (position == SLIDE_SIZE - 1) {
                        getString(R.string.got_it)
                    } else {
                        getString(R.string.next)
                    }
                }

                override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) = Unit
                override fun onPageScrollStateChanged(arg0: Int) = Unit
            })
        }

        btn_next.setOnClickListener {
            // checking for last page
            // if last page home screen will be launched
            val current = getItem(+1)

            if (current < SLIDE_SIZE) {
                // move to next screen
                view_pager.currentItem = current
            } else {
                launchMainActivity()
            }
        }
    }

    private fun addBottomDots(currentPage: Int) {
        layout_dots.removeAllViews()
        val dots = arrayListOf<TextView>().apply {
            repeat(SLIDE_SIZE) {
                add(TextView(applicationContext))
            }
        }

        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i].text = HtmlCompat.fromHtml("&#8226;", HtmlCompat.FROM_HTML_MODE_LEGACY)
            dots[i].textSize = TEXT_SIZE
            dots[i].setTextColor(ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark))
            layout_dots.addView(dots[i])
        }

        if (dots.size > 0) {
            dots[currentPage].setTextColor(ContextCompat.getColor(applicationContext, R.color.colorPrimaryLight))
        }
    }

    private fun getItem(i: Int): Int {
        return view_pager.currentItem + i
    }

    private fun launchMainActivity() {
        progress_bar.visibility = View.VISIBLE
        viewModel.setSliderShown()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    override fun getLayoutResId() = R.layout.activity_slider
}
