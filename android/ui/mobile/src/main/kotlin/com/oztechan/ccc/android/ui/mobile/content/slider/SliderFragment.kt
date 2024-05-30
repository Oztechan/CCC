/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */
package com.oztechan.ccc.android.ui.mobile.content.slider

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import co.touchlab.kermit.Logger
import com.github.submob.basemob.fragment.BaseVBFragment
import com.github.submob.scopemob.whether
import com.oztechan.ccc.android.ui.mobile.R
import com.oztechan.ccc.android.ui.mobile.databinding.FragmentSliderBinding
import com.oztechan.ccc.client.core.analytics.AnalyticsManager
import com.oztechan.ccc.client.core.analytics.model.ScreenName
import org.koin.android.ext.android.inject

class SliderFragment : BaseVBFragment<FragmentSliderBinding>() {

    private val analyticsManager: AnalyticsManager by inject()

    override fun getViewBinding() = FragmentSliderBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.i { "SliderFragment onCreate" }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !IS_THEME_SLIDE_CHECKED) {
            layouts.add(R.layout.layout_slide_dark_mode)
        }
        IS_THEME_SLIDE_CHECKED = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i { "SliderFragment onViewCreated" }
        binding.addBottomDots(0)
        binding.setListeners()
    }

    override fun onResume() {
        super.onResume()
        analyticsManager.trackScreen(ScreenName.Slider(0))

        Logger.i { "SliderFragment onResume" }
        binding.progressBar.isGone = true
    }

    private fun FragmentSliderBinding.setListeners() {
        viewPager.apply {
            adapter = SliderPagerAdapter(requireContext(), layouts)
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                override fun onPageSelected(position: Int) {
                    analyticsManager.trackScreen(ScreenName.Slider(position))

                    Logger.i { "SliderFragment onPageSelected $position" }

                    addBottomDots(position)

                    btnNext.text = if (position == layouts.size - 1) {
                        getString(R.string.got_it)
                    } else {
                        getString(R.string.next)
                    }
                }

                override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) = Unit
                override fun onPageScrollStateChanged(arg0: Int) = Unit
            })
        }

        btnNext.setOnClickListener { _ ->
            getNextItem()
                .whether { it < layouts.size }
                ?.let { position -> viewPager.currentItem = position }
                ?: run {
                    bottomBarSeparator.isGone = true
                    progressBar.isVisible = true
                    navigate(
                        R.id.sliderFragment,
                        SliderFragmentDirections.actionSliderFragmentToCurrenciesFragment()
                    )
                }
        }
    }

    private fun FragmentSliderBinding.addBottomDots(currentPage: Int) {
        layoutDots.removeAllViews()

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
                    R.color.background_weak
                )
            )
            layoutDots.addView(textView)
        }

        if (dots.size > 0) {
            dots[currentPage].setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.text_weak
                )
            )
        }
    }

    private fun getNextItem() = binding.viewPager.currentItem.inc()

    companion object {
        private val layouts = arrayListOf(
            R.layout.layout_slide_intro,
            R.layout.layout_slide_premium,
            R.layout.layout_slide_bug_report
        )
        private const val TEXT_SIZE = 36f
        private const val HTML_DOT_CODE = "&#8226;"
        private var IS_THEME_SLIDE_CHECKED = false
    }
}
