package com.example.tea1_v01

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

abstract class BaseActivity : AppCompatActivity() {
    // Implémentation commune pour toutes les activités héritant de BaseActivity

    // Récupérer le hash depuis les préférences partagées
    fun getHashFromSharedPref(): String {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("hashCode")){
            val gson = Gson()
            val json = sharedPreferences.getString("hashCode", "")
            val type = object : TypeToken<String>() {}.type
            return (gson.fromJson(json, type) )
        }
        else return ""
    }

    fun saveListsToSharedPref(lists:String) {
        Log.i("Volley","[saveListsToSharedPref] enregistrement des listes dans les prefs partagées...")
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        Log.i("Volley","[saveListsToSharedPref] Contenu de lists : $lists")
        val json = gson.toJson(lists)
        val editor = sharedPreferences.edit()
        editor.putString("lists", json)
        editor.apply()
    }

    fun apiCallSetList(hash: String, newTodoList: String){

    }
}

