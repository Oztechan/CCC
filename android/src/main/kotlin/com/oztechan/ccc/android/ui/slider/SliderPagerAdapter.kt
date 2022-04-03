package com.oztechan.ccc.android.ui.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.github.submob.scopemob.castTo

class SliderPagerAdapter(
    var context: Context,
    private var layouts: ArrayList<Int>
) : PagerAdapter() {

    override fun instantiateItem(
        container: ViewGroup,
        position: Int
    ): View = context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE)
        ?.castTo<LayoutInflater>()
        ?.inflate(layouts[position], container, false)
        ?.also { container.addView(it) }
        ?: View(context)

    override fun getCount() = layouts.size

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        obj: Any
    ) = container.removeView(obj.castTo<View>())
}
