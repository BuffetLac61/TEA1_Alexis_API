package com.example.tea1_v01

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Est remplacé par ItemToDo

/*data class DropdownItem2(val id: Int, val name: String, val pseudoActif: String) {
    var isChecked: Boolean = false
    var itemToDo: ItemToDo? = null // Ajout de la propriété itemToDo

}*/

class ItemAdapter(//Remplace DropdownAdapter2
    private var items: List<ItemToDo>,
    private val onItemCheckListener: OnItemCheckListener
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dropdown_item_layout_2, parent, false)
        return ViewHolder(view, onItemCheckListener)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onItemCheckListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateData(newItems: List<ItemToDo>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun updateDataWithoutNotify(newData: List<ItemToDo>) {
        items = newData
    }


    interface OnItemCheckListener {
        fun onItemChecked(item: ItemToDo, isChecked: Boolean)
    }

    inner class ViewHolder(itemView: View, private val onItemCheckListener: OnItemCheckListener) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)

        init {
            checkBox.setOnCheckedChangeListener{ _, isChecked ->
                val item = items[adapterPosition]
                item.setFait(checkBox.isChecked)
                onItemCheckListener.onItemChecked(item, checkBox.isChecked)
            }
        }

        fun bind(item: ItemToDo, onItemCheckListener: OnItemCheckListener) {
            textViewName.text = item.getDescription()
            checkBox.isChecked = item.getFait() // Ajout de cette ligne pour initialiser l'état de la case à cocher
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                item.setFait(isChecked)
                onItemCheckListener.onItemChecked(item, isChecked)
            }
        }


    }
}
