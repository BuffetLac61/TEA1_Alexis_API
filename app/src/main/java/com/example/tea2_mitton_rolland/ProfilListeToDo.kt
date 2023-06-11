package com.example.tea1_v01
import android.os.Parcel

class ProfilListeToDo {
    var login: String? = null
    var password: String? = null
        private set
    private var mesListeToDo: MutableList<ListeToDo> = arrayListOf()


    constructor(login: String?, password: String?) {
        this.login=login
        this.password=password
    }

    constructor(login: String?, mesListeToDo: MutableList<ListeToDo>) {
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

    fun getMesListesToDo(): MutableList<ListeToDo> {
        return mesListeToDo
    }

    fun ajouterListe(uneListe: ListeToDo?) {
        if (uneListe != null) {
            mesListeToDo.add(uneListe)
        }
    }
    fun getlogin() : String?{
        return this.login
    }

    override fun toString(): String {
        var chaineAffichee : String ="$login"
        for (listei in this.mesListeToDo){
            chaineAffichee = chaineAffichee +"\n\t"+ listei.toString()
        }

        return (chaineAffichee+ "\n")
    }


}

