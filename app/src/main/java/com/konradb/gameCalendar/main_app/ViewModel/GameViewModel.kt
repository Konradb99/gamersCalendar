package com.konradb.gameCalendar.main_app.ViewModel

import android.app.Application
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import com.konradb.gameCalendar.R
import com.konradb.gameCalendar.main_app.Model.DataBaseEntities.Game

class GameViewModel(application: Application): AndroidViewModel(application) {
    var gameId: Long
    var game: Game
    var gameName: String?
    var gameReleaseDate: String?
    var gameRating: String?
    var gameMetacritic: String?
    var gameGenres: String?
    var gameImage: String?
    init{
        gameId = 0L
        game = Game()
        gameName = null
        gameReleaseDate = null
        gameRating = null
        gameMetacritic = null
        gameGenres = null
        gameImage = null
    }
}