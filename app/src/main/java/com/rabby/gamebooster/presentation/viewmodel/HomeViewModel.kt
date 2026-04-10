package com.rabby.gamebooster.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.rabby.gamebooster.data.model.GameModel
import com.rabby.gamebooster.data.repository.GameRepository

class HomeViewModel : ViewModel() {

    fun getGames(context: Context): List<GameModel> {
        return GameRepository(context).getInstalledGames()
    }
}
