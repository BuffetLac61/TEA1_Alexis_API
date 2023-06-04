package com.example.tea1_v01


class ProfilListeToDo {
    var login: String? = null
        private set
    private lateinit var mesListeToDo: MutableList<ListeToDo>


    constructor(login: String?) {
        // Constructeur par défaut
    }

    constructor(login: String?, mesListeToDo: MutableList<ListeToDo>?) {
        this.login = login
        if (mesListeToDo != null) {
            this.mesListeToDo = mesListeToDo
        }
    }

    private fun getMesListeToDo(): List<ListeToDo?>? {
        return this.mesListeToDo
    }

    fun setMesListesToDo(mesListesToDo: MutableList<ListeToDo>?){
        if (mesListesToDo != null) {
            this.mesListeToDo = mesListesToDo
        }
    }

    fun ajouterListe(uneListe: ListeToDo?) {
        if (uneListe != null) {
            mesListeToDo.add(uneListe)
        }
    }

    override fun toString(): String {
        return "ProfilListeToDo(login='$login', mesListeToDo=$mesListeToDo)"
    }



}

