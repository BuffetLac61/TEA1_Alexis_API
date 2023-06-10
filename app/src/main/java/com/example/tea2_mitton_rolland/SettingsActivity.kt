package com.example.tea1_v01

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager


class SettingsActivity : PreferenceActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //On affiche le xml root_preferences dans le conteneur qui a comme id "setting"
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, MyPreferencesFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    class MyPreferencesFragment : PreferenceFragmentCompat() {

        private lateinit var sharedPreferences: SharedPreferences

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

            val apiUrlPreference = findPreference<EditTextPreference>("apiUrlInput")
            apiUrlPreference?.setOnPreferenceChangeListener { preference, newValue ->
                // Vérifier si la clé correspond à notre EditTextPreference
                if (preference.key == "apiUrlInput") {
                    // Vérifier si l'utilisateur a appuyé sur le bouton "OK"
                    if (newValue is String) {
                        Toast.makeText(context, "Mise à jour de l'URL de l'API", Toast.LENGTH_SHORT).show()
                        sharedPreferences.edit().putString("apiUrl", newValue).apply()
                    }
                }
                true
            }

        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean {
            when (preference.key) {

                "deletePseudoList" -> {

                    createDialogueConfirmationDeletePseudo()

                    return true
                }
            }
            return super.onPreferenceTreeClick(preference)
        }



        fun createDialogueConfirmationDeletePseudo() {
            // Action à effectuer lorsque la préférence est sélectionnée
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Confirmation")
            builder.setMessage("Voulez-vous continuer ?")
            builder.setPositiveButton("Oui") { _, _ ->
                // Action à effectuer lorsque l'utilisateur clique sur le bouton "Oui"
                // par exemple, effectuer une suppression ou une autre opération

                val sharedPreferences = requireActivity().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()
                Toast.makeText(context, "Pseudo list deleted", Toast.LENGTH_SHORT).show()
                requireActivity().finish()

            }

            builder.setNegativeButton("Non") { _, _ ->
                // Action à effectuer lorsque l'utilisateur clique sur le bouton "Non"
                // par exemple, annuler l'opération en cours
            }
            val alertDialog = builder.create()
            alertDialog.show()

        }



    }



}

