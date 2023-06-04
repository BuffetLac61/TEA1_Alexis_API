package com.example.tea1_v01

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var editTextPseudo: EditText
    private lateinit var buttonOk: Button
    private val pseudoList: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbarParametres)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            //setDisplayHomeAsUpEnabled(true)
            //setHomeAsUpIndicator(R.drawable.settings_account_box_fill0_wght400_grad0_opsz48)
        }

        editTextPseudo = findViewById(R.id.editTextPseudo)
        buttonOk = findViewById(R.id.buttonOk)

        buttonOk.setOnClickListener {
            val pseudo = editTextPseudo.text.toString()
            pseudoList.add(pseudo)
            editTextPseudo.clearComposingText()
            // Faire quelque chose avec le pseudo, par exemple l'afficher dans le Logcat
            Log.d("MainActivity", "Pseudo: $pseudo")
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
                val intent = Intent(this, SettingsActivity::class.java)
                Log.i("PMR", "[OPENED]SettingsActivity")
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}