package com.rabby.gamebooster.core.booster

import android.app.ActivityManager
import android.content.Context

object RamCleaner {

    fun clean(context: Context) {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.clearApplicationUserData()
    }
}
