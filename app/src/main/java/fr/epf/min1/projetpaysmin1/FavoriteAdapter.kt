package fr.epf.min1.projetpaysmin1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class FavoriteAdapter(context: Context, private val resource: Int, private val favorites: List<Pays>) :
    ArrayAdapter<Pays>(context, resource, favorites) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val pays = getItem(position)
        view.findViewById<TextView>(R.id.nom).text = pays?.name?.common
        Picasso.get().load(pays?.flags?.png).into(view.findViewById<ImageView>(R.id.flagImageView))
        return view
    }
}