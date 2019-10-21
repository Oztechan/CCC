package mustafaozhan.github.com.mycurrencies.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import mustafaozhan.github.com.mycurrencies.R

class SliderPagerAdapter(var context: Context) : PagerAdapter() {

    private var layouts = intArrayOf(
        R.layout.slide_intro,
        R.layout.slide_filter,
        R.layout.slide_bug_report,
        R.layout.slide_disable_ads
    )

    override fun instantiateItem(container: ViewGroup, position: Int): View {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        val view = layoutInflater?.inflate(layouts[position], container, false)
        container.addView(view)
        return view ?: View(context)
    }

    override fun getCount(): Int {
        return layouts.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        val view = obj as? View
        container.removeView(view)
    }
}
