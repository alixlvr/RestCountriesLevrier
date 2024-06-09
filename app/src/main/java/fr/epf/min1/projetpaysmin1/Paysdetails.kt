package fr.epf.min1.projetpaysmin1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso

class Paysdetails : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pays_fiche)



        val flagImageView = findViewById<ImageView>(R.id.flagImageView)
        val nameCommonTextView = findViewById<TextView>(R.id.nameCommonTextView)
        val nameOfficielTextView = findViewById<TextView>(R.id.nameOfficielTextView)
        val capitalTextView = findViewById<TextView>(R.id.capitalTextView)
        val regionTextView = findViewById<TextView>(R.id.regionTextView)
        val subregionTextView = findViewById<TextView>(R.id.subregionTextView)
        val languageTextView = findViewById<TextView>(R.id.languageTextView)
        val populationTextView = findViewById<TextView>(R.id.populationTextView)
        val monnaieTextView = findViewById<TextView>(R.id.monnaieTextView)
        val areaTextView = findViewById<TextView>(R.id.areaTextView)

        // récupérer les données du pays sélectionné
        val country = intent.getParcelableExtra<Pays>("selected_country")!!


        // afficher le drapeau
        Picasso.get().load(country.flags.png).into(flagImageView)

        // afficher les infos du pays
        nameCommonTextView.text = country.name.common
        nameOfficielTextView.text = "Nom Officiel : ${country.name.official}"
        capitalTextView.text = "Capital : ${country.capital?.joinToString() ?: "N/A"}"
        regionTextView.text = "Région : ${country.region}"
        subregionTextView.text = "Sous région : ${country.subregion ?: "N/A"}"
        languageTextView.text =
            "Langue(s): ${country.languages?.values?.joinToString() ?: "N/A"}"
        populationTextView.text = "Population : ${country.population} habitants"
        monnaieTextView.text =
            "Monnaie : ${country.currencies?.values?.joinToString { it.name } ?: "N/A"}"
        areaTextView.text = "Superficie : ${country.area?.toString() ?: "N/A"} km²"

        //barre de naviguation entre la page recherche et favoris
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.GameItem -> {
                    val intent = Intent(this, GamePays::class.java)
                    startActivity(intent)
                    true
                }
                R.id.searchItem -> {
                    // page principale (recherche)
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.favoritesItem -> {
                    //page de spays favoris
                    val intent = Intent(this, FavoriteList::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }


    private fun updateHeartIcon(heartIcon: ImageView, isFavorite: Boolean) {
        if (isFavorite) {
            heartIcon.setImageResource(R.drawable.ic_favorite_red)
        } else {
            heartIcon.setImageResource(R.drawable.ic_favorite_border_red)
        }
    }
}
