package com.konradb.gameCalendar.main_app.Model

import com.google.gson.annotations.SerializedName

class AllGames {
    @SerializedName("count")
    var count: Long? = 0
    @SerializedName("next")
    var next: String? = null
    @SerializedName("previous")
    var previous: String? = null
    @SerializedName("results")
    var result = ArrayList<Results>()
}

class Results {
    @SerializedName("id")
    var id: Long? = 0
    @SerializedName("slug")
    var slug: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("released")
    var released: String? = null
    @SerializedName("background_image")
    var background_image: String? = null
    @SerializedName("rating")
    var rating: Double? = 0.0
}