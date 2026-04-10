package com.rabby.gamebooster.core.booster

import android.app.ActivityManager
import android.content.Context

object AppKiller {

    fun killBackgroundApps(context: Context) {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val processes = am.runningAppProcesses
        processes?.forEach {
            it.pkgList.forEach { pkg ->
                am.killBackgroundProcesses(pkg)
            }
        }
    }
}
