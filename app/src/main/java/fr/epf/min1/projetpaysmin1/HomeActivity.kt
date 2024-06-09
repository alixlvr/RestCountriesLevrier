package fr.epf.min1.projetpaysmin1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity() {

    private lateinit var paysAPI: PaysService
    private lateinit var paysList: ListView
    private lateinit var adapter: PaysAdapter
    private lateinit var nameRadioButton: RadioButton
    private lateinit var capitalRadioButton: RadioButton
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://restcountries.com/v3.1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()

        paysAPI = retrofit.create(PaysService::class.java)
        Log.d("HomeActivity", "Retrofit initialized")

        val searchButton = findViewById<Button>(R.id.searchButton)
        val searchQueryEditText = findViewById<EditText>(R.id.searchQuery)
        nameRadioButton = findViewById(R.id.nameRadioButton)
        capitalRadioButton = findViewById(R.id.capitalRadioButton)
        paysList = findViewById(R.id.countryList)

        // Initialiser l'adaptateur
        adapter = PaysAdapter(this, R.layout.pays_detail)
        paysList.adapter = adapter

        searchButton.setOnClickListener {
            val searchQuery = searchQueryEditText.text.toString()
            if (searchQuery.isEmpty()) {
                Toast.makeText(this, "Veuillez saisir votre recherche", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            getCountries(searchQuery)
        }

        paysList.setOnItemClickListener { _, _, position, _ ->
            val selectedCountry = adapter.getItem(position)
            if (selectedCountry != null) {
                showCountryDetails(selectedCountry)
            }
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.GameItem -> {
                    startActivity(Intent(this, GamePays::class.java))
                    true
                }
                R.id.searchItem -> true
                R.id.favoritesItem -> {
                    startActivity(Intent(this, FavoriteList::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun getCountries(searchQuery: String) {
        val request: Call<List<Pays>>
        if (nameRadioButton.isChecked) {
            request = paysAPI.getCountriesByName(searchQuery)
        } else if (capitalRadioButton.isChecked) {
            request = paysAPI.getCountriesByCapital(searchQuery)
        } else {
            Toast.makeText(this, "Veuillez sélectionner un type de recherche", Toast.LENGTH_SHORT).show()
            return
        }
        request.enqueue(object : Callback<List<Pays>> {
            override fun onResponse(call: Call<List<Pays>>, response: Response<List<Pays>>) {
                if (response.isSuccessful) {
                    val countries = response.body()
                    if (countries != null) {
                        adapter = PaysAdapter(this@HomeActivity, R.layout.pays_detail)
                        adapter.addAll(countries)
                        paysList.adapter = adapter
                    } else {
                        Toast.makeText(this@HomeActivity, "Pas de pays trouvé", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Erreur de réponse", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<List<Pays>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Impossible d'avoir les données...", Toast.LENGTH_SHORT).show()
                Log.e("HomeActivity", "Erreur : ${t.message}", t)
            }
        })
    }

    private fun showCountryDetails(country: Pays) {
        val intent = Intent(this, Paysdetails::class.java).apply{
            putExtra("selected_country", country)
        }
        startActivity(intent)
    }
}
