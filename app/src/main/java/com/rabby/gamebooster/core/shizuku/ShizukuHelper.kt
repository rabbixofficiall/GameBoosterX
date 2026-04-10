package com.rabby.gamebooster.core.shizuku

import android.content.pm.PackageManager
import rikka.shizuku.Shizuku

object ShizukuHelper {

    private const val REQUEST_CODE_SHIZUKU = 1001

    fun isShizukuAvailable(): Boolean {
        return try {
            Shizuku.pingBinder()
        } catch (e: Throwable) {
            false
        }
    }

    fun isPermissionGranted(): Boolean {
        return isShizukuAvailable() &&
                Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(onResult: (Boolean) -> Unit) {
        if (!isShizukuAvailable()) {
            onResult(false)
            return
        }

        if (isPermissionGranted()) {
            onResult(true)
            return
        }

        val listener = object : Shizuku.OnRequestPermissionResultListener {
            override fun onRequestPermissionResult(requestCode: Int, grantResult: Int) {
                if (requestCode == REQUEST_CODE_SHIZUKU) {
                    Shizuku.removeRequestPermissionResultListener(this)
                    onResult(grantResult == PackageManager.PERMISSION_GRANTED)
                }
            }
        }

        Shizuku.addRequestPermissionResultListener(listener)
        Shizuku.requestPermission(REQUEST_CODE_SHIZUKU)
    }

    fun runShellCommand(command: String): Pair<Boolean, String> {
        if (!isPermissionGranted()) {
            return false to "Shizuku permission not granted"
        }

        return try {
            val process = Shizuku.newProcess(
                arrayOf("sh", "-c", command),
                null,
                null
            )

            val stdout = process.inputStream.bufferedReader().use { it.readText() }
            val stderr = process.errorStream.bufferedReader().use { it.readText() }
            val exitCode = process.waitFor()

            process.destroy()

            if (exitCode == 0) {
                true to if (stdout.isBlank()) "Success" else stdout.trim()
            } else {
                false to if (stderr.isBlank()) "Command failed" else stderr.trim()
            }
        } catch (e: Exception) {
            false to (e.message ?: "Unknown Shizuku error")
        }
    }
}
