package mustafaozhan.github.com.mycurrencies.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.model.Setting

/**
 * Created by Mustafa Ozhan on 10/9/17 at 12:57 PM on Arch Linux.
 */
class SettingsAdapter(private val settingsList: ArrayList<Setting>?) : RecyclerView.Adapter<SettingsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.textView)
        var checkBox: TextView = view.findViewById(R.id.checkBox)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_settings, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val setting = settingsList!![position]
        holder.name.text = setting.name
        holder.checkBox.isSelected=setting.isActive

    }

    override fun getItemCount(): Int = settingsList?.size ?: -1
}
