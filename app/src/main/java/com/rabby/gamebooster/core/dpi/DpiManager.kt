package com.rabby.gamebooster.core.dpi

import com.rabby.gamebooster.core.shizuku.ShizukuHelper

object DpiManager {

    fun applyDpi(dpi: Int): Pair<Boolean, String> {
        if (dpi < 200 || dpi > 800) {
            return false to "Use DPI between 200 and 800"
        }

        return ShizukuHelper.runShellCommand("wm density $dpi")
    }

    fun resetDpi(): Pair<Boolean, String> {
        return ShizukuHelper.runShellCommand("wm density reset")
    }

    fun readCurrentDpi(): Pair<Boolean, String> {
        return ShizukuHelper.runShellCommand("wm density")
    }
}
