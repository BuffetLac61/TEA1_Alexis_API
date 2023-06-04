package com.example.tea1_v01

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

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



}