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

class ShowListActivity : AppCompatActivity(), DropdownAdapter2.OnItemCheckListener {

    private lateinit var nomPseudoActif: TextView
    private lateinit var ListdeProfilsDeListeToDo: MutableList<ProfilListeToDo>

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

        val profilActif: ProfilListeToDo = findProfilListeToDoByLogin(getProfilListeToDoList(), pseudoActif!!)!!
        val todoListActive: ListeToDo = profilActif.getMesListesToDo()?.get(selectedItem)!!

        nomPseudoActif.text = "Profil : $pseudoActif / TodoList : " + todoListActive.getTitreListeToDo()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewShowActivity)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val dropdownItems = fromListToDoToDropDownItem2(todoListActive, pseudoActif)

        val adapter = DropdownAdapter2(dropdownItems, this) // Passer l'activité en tant qu'écouteur
        recyclerView.adapter = adapter

        btnOk.setOnClickListener {
            //actions à effectuer quand on appuye sur OK
            val newTodo: String = editTextNewList.text.toString() //On récupère le pseudo qui est dans le champ

            if (newTodo != "") { //on vérifie qu'il y a bien un nom rentré
                //Toast.makeText(applicationContext, newTodo, Toast.LENGTH_SHORT).show()

                ListdeProfilsDeListeToDo = getProfilListeToDoList() //charge la liste des pseudos à partir des préférences partagées
                val profilActif = findProfilListeToDoByLogin(ListdeProfilsDeListeToDo, pseudoActif)!!
                todoListString = getToDoListAsString(todoListActive)

                if (todoListString.contains(newTodo)) {
                    Toast.makeText(applicationContext, "Cette ToDo existe déjà", Toast.LENGTH_SHORT).show()
                } else {
                    todoListActive.addItem(newTodo)
                    Log.i("PMR", "[OPENED]Contenu de todoListActive : $todoListActive")
                    Log.i("PMR", "[OPENED]Contenu de profilActif : $profilActif")

                    updateListeToDoInProfilListeToDoList(ListdeProfilsDeListeToDo, todoListActive)

                    saveProfilListeToDoList(ListdeProfilsDeListeToDo) //On sauvegarde les modifications
                }

                val dropdownItems = fromListToDoToDropDownItem2(todoListActive, pseudoActif)
                adapter.updateData(dropdownItems)
                adapter.notifyDataSetChanged()

                Log.i("PMR", "[OPENED]Contenu de ListdeProfilsDeListeToDo : $ListdeProfilsDeListeToDo")
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

    // Transformer la liste de profils en liste de leurs noms
    fun getListeToDoListAsString(listesDuProfil: ProfilListeToDo): ArrayList<String> {
        val lestdestodo: ArrayList<String> = ArrayList()
        for (liste: ListeToDo in listesDuProfil.getMesListesToDo()) {
            liste.getTitreListeToDo().let { lestdestodo.add(it) }
        }
        return lestdestodo
    }

    //fonction qui prend en paramètre un ProfilListeToDo ainsi qu'un nom de liste, et renvoie la ListeToDo qui correspond au nom
    fun findListeToDoByName(profilListeToDo: ProfilListeToDo, listeName: String): ListeToDo? {
        return profilListeToDo.getMesListesToDo().find { it.getTitreListeToDo() == listeName }
    }

    fun fromListToDoToDropDownItem2(listeToDo: ListeToDo, pseudoActif: String): List<DropdownItem2> {
        val dropdownItems = mutableListOf<DropdownItem2>()
        for ((index, item) in listeToDo.getLesItems().withIndex()) {
            dropdownItems.add(DropdownItem2(index + 1, item.getDescription(), pseudoActif))
        }
        return dropdownItems
    }

    fun getToDoListAsString(listeToDo: ListeToDo): MutableList<String> {
        val listeDeToDoString: MutableList<String> = mutableListOf()
        for (item: ItemToDo in listeToDo.getLesItems()) {
            listeDeToDoString.add(item.getDescription())
        }
        return listeDeToDoString
    }

    fun updateProfilListeToDo(profilListeToDo: ProfilListeToDo, listeDeProfilsDeListeToDo: MutableList<ProfilListeToDo>) {
        val index = listeDeProfilsDeListeToDo.indexOfFirst { it.getlogin() == profilListeToDo.getlogin() }
        if (index != -1) {
            listeDeProfilsDeListeToDo[index] = profilListeToDo
        } else {
            listeDeProfilsDeListeToDo.add(profilListeToDo)
        }
    }

    fun updateListeToDoInProfilListeToDoList(profilListeToDoList: MutableList<ProfilListeToDo>, listeToDo: ListeToDo) {
        val listeToDoName = listeToDo.getTitreListeToDo()
        for (profilListeToDo in profilListeToDoList) {
            val mesListesToDo = profilListeToDo.getMesListesToDo()
            for (i in 0 until mesListesToDo.size) {
                val currentListeToDo = mesListesToDo[i]
                if (currentListeToDo.getTitreListeToDo() == listeToDoName) {
                    mesListesToDo[i] = listeToDo
                    break
                }
            }
        }
    }

    override fun onItemChecked(item: DropdownItem2, isChecked: Boolean) {
        item.isChecked = isChecked
        val pseudoActif = intent.getStringExtra("pseudoActif")

        ListdeProfilsDeListeToDo = getProfilListeToDoList() //charge la liste des pseudos à partir des préférences partagées

        val profilActif = findProfilListeToDoByLogin(ListdeProfilsDeListeToDo, pseudoActif!!)!!

        val selectedItem = item.id - 1 // Adapter les indices
        val mesListesToDo = profilActif.getMesListesToDo()
        val todoListActive: ListeToDo? = if (mesListesToDo.size > selectedItem) mesListesToDo[selectedItem] else null

        // Récupérer l'ItemToDo correspondant
        if (todoListActive != null) {

            val items = todoListActive.getLesItems()
            if (items.isNotEmpty() && selectedItem < items.size) {
                val itemToDo = items[selectedItem]
                // Utilisez itemToDo selon vos besoins
                // Mettre à jour la propriété estFait
                itemToDo.setFait(isChecked)

                updateListeToDoInProfilListeToDoList(ListdeProfilsDeListeToDo, todoListActive)

                Log.i("PMR", "[OPENED]Contenu de itemToDo : $itemToDo")
                Log.i("PMR", "[OPENED]Contenu de todoListActive : $todoListActive")
                Log.i("PMR", "[OPENED]Contenu de ListdeProfilsDeListeToDo : $ListdeProfilsDeListeToDo")

                // Enregistrer les modifications dans la liste de profils
                saveProfilListeToDoList(ListdeProfilsDeListeToDo)
            } else {
                // Gérez le cas où le tableau est vide ou selectedItem est invalide
            }


        }
    }
}
