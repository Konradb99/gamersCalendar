package com.konradb.gameCalendar.main_app.Model.FireBase

data class GameData(
    var id: String? = null,
    var name: String? = null,
    var released: String? = null,
    var rating: String? = null,
    var metacritic: Int = 0,
    var background_image: String? = null,
    var genres: String? = null,
    var status: String? = null
){}