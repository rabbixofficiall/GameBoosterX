package com.rabby.gamebooster.core.optimizer

import android.app.ActivityManager
import android.content.Context

object DynamicOptimizer {

    fun optimize(context: Context): String {
        return try {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

            val memoryInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)

            val availableMb = memoryInfo.availMem / (1024 * 1024)

            when {
                availableMb < 800 -> "Low RAM detected. Aggressive optimization applied."
                availableMb < 1500 -> "Medium RAM state. Balanced optimization applied."
                else -> "Good RAM state. Light optimization applied."
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Optimization failed."
        }
    }
}
