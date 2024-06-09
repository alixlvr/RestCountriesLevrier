# RestCountriesLevrier

Alix Levrier

**Documentation utilisateur :**

Application Recherche de Pays
10/06/2024
Cachan

1. **Introduction**

**ProjetPaysMIN1** est une application Android qui permet aux utilisateurs de rechercher des informations sur les pays, consulter les détails de chaque pays et de les ajouter à leurs favoris, que l’utilisateur peut consulter hors ligne.

2. **Fonctionnalités implémentées**

- Recherche des pays par nom ou par capitale
- Affichage la fiche des détails d'un pays
- Ajout de pays aux favoris de l’utilisateur, qui sont disponible hors ligne. Possibilité de consulter la fiche des pays en favoris hors ligne.
- Navigation entre la page de recherche et les favoris grâce à une barre de navigation en bas de l’écran
- Jeux des drapeaux, pour tester ces connaissances sur les drapeaux des différents pays du monde

3. **Réalisation des fonctionnalités implémentées**
A. **Recherche de pays par nom ou par capitale**

**Description :** L'utilisateur peut rechercher des pays soit par leur nom soit par leur capitale en utilisant un champ de recherche et des boutons radio pour sélectionner le type de recherche.

**Implémentation :**

- **UI** : Champ de recherche (EditText), boutons radio (RadioButton) et bouton de recherche (Button).
- **API** : Utilisation de Retrofit pour appeler l'API REST Countries.
- **Code** :

val request: Call&lt;List<Pays&gt;>

if (nameRadioButton.isChecked) {

request = paysAPI.getCountriesByName(searchQuery)

}

else if (capitalRadioButton.isChecked) {

request = paysAPI.getCountriesByCapital(searchQuery)

}

request.enqueue(object : Callback&lt;List<Pays&gt;> {

override fun onResponse(call: Call&lt;List<Pays&gt;>, response: Response&lt;List<Pays&gt;>) { // Gérer la réponse }

override fun onFailure(call: Call&lt;List<Pays&gt;>, t: Throwable) { // Gérer l'échec }

})

B. **Affichage des Détails d'un Pays**

**Description :** L'utilisateur peut cliquer sur un pays dans la liste pour voir plus de détails sur ce pays. Dans cette fiche l’utilisateur peut consulter le nom commun, le nom officiel, la capital, la region, la sous-region, la/les langue(s), le nombre d’habitants, la monnaie utilisé et la superficie du pays en question.

**Implémentation :**

- **UI** : ListView pour afficher les pays et une nouvelle activité pour les détails (Paysdetails).
- **Code** :

paysList.setOnItemClickListener { \_, \_, position, _ ->

val selectedCountry = adapter.getItem(position)

if (selectedCountry != null) {

val intent = Intent(this, Paysdetails::class.java).apply {

putExtra("selected_country", selectedCountry)

}

startActivity(intent)

}

}

C. **Ajout de Pays aux Favoris**

**Description :** L'utilisateur peut ajouter des pays à ses favoris en cliquant sur une icône de cœur.

**Implémentation :**

- **UI** : Icône de cœur dans chaque élément de la liste (ImageView).
- **Stockage** : Utilisation des SharedPreferences pour stocker les favoris.
- **Code** :

val heartIcon = view.findViewById&lt;ImageView&gt;(R.id.heartIcon)

heartIcon.setOnClickListener {

toggleFavorite(pays)

updateHeartIcon(heartIcon, isFavorite(pays))

}

D. **Consultation de la Liste des Favoris**

**Description :** L'utilisateur peut voir tous les pays ajoutés à ses favoris dans une liste dédiée et consulter la fiche de ce pays hors connexion.

**Implémentation :**

- **UI** : ListView pour afficher les favoris.
- **Code** :

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

}

E. **Navigation entre la Page de Recherche et les Favoris**

**Description :** L'utilisateur peut naviguer entre la page du jeu des drapeaux (icone peu visible en bas à gauche), la page de recherche et la page des favoris à l'aide d'une barre de navigation.

**Implémentation :**

- **UI** : BottomNavigationView pour la navigation.
- **Code** :

bottomNavigationView.setOnNavigationItemSelectedListener { item ->

when (item.itemId) {

R.id.GameItem -> {

startActivity(Intent(this, GamePays::class.java))

true

}

R.id.searchItem -> {

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

F. **Jeux des drapeaux**

**Description** : Le jeu des drapeaux permet aux utilisateurs de tester leurs connaissances sur les drapeaux des différents pays du monde. Cependant, cette fonctionnalité est encore en cours de développement et ne fonctionne pas correctement.

**Implémentation prévue :**

- Afficher aléatoirement un drapeau à partir de l'API des pays.
- Proposer quatre options de noms de pays, parmi lesquels l'utilisateur doit sélectionner le nom du pays correspondant au drapeau affiché.
- Donner un retour à l'utilisateur sur la correction de sa réponse.

**État actuel** : Le jeu des drapeaux est en cours de développement et n'est pas fonctionnel. Des travaux supplémentaires sont nécessaires pour résoudre les problèmes de chargement des drapeaux. En effet, je n’arrive pas à télécharger le drapeau dans l’emplacement prévu et je ne sais pas si cela est dû à une API lente ou à un dysfonctionnement dans mon code.

**Code** :

private fun setupGame(countries: List&lt;Pays&gt;) {

val randomCountries = countries.shuffled().take(4)

val correctCountry = randomCountries.random()

correctCountryName = correctCountry.name.common

Picasso.get().load(correctCountry.flags.png).into(flagGameImageView)

optionButtons.forEachIndexed { index, button ->

button.text = randomCountries\[index\].name.common

button.setOnClickListener {

checkAnswer(button.text.toString())

}

}

}
