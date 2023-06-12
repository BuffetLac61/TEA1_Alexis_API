package com.example.tea1_v01

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.auth.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.JsonObject
import org.json.JSONObject

import okhttp3.*
import java.io.IOException



class MainActivity : BaseActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var editTextPseudo: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonOk: Button
    private var pseudoList: ArrayList<String> = ArrayList()
    private var pseudoList2: ArrayList<String> = ArrayList()

    private lateinit var currentPseudo :String
    private lateinit var ListdeProfilsDeListeToDo : MutableList<ProfilListeToDo>

    private lateinit var IndicatoOffline: TextView
    private lateinit var IndicatoOnline: TextView

    var hash = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbarParametres)
        setSupportActionBar(toolbar)


        var url = "http://tomnab.fr/todo-api/authenticate?user=tom&password=web"
        firstApiCall(url,object : ApiResponseCallback {
            override fun onSuccess(response: JSONObject) {
                // Gérez la réponse réussie ici
                hash = response["hash"].toString()
                saveHashToSharedPref(hash)
            }

            override fun onError(error: VolleyError) {
                // Gérez l'erreur ici
            }
        })

        editTextPseudo = findViewById(R.id.editTextPseudo)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonOk = findViewById(R.id.buttonOk)

        //Gestion de l'indice d'accès à internet
        IndicatoOnline = findViewById(R.id.IndicatoOnline)
        IndicatoOnline.visibility = View.INVISIBLE
        IndicatoOffline = findViewById(R.id.IndicatoOffline)


        if(isNetworkAvailable()){
            buttonOk.visibility = View.VISIBLE
            buttonOk.isEnabled = true
            Log.i("PMR","[NETWORK] connection sucessfull")
            IndicatoOffline.visibility = View.INVISIBLE
            IndicatoOnline.visibility = View.VISIBLE


        }
        else {
            buttonOk.visibility = View.GONE
        }

        makeGetRequest()

        buttonOk.setOnClickListener {v ->
            //actions à effectuer quand on appuye sur OK
            val pseudo = editTextPseudo.text.toString() //On récupère le pseudo qui est dans le champ
            val password = editTextPassword.text.toString()



            if(pseudo!="" && password!="") {

                //on vérifie qu'il y ait bien un nom et un mot de passe rentré

                /*loadPseudoListFromSharedPreferences()//charge la liste des pseudos à pertir des préférences partagées
                ListdeProfilsDeListeToDo = getProfilListeToDoList() //charge la liste des profils
                pseudoList2 = getProfilListeToDoListAsString(ListdeProfilsDeListeToDo)

                if (pseudoList2.contains(pseudo)) {
                    currentPseudo = pseudo

                } else {
                    pseudoList2.add(pseudo) //on ajoute le pseudo dans la liste
                    currentPseudo = pseudo
                    val profil = ProfilListeToDo(pseudo)
                    (ListdeProfilsDeListeToDo).add(profil)
                }

                //editTextPseudo.clearComposingText() //on efface le pseudo ? (ne fonctionne pas)

                // Faire quelque chose avec le pseudo, par exemple l'afficher dans le Logcat
                Log.d("MainActivity", "Pseudo: $pseudo")

                savePseudoListToSharedPreferences() //on enregistre la liste de pseudo
                saveProfilListeToDoList(ListdeProfilsDeListeToDo)
                Log.d("PMR", "Contenu de pseudoList : ${pseudoList.toString()}")//on l'affiche
                Log.d("PMR", "Contenu de pseudoList2 : ${pseudoList2.toString()}")//on l'affiche


                editTextPseudo.hint = pseudo

                */



                //puis ouvrir l'activité ChoixListActivity
                //val intent = Intent(this, ChoixListActivity::class.java)
                //intent.putExtra("pseudoActif", pseudo)
                //Log.i("PMR", "[OPENED]ChoixListActivity")

                //startActivity(intent)




                Log.i("Volley","jjjjj"+hash)

                val url = "http://tomnab.fr/todo-api/lists?hash=$hash"
                val hashValue = hash

                val request = object : StringRequest(
                    Method.GET, url,
                    Response.Listener{ response ->
                        Log.i("Volley","success mdr")
                        // Traitement de la réponse ici
                    },
                    Response.ErrorListener { error ->
                        val errorMessage = error?.networkResponse?.data?.toString(Charsets.UTF_8) ?: "Erreur inconnue"
                        Log.i("Volley", "Erreur lors de la requête API : $errorMessage")
                        // Gestion des erreurs ici
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["hash"] = hashValue
                        return headers
                    }
                }

                val queue = Volley.newRequestQueue(v.context)
                queue.add(request)
            } //si l'utilisateur ne rentre pas de pseudo
            else Toast.makeText(applicationContext, "Veuillez rentrer un pseudo et un mot de passe", Toast.LENGTH_SHORT).show()
        }

    }
    private fun makeGetRequest() {
        val url = "http://tomnab.fr/todo-api/user"
        val hashValue = "a4d36a05d87c70f299e927f47e602fbe"

        val request = object : StringRequest(
            Method.GET, url,
            Response.Listener<String> { response ->
                // Traitement de la réponse ici
            },
            Response.ErrorListener { error ->
                val errorMessage = error?.message ?: "Erreur inconnue"
                Log.i("Volley", "Erreur lors de la requête API : $errorMessage")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["hash"] = hashValue
                return headers
            }
        }

        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }


     /////////
    // TEA2 //
    /////////

     /////////
    // TEA2 //
    /////////



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

    // Enregistrer une liste de ProfilListeToDo dans les préférences partagées
    fun saveProfilListeToDoList(profilListeToDoList: MutableList<ProfilListeToDo>) {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(profilListeToDoList)
        val editor = sharedPreferences.edit()
        editor.putString("profilListeToDoList", json)
        editor.apply()
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
    fun getProfilListeToDoListAsString(ListeDesProfils:List<ProfilListeToDo>) : ArrayList<String>{
        var ListePseudo : ArrayList<String> = ArrayList()
        for(profil : ProfilListeToDo in ListeDesProfils) {
            profil.getlogin()?.let { ListePseudo.add(it) }
        }
        return(ListePseudo)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)
                ?: return false

            actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            @Suppress("DEPRECATION")
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }

    /*fun apiCall(url: String, callback: ApiResponseCallback) {
        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, null,
            { response ->
                Log.i("Volley", "ça marche ! Réponse : " + response.toString())
                callback.onSuccess(response)
            },
            { error ->
                Log.i("Volley", error.toString())
                callback.onError(error)
            }
        )


        requestQueue.add(jsonObjectRequest)
    }*/

    fun firstApiCall(url: String, callback: ApiResponseCallback) {
        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, null,
            { response ->
                Log.i("Volley", "Premier appel : ça marche ! Réponse : " + response.toString())
                callback.onSuccess(response)
            },
            { error ->
                Log.i("Volley", error.toString())
                callback.onError(error)
            }
        )
        requestQueue.add(jsonObjectRequest)
    }

    fun apiCall(url: String, hash: String,method:String="GET", callback: ApiResponseCallback) {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET, url, null,
            { response ->
                Log.i("Volley", "Premier appel : ça marche ! Réponse : " + response.toString())
                callback.onSuccess(response)
            },
            { error ->
                Log.i("Volley", error.toString())
                callback.onError(error)
            }){}}

    /*{
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["hash"] = hash

            Log.i("volley","Contenu du header :"+ headers)
            return headers
        }
    }*/


    interface ApiResponseCallback {
        fun onSuccess(response: JSONObject)
        fun onError(error: VolleyError)
    }

    //Sauvegarder le hash dans les preferences partagées
    fun saveHashToSharedPref(hash:String) {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(hash)
        val editor = sharedPreferences.edit()
        editor.putString("hashCode", json)
        editor.apply()
    }

    // Récupérer une liste de ProfilListeToDo depuis les préférences partagées
    fun getHashToSharedPref(): String {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("hashCode", "")
        val type = object : TypeToken<String>() {}.type
        return (gson.fromJson(json, type) )
    }




}

