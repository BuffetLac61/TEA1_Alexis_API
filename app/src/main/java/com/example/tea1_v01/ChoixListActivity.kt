package com.example.tea1_v01

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tea1_v01.ListeToDo

class ChoixListActivity : BaseActivity() {

    private lateinit var nomPseudoActif: TextView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        toolbar = findViewById(R.id.toolbarParametres)
        setSupportActionBar(toolbar)

        val intent = intent
        val pseudoActif = intent.getStringExtra("pseudoActif")

        nomPseudoActif = findViewById(R.id.nomProfilActif)
        nomPseudoActif.text = pseudoActif



        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewChoixActivity)
        recyclerView.layoutManager = LinearLayoutManager(this)


        var profilListeToDo1 = ajouterListe_test(pseudoActif)

        val dropdownItems = fromListToDoToDropDownItem(profilListeToDo1)

        val adapter = DropdownAdapter(dropdownItems)
        recyclerView.adapter = adapter


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

    fun fromListToDoToDropDownItem(profilListeToDo1: ProfilListeToDo) : List<DropdownItem>{
        val i = 1
        val dropdownItems = mutableListOf<DropdownItem>()
        for(listei in profilListeToDo1.getMesListesToDo()){
            dropdownItems.add(DropdownItem(i,listei.getTitreListeToDo()))
        }
        return(dropdownItems)
    }

    fun ajouterListe_test(monLogin: String?) : ProfilListeToDo{
        // Création d'une instance de ProfilListeToDo
        val profil = ProfilListeToDo(monLogin)

        // Création d'une liste de tâches
        var liste = ListeToDo()
        liste.setTitreListeToDo("Ma liste de tâches")

        //creation des tâches

        var item1 = ItemToDo("Faire la vaisselle")
        var item2 = ItemToDo("Faire la vaisselle encore")
        var item3 = ItemToDo("reFaire la vaisselle")
        var items = arrayOf(item1, item2, item3)
        liste.setLesItems(items)

        // Ajout de la liste à l'instance de ProfilListeToDo
        profil.ajouterListe(liste)

        liste = ListeToDo()
        liste.setTitreListeToDo("Mon autre liste de tâches")

        //creation des tâches
        item1 = ItemToDo("Faire la vaisselle")
        item2 = ItemToDo("Faire la vaisselle encore")
        item3 = ItemToDo("reFaire la vaisselle")
        items = arrayOf(item1, item2, item3)

        liste.setLesItems(items)

        // Ajout de la liste à l'instance de ProfilListeToDo
        profil.ajouterListe(liste)

        //On marque un item comme fait
        val itemModified = profil.getMesListesToDo()[0].rechercherItem("Faire la vaisselle")

        if (itemModified != null) {
            itemModified.setFait(true)
        }

        // Vérification que la liste a été ajoutée avec succès
        println( profil.toString())

        return(profil)

    }

}