package com.rabby.gamebooster.data.repository

import android.content.Context
import com.rabby.gamebooster.data.model.GameModel
import com.rabby.gamebooster.domain.scanner.GameScanner

class GameRepository(private val context: Context) {

    fun getInstalledGames(): List<GameModel> {
        return GameScanner.scanInstalledGames(context)
    }
}
