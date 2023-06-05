package com.example.tea1_v01


class ListeToDo {
    private var titreListeToDo: String = ""
    private var lesItems: MutableList<ItemToDo> = arrayListOf()

    fun setTitreListeToDo(titre: String) {
        titreListeToDo = titre
    }

    fun getTitreListeToDo(): String {
        return titreListeToDo
    }

    fun setLesItems(items: ArrayList<ItemToDo>) {
        lesItems = items
    }

    fun addItem(description: String){
        //val newList = this.getLesItems().toMutableList() // Convertir la liste en MutableList
        //val newItem = ItemToDo(description)
        //newList.add(newItem)
        //val newListToDo : ListeToDo = ListeToDo()
        //newListToDo.setLesItems(newList.toTypedArray())
        //this.setLesItems(newList.toTypedArray())
        lesItems.add(ItemToDo(description))
    }

    fun getLesItems(): Array<ItemToDo> {
        return lesItems.toTypedArray()
    }

    fun rechercherItem(description: String): ItemToDo? {
        for (item in lesItems) {
            if (item.getDescription() == description) {
                return item
            }
        }
        return null
    }

    override fun toString(): String {
        var chaineAffichee : String ="###### $titreListeToDo ######"
        for (itemi in this.getLesItems()){
            chaineAffichee = chaineAffichee +"\n\t"+ itemi.toString()
        }

        return chaineAffichee+ "\n"
    }
}
