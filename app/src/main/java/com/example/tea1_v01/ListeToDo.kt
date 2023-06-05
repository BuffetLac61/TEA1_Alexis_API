package com.example.tea1_v01


class ListeToDo {
    private var titreListeToDo: String = ""
    private var lesItems: Array<ItemToDo> = arrayOf()

    fun setTitreListeToDo(titre: String) {
        titreListeToDo = titre
    }

    fun getTitreListeToDo(): String {
        return titreListeToDo
    }

    fun setLesItems(items: Array<ItemToDo>) {
        lesItems = items
    }

    fun addItem(description: String){
        val newList = this.getLesItems().toMutableList() // Convertir la liste en MutableList
        val newItem = ItemToDo(description)
        newList.add(newItem)
        //val newListToDo : ListeToDo = ListeToDo()
        //newListToDo.setLesItems(newList.toTypedArray())
        this.lesItems = newList.toTypedArray()
    }

    fun getLesItems(): Array<ItemToDo> {
        return lesItems
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
