package com.rabby.gamebooster.core.network

import android.content.Context
import android.provider.Settings

object DnsManager {

    fun setPrivateDns(context: Context, provider: String): Boolean {
        return try {
            Settings.Global.putString(
                context.contentResolver,
                "private_dns_mode",
                "hostname"
            )
            Settings.Global.putString(
                context.contentResolver,
                "private_dns_specifier",
                provider
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun disablePrivateDns(context: Context): Boolean {
        return try {
            Settings.Global.putString(
                context.contentResolver,
                "private_dns_mode",
                "off"
            )
            Settings.Global.putString(
                context.contentResolver,
                "private_dns_specifier",
                ""
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
