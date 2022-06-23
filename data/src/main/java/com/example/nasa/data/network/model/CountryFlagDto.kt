package com.example.nasa.data.network.model

import com.example.nasa.data.network.Flags
import com.example.nasa.data.network.Name
import com.google.gson.annotations.SerializedName

data class CountryFlagDto(
    @SerializedName("name")
    val name: Name,

    @SerializedName("flags")
    val flags: Flags,
)