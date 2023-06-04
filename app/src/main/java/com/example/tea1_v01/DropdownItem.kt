package com.example.tea1_v01

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class DropdownItem(val id: Int, val name: String) {
}

class DropdownAdapter(private val items: List<DropdownItem>) :
    RecyclerView.Adapter<DropdownAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dropdown_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)

        fun bind(item: DropdownItem) {
            // Lier les données à la vue ici
            textViewName.text = item.name
            itemView.setOnClickListener {
                // Réagir au clic sur l'élément de la vue déroulante
            }
        }
    }
}
