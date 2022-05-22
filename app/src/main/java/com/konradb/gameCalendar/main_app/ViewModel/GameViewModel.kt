package com.konradb.gameCalendar.main_app.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.konradb.gameCalendar.main_app.Model.DataBaseEntities.Game

class GameViewModel(application: Application): AndroidViewModel(application) {
    var gameId: Long
    var game: Game
    init{
        gameId = 0L
        game = Game()
    }
}