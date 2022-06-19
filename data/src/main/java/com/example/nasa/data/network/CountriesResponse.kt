package com.example.nasa.data.network

import com.google.gson.annotations.SerializedName


data class CountriesResponseItem(
    @SerializedName("altSpellings")
    val altSpellings: List<String>,
    @SerializedName("area")
    val area: Double,
    @SerializedName("borders")
    val borders: List<String>,
    @SerializedName("capital")
    val capital: List<String>,
    @SerializedName("capitalInfo")
    val capitalInfo: CapitalInfo,
    @SerializedName("car")
    val car: Car,
    @SerializedName("cca2")
    val cca2: String,
    @SerializedName("cca3")
    val cca3: String,
    @SerializedName("ccn3")
    val ccn3: String,
    @SerializedName("cioc")
    val cioc: String,
    @SerializedName("coatOfArms")
    val coatOfArms: CoatOfArms,
    @SerializedName("continents")
    val continents: List<String>,
    @SerializedName("currencies")
    val currencies: Currencies,
    @SerializedName("demonyms")
    val demonyms: Demonyms,
    @SerializedName("fifa")
    val fifa: String,
    @SerializedName("flag")
    val flag: String,
    @SerializedName("flags")
    val flags: Flags,
    @SerializedName("gini")
    val gini: Gini,
    @SerializedName("idd")
    val idd: Idd,
    @SerializedName("independent")
    val independent: Boolean,
    @SerializedName("landlocked")
    val landlocked: Boolean,
    @SerializedName("languages")
    val languages: Languages,
    @SerializedName("latlng")
    val latlng: List<Double>,
    @SerializedName("maps")
    val maps: Maps,
    @SerializedName("name")
    val name: Name,
    @SerializedName("population")
    val population: Int,
    @SerializedName("postalCode")
    val postalCode: PostalCode,
    @SerializedName("region")
    val region: String,
    @SerializedName("startOfWeek")
    val startOfWeek: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("subregion")
    val subregion: String,
    @SerializedName("timezones")
    val timezones: List<String>,
    @SerializedName("tld")
    val tld: List<String>,
    @SerializedName("translations")
    val translations: Translations,
    @SerializedName("unMember")
    val unMember: Boolean
)

data class CapitalInfo(
    @SerializedName("latlng")
    val latlng: List<Double>
)

data class Car(
    @SerializedName("side")
    val side: String,
    @SerializedName("signs")
    val signs: List<String>
)

data class CoatOfArms(
    @SerializedName("png")
    val png: String,
    @SerializedName("svg")
    val svg: String
)

data class Currencies(
    @SerializedName("USD")
    val uSD: USD
)

data class Demonyms(
    @SerializedName("eng")
    val eng: Eng,
    @SerializedName("fra")
    val fra: Fra
)

data class Flags(
    @SerializedName("png")
    val png: String,
    @SerializedName("svg")
    val svg: String
)

data class Gini(
    @SerializedName("2013")
    val x2013: Double
)

data class Idd(
    @SerializedName("root")
    val root: String,
    @SerializedName("suffixes")
    val suffixes: List<String>
)

data class Languages(
    @SerializedName("eng")
    val eng: String
)

data class Maps(
    @SerializedName("googleMaps")
    val googleMaps: String,
    @SerializedName("openStreetMaps")
    val openStreetMaps: String
)

data class Name(
    @SerializedName("common")
    val common: String,
    @SerializedName("nativeName")
    val nativeName: NativeName,
    @SerializedName("official")
    val official: String
)

data class PostalCode(
    @SerializedName("format")
    val format: String,
    @SerializedName("regex")
    val regex: String
)

data class Translations(
    @SerializedName("ara")
    val ara: Ara,
    @SerializedName("ces")
    val ces: Ces,
    @SerializedName("cym")
    val cym: Cym,
    @SerializedName("deu")
    val deu: Deu,
    @SerializedName("est")
    val est: Est,
    @SerializedName("fin")
    val fin: Fin,
    @SerializedName("fra")
    val fra: FraX,
    @SerializedName("hrv")
    val hrv: Hrv,
    @SerializedName("hun")
    val hun: Hun,
    @SerializedName("ita")
    val ita: Ita,
    @SerializedName("jpn")
    val jpn: Jpn,
    @SerializedName("kor")
    val kor: Kor,
    @SerializedName("nld")
    val nld: Nld,
    @SerializedName("per")
    val per: Per,
    @SerializedName("pol")
    val pol: Pol,
    @SerializedName("por")
    val por: Por,
    @SerializedName("rus")
    val rus: Rus,
    @SerializedName("slk")
    val slk: Slk,
    @SerializedName("spa")
    val spa: Spa,
    @SerializedName("swe")
    val swe: Swe,
    @SerializedName("urd")
    val urd: Urd,
    @SerializedName("zho")
    val zho: Zho
)

data class USD(
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String
)

data class Eng(
    @SerializedName("f")
    val f: String,
    @SerializedName("m")
    val m: String
)

data class Fra(
    @SerializedName("f")
    val f: String,
    @SerializedName("m")
    val m: String
)

data class NativeName(
    @SerializedName("eng")
    val eng: EngX
)

data class EngX(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Ara(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Ces(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Cym(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Deu(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Est(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Fin(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class FraX(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Hrv(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Hun(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Ita(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Jpn(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Kor(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Nld(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Per(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Pol(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Por(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Rus(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Slk(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Spa(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Swe(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Urd(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

data class Zho(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)