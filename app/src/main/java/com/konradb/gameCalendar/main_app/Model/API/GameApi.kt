package com.konradb.gameCalendar.main_app.Model.API

import com.google.gson.annotations.SerializedName

class GameApi {
    @SerializedName("id")
    var id: Long? = 0L
    @SerializedName("name")
    var name: String? = null
    @SerializedName("released")
    var released: String? = null
    @SerializedName("rating")
    var rating: String? = null
    @SerializedName("metacritic")
    var metacritic: Int = 0
    @SerializedName("background_image")
    var background_image: String? = null
    @SerializedName("genres")
    var genres = ArrayList<Genres>()
}

class Genres{
    @SerializedName("id")
    var id: Long? = 0L
    @SerializedName("name")
    var name: String? = null
}
