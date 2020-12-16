/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.github.mustafaozhan.basemob.activity.BaseVBActivity
import com.github.mustafaozhan.scopemob.castTo
import com.github.mustafaozhan.scopemob.whether
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.ActivitySliderBinding

class SliderActivity : BaseVBActivity<ActivitySliderBinding>() {

    companion object {
        private var layouts = arrayListOf(
            R.layout.layout_slide_intro,
            R.layout.layout_slide_bug_report,
            R.layout.layout_slide_disable_ads
        )
        var SLIDE_SIZE = 3
        const val TEXT_SIZE = 36f
        const val HTML_DOT_CODE = "&#8226;"
    }

    override fun bind() {
        binding = ActivitySliderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

            window.decorView.systemUiVisibility =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    layouts.add(R.layout.layout_slide_dark_mode)
                    SLIDE_SIZE++
                    if (resources.configuration.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO
                    ) {
                        flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        flags
                    }
                } else {
                    flags
                }
        }

        addBottomDots(0)
        changeStatusBarColor()
        setListeners()
    }

    private fun setListeners() {

        binding.viewPager.apply {
            adapter = SliderPagerAdapter(applicationContext, layouts)
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageSelected(position: Int) {
                    addBottomDots(position)

                    binding.btnNext.text = if (position == SLIDE_SIZE - 1) {
                        getString(R.string.got_it)
                    } else {
                        getString(R.string.next)
                    }
                }

                override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) = Unit
                override fun onPageScrollStateChanged(arg0: Int) = Unit
            })
        }

        binding.btnNext.setOnClickListener {
            getNextItem()
                .whether { it < SLIDE_SIZE }
                ?.let { binding.viewPager.currentItem = it }
                ?: run { launchMainActivity() }
        }
    }

    private fun addBottomDots(currentPage: Int) {
        binding.layoutDots.removeAllViews()
        val dots = arrayListOf<TextView>().apply {
            repeat(SLIDE_SIZE) {
                add(TextView(applicationContext))
            }
        }

        dots.forEach { textView ->
            textView.text = HtmlCompat.fromHtml(HTML_DOT_CODE, HtmlCompat.FROM_HTML_MODE_LEGACY)
            textView.textSize = TEXT_SIZE
            textView.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.color_background_weak
                )
            )
            binding.layoutDots.addView(textView)
        }

        if (dots.size > 0) {
            dots[currentPage].setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.color_text_weak
                )
            )
        }
    }

    private fun getNextItem() = binding.viewPager.currentItem.inc()

    private fun launchMainActivity() {
        binding.progressBar.visibility = View.VISIBLE
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    inner class SliderPagerAdapter(var context: Context, private var layouts: ArrayList<Int>) :
        PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            val view = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                ?.castTo<LayoutInflater>()
                ?.inflate(layouts[position], container, false)
            container.addView(view)
            return view ?: View(context)
        }

        override fun getCount() = layouts.size

        override fun isViewFromObject(view: View, obj: Any) = view == obj

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) =
            container.removeView(obj.castTo<View>())
    }
}
