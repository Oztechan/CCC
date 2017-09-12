package mustafaozhan.github.com.mycurrencies.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_currency.view.*

import mustafaozhan.github.com.mycurrencies.R

/**
* Created by Mustafa Ozhan on Arch Linux at 11:05 PM on Arch Linux.
*/
class MyCurrencyAdapter(private var baseList: List<String>?) :
        RecyclerView.Adapter<MyCurrencyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCocktail(baseList!![position])

    }


    override fun getItemCount() = baseList!!.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindCocktail(base: String) {
            itemView.txtBase.text = base
            itemView.txtResult.text="100000"
        }


    }
}