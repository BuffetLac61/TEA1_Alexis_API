package com.example.tea1_v01

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class DropdownItem2(val id: Int, val name: String,val pseudoActif:String) {
}


class DropdownAdapter2(private var items: List<DropdownItem2>) : RecyclerView.Adapter<DropdownAdapter2.ViewHolder>() {

    fun updateData(newItems: List<DropdownItem2>) {
        items = newItems
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dropdown_item_layout_2, parent, false)
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
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        fun bind(item: DropdownItem2) {
            textViewName.text = item.name
            //checkBox.isChecked = false

            itemView.setOnClickListener {
            }}
    }
}


