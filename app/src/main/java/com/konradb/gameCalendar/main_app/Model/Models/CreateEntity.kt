package com.konradb.gameCalendar.main_app.Model.Models

import androidx.activity.result.contract.ActivityResultContracts
import com.konradb.gameCalendar.main_app.Model.DataBaseEntities.Game
import com.konradb.gameCalendar.main_app.Model.Results


class CreateEntity {
    companion object{
        fun CreateGame(game: Results): Game {
            var CreatedGame: Game = Game(game.name, game.released, game.background_image)
            return CreatedGame
        }
    }
}