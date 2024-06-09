package fr.epf.min1.projetpaysmin1
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


class FavoriteList : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var moshi: Moshi
    private lateinit var jsonAdapter: JsonAdapter<Pays>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite_liste)

        sharedPreferences = getSharedPreferences("favorite_countries", Context.MODE_PRIVATE)

        moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        jsonAdapter = moshi.adapter(Pays::class.java)

        val favoritesListView = findViewById<ListView>(R.id.favoritesListView)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.GameItem -> {
                    val intent = Intent(this, GamePays::class.java)
                    startActivity(intent)
                    true
                }
                R.id.searchItem -> {
                    // Ouvrir page recherche de pays
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.favoritesItem -> {
                    true
                }
                else -> false
            }
        }
        loadFavoriteCountries(favoritesListView)
    }

    private fun loadFavoriteCountries(listView: ListView) {
        val favorites = sharedPreferences.getStringSet("favorites", mutableSetOf())?.toList() ?: listOf()
        val favoriteCountries = favorites.mapNotNull { json ->
            try {
                jsonAdapter.fromJson(json)
            } catch (e: Exception) {
                null
            }
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, favoriteCountries.map { it.name.common })
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedCountry = favoriteCountries[position]
            val intent = Intent(this, Paysdetails::class.java).apply {
                putExtra("selected_country", selectedCountry)
            }
            startActivity(intent)
        }
    }

}