package com.example.tea1_v01

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class DropdownItem(val id: Int, val name: String,val pseudoActif:String) {
}

//la classe DropdownAdapter2 correspond à la liste des To-Do Lists
class DropdownAdapter(private var items: List<DropdownItem>) : RecyclerView.Adapter<DropdownAdapter.ViewHolder>() {

    fun updateData(newItems: List<DropdownItem>) {
        items = newItems
        notifyDataSetChanged()
    }


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
            textViewName.text = item.name

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, ShowListActivity::class.java)
                intent.putExtra("pseudoActif", item.pseudoActif)
                intent.putExtra("selectedItem", adapterPosition)
                context.startActivity(intent) // Lancer l'intention pour ouvrir ShowListActivity
            }}
    }
}


