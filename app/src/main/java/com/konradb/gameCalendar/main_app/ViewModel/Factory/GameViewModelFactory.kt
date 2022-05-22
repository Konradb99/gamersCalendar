package com.konradb.gameCalendar.main_app.ViewModel.Factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.konradb.gameCalendar.main_app.ViewModel.GameViewModel
import java.lang.IllegalArgumentException

class GameViewModelFactory(private val application: Application): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameViewModel::class.java)){
            return GameViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}