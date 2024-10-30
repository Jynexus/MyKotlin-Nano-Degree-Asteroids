package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

class AsteroidsAdapter : RecyclerView.Adapter<ViewHolder>() {
    var data = listOf<Asteroid>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var activity : MainActivity? = null

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item = data[position]
        holder.hazardousImg.visibility = View.VISIBLE

        if(item.isPotentiallyHazardous){ holder.hazardousImg.contentDescription = "Potentially hazardous asteroid image"}
        else{holder.hazardousImg.contentDescription = "Not hazardous asteroid image"}

        holder.closeDate.text = item.closeApproachDate
        holder.codeName.text = item.codename
        holder.hazardousImg.setImageResource(when(item.isPotentiallyHazardous){
            true -> R.drawable.ic_status_potentially_hazardous
            false -> R.drawable.ic_status_normal
        })
        holder.itemView.setOnClickListener {
            activity!!.asteroid = item
            Navigation.findNavController(holder.itemView)
                .navigate(R.id.action_showDetail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.astro_item,parent,false)
        return  ViewHolder(view)
    }
}

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val codeName = itemView.findViewById<TextView>(R.id.textViewCode)
    val closeDate = itemView.findViewById<TextView>(R.id.textViewDate)
    val hazardousImg = itemView.findViewById<ImageView>(R.id.imageViewHazardous)
}