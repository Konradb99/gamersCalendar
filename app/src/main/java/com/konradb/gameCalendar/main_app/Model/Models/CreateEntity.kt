package com.konradb.gameCalendar.main_app.Model.Models

import androidx.activity.result.contract.ActivityResultContracts
import com.konradb.gameCalendar.main_app.Model.API.Results
import com.konradb.gameCalendar.main_app.Model.DataBaseEntities.Game


class CreateEntity {
    companion object{
        fun CreateGame(game: Results): Game {
            var CreatedGame: Game = Game(game.id, game.name, game.released, game.background_image)
            return CreatedGame
        }
    }
}