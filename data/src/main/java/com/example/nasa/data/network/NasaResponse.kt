package com.example.nasa.data.network

import com.google.gson.annotations.SerializedName



internal data class Collection(
    @SerializedName("href")
    val href: String,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("metadata")
    val metadata: Metadata,
    @SerializedName("version")
    val version: String
)

internal data class Item(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("href")
    val href: String,
    @SerializedName("links")
    val links: List<Link>
)

internal data class Metadata(
    @SerializedName("total_hits")
    val totalHits: Int
)

internal data class Data(
    @SerializedName("center")
    val center: String,
    @SerializedName("date_created")
    val dateCreated: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("keywords")
    val keywords: List<String>,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("nasa_id")
    val nasaId: String,
    @SerializedName("photographer")
    val photographer: String,
    @SerializedName("title")
    val title: String
)

internal data class Link(
    @SerializedName("href")
    val href: String,
    @SerializedName("rel")
    val rel: String,
    @SerializedName("render")
    val render: String
)