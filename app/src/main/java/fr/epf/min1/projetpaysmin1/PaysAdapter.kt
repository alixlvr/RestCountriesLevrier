package fr.epf.min1.projetpaysmin1


import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso


class PaysAdapter(context: Context, private val resource: Int) :
    ArrayAdapter<Pays>(context, resource) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
            val pays = getItem(position)
            view.findViewById<TextView>(R.id.nom).text = pays?.name?.common
            Picasso.get().load(pays?.flags?.png).into(view.findViewById<ImageView>(R.id.drapeau))

            val heartIcon = view.findViewById<ImageView>(R.id.heartIcon)
            heartIcon.setOnClickListener {
                toggleFavorite(pays)
                updateHeartIcon(heartIcon, isFavorite(pays))
            }
            updateHeartIcon(heartIcon, isFavorite(pays))

        return view
    }

    private fun toggleFavorite(pays: Pays?) {
        pays ?: return
        val sharedPreferences = context.getSharedPreferences("favorite_countries", Context.MODE_PRIVATE)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(Pays::class.java)
        val favorites = sharedPreferences.getStringSet("favorites", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
        val countryKey = jsonAdapter.toJson(pays)
        if (favorites.contains(countryKey)) {
            favorites.remove(countryKey)
        } else {
            favorites.add(countryKey)
        }
        sharedPreferences.edit().putStringSet("favorites", favorites).apply()
    }


    private fun isFavorite(pays: Pays?): Boolean {
        pays ?: return false
        val sharedPreferences = context.getSharedPreferences("favorites_countries", Context.MODE_PRIVATE)
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(Pays::class.java)
        val favorites = sharedPreferences.getStringSet("favorites", mutableSetOf())
        val countryKey = jsonAdapter.toJson(pays)
        return favorites?.contains(countryKey) ?: false
    }


    private fun updateHeartIcon(heartIcon: ImageView, isFavorite: Boolean) {
        if (isFavorite) {
            heartIcon.setImageResource(R.drawable.ic_favorite_red)
        } else {
            heartIcon.setImageResource(R.drawable.ic_favorite_border_red)
        }
    }


}
