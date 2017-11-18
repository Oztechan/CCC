package mustafaozhan.github.com.mycurrencies.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.model.data.Currency
import mustafaozhan.github.com.mycurrencies.utils.setBackgroundByName

/**
 * Created by Mustafa Ozhan on 10/7/17 at 6:56 PM on Arch Linux.
 */
class MyCurrencyAdapter(private val currencyList: ArrayList<Currency>?) : RecyclerView.Adapter<MyCurrencyAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var type: TextView = view.findViewById(R.id.txtType)
        var amount: TextView = view.findViewById(R.id.txtAmount)
        var icon: ImageView = view.findViewById(R.id.txtIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currency = currencyList!![position]
        holder.type.text = currency.name
        holder.amount.text = (Math.floor(currency.rate * 100) / 100).toString()
        holder.icon.setBackgroundByName(holder.type.text.toString())
    }

    override fun getItemCount(): Int = currencyList?.size ?: -1
}
