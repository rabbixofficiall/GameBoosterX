package com.rabby.gamebooster.domain.scanner

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import com.rabby.gamebooster.data.model.GameModel
import java.util.Locale

object GameScanner {

    fun scanInstalledGames(context: Context): List<GameModel> {
        val packageManager = context.packageManager

        val installedApps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getInstalledApplications(
                PackageManager.ApplicationInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.getInstalledApplications(0)
        }

        return installedApps
            .asSequence()
            .filter { app ->
                isLaunchableApp(packageManager, app.packageName) &&
                        !isSystemApp(app) &&
                        isProbablyGame(packageManager, app)
            }
            .map { app ->
                val appName = packageManager.getApplicationLabel(app).toString()
                GameModel(
                    name = appName,
                    packageName = app.packageName
                )
            }
            .distinctBy { it.packageName }
            .sortedBy { it.name.lowercase(Locale.getDefault()) }
            .toList()
    }

    private fun isLaunchableApp(
        packageManager: PackageManager,
        packageName: String
    ): Boolean {
        return packageManager.getLaunchIntentForPackage(packageName) != null
    }

    private fun isSystemApp(app: ApplicationInfo): Boolean {
        return (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0
    }

    private fun isProbablyGame(
        packageManager: PackageManager,
        app: ApplicationInfo
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (app.category == ApplicationInfo.CATEGORY_GAME) return true
        }

        val packageName = app.packageName.lowercase(Locale.getDefault())
        val appName = packageManager.getApplicationLabel(app)
            .toString()
            .lowercase(Locale.getDefault())

        val keywords = listOf(
            "game", "gaming", "free fire", "pubg", "bgmi", "cod",
            "call of duty", "mobile legends", "mlbb", "arena breakout",
            "clash", "roblox", "minecraft", "asphalt", "efootball",
            "pes", "fifa", "coc", "brawl", "genshin"
        )

        val text = "$packageName $appName"
        return keywords.any { keyword -> text.contains(keyword) } ||
                hasGameLauncherCategory(packageManager, app.packageName)
    }

    private fun hasGameLauncherCategory(
        packageManager: PackageManager,
        packageName: String
    ): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
                addCategory(Intent.CATEGORY_GAME)
                `package` = packageName
            }

            val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.queryIntentActivities(
                    intent,
                    PackageManager.ResolveInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                packageManager.queryIntentActivities(intent, 0)
            }

            result.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }
}
