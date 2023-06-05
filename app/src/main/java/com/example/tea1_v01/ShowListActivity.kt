package com.example.tea1_v01

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ShowListActivity : AppCompatActivity() {

    private lateinit var nomPseudoActif: TextView

    private lateinit var toolbar: Toolbar


    private lateinit var editTextNewList: EditText
    private lateinit var btnOk: Button

    private lateinit var ListdeToDo: MutableList<ItemToDo>

    private lateinit var todoListString: MutableList<String>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        toolbar = findViewById(R.id.toolbarParametres)
        setSupportActionBar(toolbar)


        val editTextNewTodo = findViewById<EditText>(R.id.editTextNewList)


        val selectedItem = intent.getIntExtra("selectedItem", -1)
        val pseudoActif = intent.getStringExtra("pseudoActif")
        // Utilisez le numéro de l'élément sélectionné selon vos besoins
        nomPseudoActif = findViewById(R.id.nomProfilActif)
        editTextNewList = findViewById(R.id.editTextNewList)
        btnOk = findViewById(R.id.btnOk)

        val ListdeProfilsDeListeToDo = getProfilListeToDoList()

        val TodolistActive : ListeToDo =
            findProfilListeToDoByLogin(getProfilListeToDoList(), pseudoActif!!)?.getMesListesToDo()?.get(selectedItem)!!

        nomPseudoActif.text = "Profil : $pseudoActif / TodoList : " + TodolistActive.getTitreListeToDo()

        var item1 = ItemToDo("Faire la vaisselle")
        var item2 = ItemToDo("Faire la vaisselle encore")
        var item3 = ItemToDo("reFaire la vaisselle")
        var items = arrayOf(item1, item2, item3)
        TodolistActive.setLesItems(items)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewShowActivity)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dropdownItems = fromListToDoToDropDownItem2(TodolistActive,pseudoActif)

        var adapter = DropdownAdapter2(dropdownItems)
        recyclerView.adapter = adapter

        btnOk.setOnClickListener {
            //actions à effectuer quand on appuye sur OK
            val newTodo : String = editTextNewList.text.toString()//On récupère le pseudo qui est dans le champ



                            if(newTodo!="") { //on vérifie qu'il y ai bien un nom rentré
                                //Toast.makeText(applicationContext, newTodo, Toast.LENGTH_SHORT).show()
                                todoListString = getToDoListAsString(TodolistActive)

                                if (todoListString.contains(newTodo)) {//
                                    Toast.makeText(applicationContext, "Cette ToDo existe déjà", Toast.LENGTH_SHORT).show()
                                } else {
                                    TodolistActive.addItem(newTodo) //on ajouter l'item à la liste
                                    findProfilListeToDoByLogin(getProfilListeToDoList(), pseudoActif!!)?.getMesListesToDo()!!.get(selectedItem).addItem(newTodo)

                                }


                                val dropdownItems = fromListToDoToDropDownItem2(TodolistActive,pseudoActif)
                                adapter.updateData(dropdownItems)
                                adapter.notifyDataSetChanged()





                                Log.i("PMR", "[OPENED]Contenu de ListdeProfilsDeListeToDo : $ListdeProfilsDeListeToDo")


                                saveProfilListeToDoList(ListdeProfilsDeListeToDo) //On sauvegarde les modifs

                            }

                    }






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

    fun fromListToDoToDropDownItem2(Listetodo1: ListeToDo,pseudoActif:String) : List<DropdownItem2>{
        val i = 1
        val dropdownItems = mutableListOf<DropdownItem2>()
        for(listei in Listetodo1.getLesItems()){
            dropdownItems.add(DropdownItem2(i,listei.getDescription(), pseudoActif))
        }
        return(dropdownItems)
    }

    fun getToDoListAsString(TodolistActive1: ListeToDo ):MutableList<String>{
        ListdeToDo= TodolistActive1.getLesItems().toMutableList()//Récupère les items de la liste de todo TodolistActive
        var listeDeToDoString : MutableList<String> = mutableListOf()
        for(Itemi : ItemToDo in ListdeToDo){
            listeDeToDoString.add(Itemi.getDescription())
        }
        return(listeDeToDoString)
    }
}