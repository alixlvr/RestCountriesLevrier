package fr.epf.min1.projetpaysmin1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class GamePays : AppCompatActivity() {

    private lateinit var flagGameImageView: ImageView
    private lateinit var optionButtons: List<Button>
    private lateinit var resultGameTextView: TextView
    private lateinit var paysAPI: PaysService
    private var correctCountryName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_pays)

        flagGameImageView = findViewById(R.id.flagGameImageView)
        optionButtons = listOf(
            findViewById(R.id.option1NameCountryButton),
            findViewById(R.id.option2NameCountryButton),
            findViewById(R.id.option3NameCountryButton),
            findViewById(R.id.option4NameCountryButton)
        )
        resultGameTextView = findViewById(R.id.resultGameTextView)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.GameItem -> {
                    true
                }
                R.id.searchItem -> {
                    // Ouvrir page recherche de pays
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.favoritesItem -> {
                    startActivity(Intent(this, FavoriteList::class.java))
                    true
                }
                else -> false
            }
        }
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

        fetchRandomCountries()
    }

    private fun fetchRandomCountries() {
        paysAPI.getAllCountries().enqueue(object : Callback<List<Pays>> {
            override fun onResponse(call: Call<List<Pays>>, response: Response<List<Pays>>) {
                if (response.isSuccessful) {
                    val countries = response.body()
                    if (countries != null) {
                        setupGame(countries)
                    }
                } else {
                    Toast.makeText(this@GamePays, "Erreur de réponse", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Pays>>, t: Throwable) {
                Toast.makeText(this@GamePays, "Erreur : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupGame(countries: List<Pays>) {
        val randomCountries = countries.shuffled().take(4)
        val correctCountry = randomCountries.random()

        correctCountryName = correctCountry.name.common

        Picasso.get().load(correctCountry.flags.png).into(flagGameImageView)

        optionButtons.forEachIndexed { index, button ->
            button.text = randomCountries[index].name.common
            button.setOnClickListener {
                checkAnswer(button.text.toString())
            }
        }
    }

    private fun checkAnswer(selectedCountryName: String) {
        if (selectedCountryName == correctCountryName) {
            resultGameTextView.text = "Correct!"
        } else {
            resultGameTextView.text = "Incorrect. Correct answer: $correctCountryName"
        }
        fetchRandomCountries()
    }


}