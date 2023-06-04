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
        return "ListeToDo(titreListeToDo='$titreListeToDo', lesItems=${lesItems.contentToString()})"
    }
}
