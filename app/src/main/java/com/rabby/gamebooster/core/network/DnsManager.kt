package com.rabby.gamebooster.core.network

import com.rabby.gamebooster.core.shizuku.ShizukuHelper

object DnsManager {

    fun applyCloudflareDns(): Pair<Boolean, String> {
        val mode = ShizukuHelper.runShellCommand("settings put global private_dns_mode hostname")
        if (!mode.first) return mode

        val host = ShizukuHelper.runShellCommand(
            "settings put global private_dns_specifier 1dot1dot1dot1.cloudflare-dns.com"
        )
        if (!host.first) return host

        return true to "Cloudflare DNS applied"
    }

    fun applyGoogleDns(): Pair<Boolean, String> {
        val mode = ShizukuHelper.runShellCommand("settings put global private_dns_mode hostname")
        if (!mode.first) return mode

        val host = ShizukuHelper.runShellCommand(
            "settings put global private_dns_specifier dns.google"
        )
        if (!host.first) return host

        return true to "Google DNS applied"
    }

    fun disablePrivateDns(): Pair<Boolean, String> {
        val mode = ShizukuHelper.runShellCommand("settings put global private_dns_mode off")
        if (!mode.first) return mode

        val host = ShizukuHelper.runShellCommand("settings put global private_dns_specifier ''")
        if (!host.first) return host

        return true to "Private DNS disabled"
    }

    fun readDnsState(): Pair<Boolean, String> {
        return ShizukuHelper.runShellCommand("settings get global private_dns_mode")
    }
}
