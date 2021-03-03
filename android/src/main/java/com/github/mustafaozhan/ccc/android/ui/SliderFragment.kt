/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ccc.android.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.github.mustafaozhan.basemob.fragment.BaseVBFragment
import com.github.mustafaozhan.ccc.android.util.gone
import com.github.mustafaozhan.ccc.android.util.visible
import com.github.mustafaozhan.logmob.kermit
import com.github.mustafaozhan.scopemob.castTo
import com.github.mustafaozhan.scopemob.whether
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.databinding.FragmentSliderBinding

class SliderFragment : BaseVBFragment<FragmentSliderBinding>() {

    companion object {
        private var layouts = arrayListOf(
            R.layout.layout_slide_intro,
            R.layout.layout_slide_bug_report,
            R.layout.layout_slide_disable_ads
        )
        private const val TEXT_SIZE = 36f
        private const val HTML_DOT_CODE = "&#8226;"
        private var IS_THEME_SLIDE_CHECKED = false
    }

    override fun bind() {
        binding = FragmentSliderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kermit.d { "SliderActivity onCreate" }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !IS_THEME_SLIDE_CHECKED) {
            layouts.add(R.layout.layout_slide_dark_mode)
        }
        IS_THEME_SLIDE_CHECKED = true

        addBottomDots(0)
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.gone()
    }

    private fun setListeners() {

        binding.viewPager.apply {
            adapter = SliderPagerAdapter(requireContext(), layouts)
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageSelected(position: Int) {
                    kermit.d { "SliderActivity onPageSelected" }
                    addBottomDots(position)

                    binding.btnNext.text = if (position == layouts.size - 1) {
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
                .whether { it < layouts.size }
                ?.let { binding.viewPager.currentItem = it }
                ?: run {
                    binding.progressBar.visible()
                    navigate(
                        R.id.sliderFragment,
                        SliderFragmentDirections.actionSliderFragmentToCurrenciesFragment()
                    )
                }
        }
    }

    private fun addBottomDots(currentPage: Int) {
        binding.layoutDots.removeAllViews()
        val dots = arrayListOf<TextView>().apply {
            repeat(layouts.size) {
                add(TextView(requireContext()))
            }
        }

        dots.forEach { textView ->
            textView.text = HtmlCompat.fromHtml(HTML_DOT_CODE, HtmlCompat.FROM_HTML_MODE_LEGACY)
            textView.textSize = TEXT_SIZE
            textView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_background_weak
                )
            )
            binding.layoutDots.addView(textView)
        }

        if (dots.size > 0) {
            dots[currentPage].setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_text_weak
                )
            )
        }
    }

    private fun getNextItem() = binding.viewPager.currentItem.inc()
}

class SliderPagerAdapter(
    var context: Context,
    private var layouts: ArrayList<Int>
) : PagerAdapter() {

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
