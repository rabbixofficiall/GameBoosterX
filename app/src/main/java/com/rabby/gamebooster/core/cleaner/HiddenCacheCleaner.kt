package com.rabby.gamebooster.core.cleaner

import android.content.Context
import java.io.File

object HiddenCacheCleaner {

    fun scanHiddenFiles(context: Context): Int {
        return try {
            val root = context.cacheDir.parentFile ?: return 0
            countHiddenFiles(root)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    private fun countHiddenFiles(dir: File?): Int {
        if (dir == null || !dir.exists()) return 0

        var count = 0
        dir.listFiles()?.forEach { file ->
            if (file.name.startsWith(".")) count++
            if (file.isDirectory) count += countHiddenFiles(file)
        }
        return count
    }
}
