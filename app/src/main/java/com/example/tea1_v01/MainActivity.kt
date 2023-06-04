package com.example.tea1_v01

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar


class MainActivity : BaseActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var editTextPseudo: EditText
    private lateinit var buttonOk: Button
    private var pseudoList: ArrayList<String> = ArrayList()
    private lateinit var currentPseudo :String

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbarParametres)
        setSupportActionBar(toolbar)


        editTextPseudo = findViewById(R.id.editTextPseudo)
        buttonOk = findViewById(R.id.buttonOk)

        buttonOk.setOnClickListener {
            //actions à effectuer quand on appuye sur OK
            val pseudo = editTextPseudo.text.toString() //On récupère le pseudo qui est dans le champ

            if(pseudo!="") { //on vérifie qu'il y ai bien un nom rentré

                loadPseudoListFromSharedPreferences()//charge la liste des pseudos à pertir des préférences partagées

                if (pseudoList.contains(pseudo)) {
                    currentPseudo = pseudo
                } else {
                    pseudoList.add(pseudo) //on ajoute le pseudo dans la liste
                    currentPseudo = pseudo
                }

                //editTextPseudo.clearComposingText() //on efface le pseudo ? (ne fonctionne pas)

                // Faire quelque chose avec le pseudo, par exemple l'afficher dans le Logcat
                Log.d("MainActivity", "Pseudo: $pseudo")

                savePseudoListToSharedPreferences() //on enregistre la liste de pseudo
                Log.d("PMR", "Contenu de pseudoList : ${pseudoList.toString()}")//on l'affiche

                editTextPseudo.hint = pseudo

                //puis ouvrir l'activité ChoixListActivity
                val intent = Intent(this, ChoixListActivity::class.java)
                Log.i("PMR", "[OPENED]ChoixListActivity")

                startActivity(intent)
            } //si l'utilisateur ne rentre pas de pseudo

            else Toast.makeText(applicationContext, "Veuillez rentrer un pseudo", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //création de la toolbar à partir de menu_main.xml
        menuInflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Action à effectuer lorsque le bouton des paramètres est cliqué
                val intent = Intent(this, SettingsActivity::class.java)
                Log.i("PMR", "[OPENED]SettingsActivity")
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun savePseudoListToSharedPreferences() {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val pseudoSet = HashSet(pseudoList)
        editor.putStringSet("pseudoList", pseudoSet)
        editor.apply()
    }

    private fun loadPseudoListFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val pseudoSet = sharedPreferences.getStringSet("pseudoList", HashSet<String>())
        pseudoList.clear()
        if (pseudoSet != null) {
            pseudoList.addAll(pseudoSet)
        }
    }


}

