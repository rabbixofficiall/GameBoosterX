package com.rabby.gamebooster.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.rabby.gamebooster.data.model.GameModel

class HomeViewModel : ViewModel() {

    fun getGames(): List<GameModel> {
        return listOf(
            GameModel("Free Fire", "com.dts.freefireth"),
            GameModel("PUBG Mobile", "com.tencent.ig")
        )
    }
}
