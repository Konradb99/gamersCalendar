package com.konradb.gameCalendar.main_app.Model.DataBaseEntities

data class GameDetails (
    var id: Long = 0,
    var name: String? = null,
    var released: String? = null,
    var background_image: String? = null,
){}