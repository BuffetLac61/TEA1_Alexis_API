package com.example.tea1_v01

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ShowListActivity : AppCompatActivity() {

    private lateinit var nomPseudoActif: TextView

    private lateinit var toolbar: Toolbar

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        toolbar = findViewById(R.id.toolbarParametres)
        setSupportActionBar(toolbar)

        val selectedItem = intent.getIntExtra("selectedItem", -1)
        val pseudoActif = intent.getStringExtra("pseudoActif")
        // Utilisez le numéro de l'élément sélectionné selon vos besoins
        nomPseudoActif = findViewById(R.id.nomProfilActif)

        val TodolistActive : ListeToDo =
            findProfilListeToDoByLogin(getProfilListeToDoList(), pseudoActif!!)?.getMesListesToDo()?.get(selectedItem)!!

        nomPseudoActif.text = "Profil : $pseudoActif - TodoList : " + TodolistActive.getTitreListeToDo()







    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Action à effectuer lorsque le bouton des paramètres est cliqué
                finish()
                finish()
                val intent = Intent(this, SettingsActivity::class.java)
                Log.i("PMR", "[OPENED]SettingsActivity")
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    // Enregistrer une liste de ProfilListeToDo dans les préférences partagées
    fun saveProfilListeToDoList(profilListeToDoList: MutableList<ProfilListeToDo>) {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(profilListeToDoList)
        val editor = sharedPreferences.edit()
        editor.putString("profilListeToDoList", json)
        editor.apply()
    }

    //fonction qui prend en paramètre une liste de ProfilListeToDo ainsi qu'un login, et renvoie la ProfilListeToDo qui correspond au login
    fun findProfilListeToDoByLogin(profilListeToDoList: List<ProfilListeToDo>, login: String): ProfilListeToDo? {
        return profilListeToDoList.find { it.login == login }
    }


    // Récupérer une liste de ProfilListeToDo depuis les préférences partagées
    fun getProfilListeToDoList(): MutableList<ProfilListeToDo> {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("profilListeToDoList", "")
        val type = object : TypeToken<MutableList<ProfilListeToDo>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }


    //Transformer la liste de profils en liste de leurs noms
    fun getListeToDoListAsString(listesDuProfil: ProfilListeToDo) : ArrayList<String>{
        var lestdestodo : ArrayList<String> = ArrayList()
        for(liste : ListeToDo in listesDuProfil.getMesListesToDo()) {
            liste.getTitreListeToDo().let { lestdestodo.add(it) }
        }
        return(lestdestodo)
    }

    //fonction qui prend en paramètre un ProfilListeToDo ainsi qu'un nom de liste, et renvoie la ListeToDo qui correspond au nom
    fun findListeToDoByName(profilListeToDo: ProfilListeToDo, listeName: String): ListeToDo? {
        return profilListeToDo.getMesListesToDo().find { it.getTitreListeToDo() == listeName }
    }

}