package com.rabby.gamebooster.domain.usecase

import android.content.Context
import com.rabby.gamebooster.core.booster.RamCleaner
import com.rabby.gamebooster.core.booster.AppKiller

class BoostGameUseCase {

    fun execute(context: Context) {
        RamCleaner.clean(context)
        AppKiller.killBackgroundApps(context)
    }
}
