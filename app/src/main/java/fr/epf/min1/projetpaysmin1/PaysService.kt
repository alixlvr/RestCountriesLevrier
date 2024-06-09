package fr.epf.min1.projetpaysmin1

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface PaysService {
    @GET("name/{name}")
    fun getCountriesByName(@Path("name") name: String): Call<List<Pays>>

    @GET("capital/{capital}")
    fun getCountriesByCapital(@Path("capital") capital: String): Call<List<Pays>>

    @GET("all")
    fun getAllCountries(): Call<List<Pays>>
}

@Parcelize
data class Pays(
    val name: Name,
    val capital: List<String>?,
    val flags: Flags,
    val region: String,
    val subregion: String?,
    val population: Long,
    val languages: Map<String, String>?,
    val currencies: Map<String, Currency>?,
    val area: Double?,
) : Parcelable

@Parcelize
data class Name(
    val common: String,
    val official: String
) : Parcelable

@Parcelize
data class Flags(
    val png: String
) : Parcelable

@Parcelize
data class Currency(
    val name: String,
    val symbol: String?
) : Parcelable








