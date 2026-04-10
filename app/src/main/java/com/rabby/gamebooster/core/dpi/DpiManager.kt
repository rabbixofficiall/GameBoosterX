package com.rabby.gamebooster.core.dpi

import android.content.Context
import java.io.DataOutputStream

object DpiManager {

    fun changeDpiWithShizukuOrRoot(context: Context, dpi: Int): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("sh")
            val outputStream = DataOutputStream(process.outputStream)

            outputStream.writeBytes("wm density $dpi\n")
            outputStream.writeBytes("exit\n")
            outputStream.flush()

            process.waitFor() == 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun resetDpi(context: Context): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("sh")
            val outputStream = DataOutputStream(process.outputStream)

            outputStream.writeBytes("wm density reset\n")
            outputStream.writeBytes("exit\n")
            outputStream.flush()

            process.waitFor() == 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
