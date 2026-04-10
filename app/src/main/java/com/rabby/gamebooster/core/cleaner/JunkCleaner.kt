package com.rabby.gamebooster.core.cleaner

import android.content.Context
import java.io.File

object JunkCleaner {

    fun cleanCache(context: Context): Long {
        return try {
            deleteDir(context.cacheDir)
        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }

    private fun deleteDir(dir: File?): Long {
        if (dir == null || !dir.exists()) return 0L

        var deletedBytes = 0L

        if (dir.isDirectory) {
            dir.listFiles()?.forEach { file ->
                deletedBytes += deleteDir(file)
            }
        }

        deletedBytes += dir.length()
        dir.delete()
        return deletedBytes
    }
}
