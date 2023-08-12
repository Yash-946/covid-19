package com.example.covid19

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RVAdapter(private val context: Context,
              private val covidArrayList: ArrayList<Model>): RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val state:TextView = itemView.findViewById(R.id.state)
        val city:TextView = itemView.findViewById(R.id.city)
        val activedata:TextView = itemView.findViewById(R.id.activeData)
        val confirmeddata:TextView = itemView.findViewById(R.id.confirmedData)
        val deceaseddata:TextView = itemView.findViewById(R.id.deceasedData)
        val recovereddata:TextView = itemView.findViewById(R.id.recoveredData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(context).inflate(R.layout.item_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return covidArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = covidArrayList[position]
        holder.state.text = model.state
        holder.city.text = model.city
        holder.activedata.text =model.active
        holder.confirmeddata.text = model.confirmed
        holder.deceaseddata.text = model.deceased
        holder.recovereddata.text = model.recovered
    }
}
