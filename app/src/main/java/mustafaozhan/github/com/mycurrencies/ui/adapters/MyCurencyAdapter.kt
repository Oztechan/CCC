package mustafaozhan.github.com.mycurrencies.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.View
import mustafaozhan.github.com.mycurrencies.R


/**
 * Created by Mustafa Ozhan on 10/7/17 at 6:56 PM on Arch Linux.
 */
class MyCurencyAdapter(private val moviesList: List<String>) : RecyclerView.Adapter<MyCurencyAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var type: TextView = view.findViewById(R.id.txtType)
        var amount: TextView = view.findViewById(R.id.txtAmount)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = moviesList[position]
//        holder.title.setText(movie.getTitle())
//        holder.genre.setText(movie.getGenre())
//        holder.year.setText(movie.getYear())
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }
}