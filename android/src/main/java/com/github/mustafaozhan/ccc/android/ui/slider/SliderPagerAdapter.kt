/*
 * Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.android.ui.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.github.mustafaozhan.scopemob.castTo

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
