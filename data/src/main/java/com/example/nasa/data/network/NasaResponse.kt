package com.example.nasa.data.network

import com.google.gson.annotations.SerializedName

internal data class Collection(
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("metadata")
    val metadata: Metadata,
)

internal data class Item(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("links")
    val links: List<Link>
)

internal data class Metadata(
    @SerializedName("total_hits")
    val totalHits: Int
)

internal data class Data(
    @SerializedName("description")
    val description: String,
    @SerializedName("nasa_id")
    val nasaId: String,
    @SerializedName("title")
    val title: String
)

internal data class Link(
    @SerializedName("href")
    val href: String,
)