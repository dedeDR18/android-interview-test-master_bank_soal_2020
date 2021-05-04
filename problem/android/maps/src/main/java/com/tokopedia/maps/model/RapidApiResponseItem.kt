package com.tokopedia.maps.model

data class RapidApiResponseItem(
    val alpha2Code: String,
    val alpha3Code: String,
    val altSpellings: List<String>,
    val area: Int,
    val borders: List<String>,
    val callingCodes: List<String>,
    val capital: String,
    val currencies: List<String>,
    val demonym: String,
    val gini: Double,
    val languages: List<String>,
    val latlng: List<Double>,
    val name: String,
    val nativeName: String,
    val numericCode: String,
    val population: Int,
    val region: String,
    val relevance: String,
    val subregion: String,
    val timezones: List<String>,
    val topLevelDomain: List<String>,
    val translations: Translations
)