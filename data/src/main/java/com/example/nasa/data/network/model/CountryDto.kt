package com.example.nasa.data.network.model

import com.example.nasa.data.network.Name
import com.google.gson.annotations.SerializedName

data class CountryDto(
    @SerializedName("name")
    val name: Name,

    @SerializedName("latlng")
    val latlng: List<Double>,
)