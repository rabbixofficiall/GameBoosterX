package com.rabby.gamebooster.presentation.ui.settings

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rabby.gamebooster.R
import com.rabby.gamebooster.core.cleaner.HiddenCacheCleaner
import com.rabby.gamebooster.core.cleaner.JunkCleaner
import com.rabby.gamebooster.core.dpi.DpiManager
import com.rabby.gamebooster.core.network.DnsManager
import com.rabby.gamebooster.core.optimizer.DynamicOptimizer
import com.rabby.gamebooster.core.touch.TouchBooster

class SettingsActivity : AppCompatActivity() {

    private lateinit var dpiInput: EditText
    private lateinit var applyDpiButton: Button
    private lateinit var resetDpiButton: Button
    private lateinit var cloudflareDnsButton: Button
    private lateinit var googleDnsButton: Button
    private lateinit var disableDnsButton: Button
    private lateinit var optimizeButton: Button
    private lateinit var junkCleanButton: Button
    private lateinit var hiddenCacheButton: Button
    private lateinit var dynamicSwitch: Switch
    private lateinit var touchSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        dpiInput = findViewById(R.id.dpiInput)
        applyDpiButton = findViewById(R.id.applyDpiButton)
        resetDpiButton = findViewById(R.id.resetDpiButton)
        cloudflareDnsButton = findViewById(R.id.cloudflareDnsButton)
        googleDnsButton = findViewById(R.id.googleDnsButton)
        disableDnsButton = findViewById(R.id.disableDnsButton)
        optimizeButton = findViewById(R.id.optimizeButton)
        junkCleanButton = findViewById(R.id.junkCleanButton)
        hiddenCacheButton = findViewById(R.id.hiddenCacheButton)
        dynamicSwitch = findViewById(R.id.dynamicSwitch)
        touchSwitch = findViewById(R.id.touchSwitch)

        applyDpiButton.setOnClickListener {
            val dpiText = dpiInput.text.toString().trim()
            if (dpiText.isEmpty()) {
                toast("Enter DPI first")
                return@setOnClickListener
            }

            val dpi = dpiText.toIntOrNull()
            if (dpi == null || dpi < 200 || dpi > 800) {
                toast("Use a valid DPI between 200 and 800")
                return@setOnClickListener
            }

            val success = DpiManager.changeDpiWithShizukuOrRoot(this, dpi)
            toast(if (success) "DPI changed to $dpi" else "DPI change failed")
        }

        resetDpiButton.setOnClickListener {
            val success = DpiManager.resetDpi(this)
            toast(if (success) "DPI reset done" else "DPI reset failed")
        }

        cloudflareDnsButton.setOnClickListener {
            val success = DnsManager.setPrivateDns(this, "1dot1dot1dot1.cloudflare-dns.com")
            toast(if (success) "Cloudflare DNS applied" else "DNS apply failed")
        }

        googleDnsButton.setOnClickListener {
            val success = DnsManager.setPrivateDns(this, "dns.google")
            toast(if (success) "Google DNS applied" else "DNS apply failed")
        }

        disableDnsButton.setOnClickListener {
            val success = DnsManager.disablePrivateDns(this)
            toast(if (success) "Private DNS disabled" else "Disable failed")
        }

        optimizeButton.setOnClickListener {
            val result = DynamicOptimizer.optimize(this)
            toast(result)
        }

        junkCleanButton.setOnClickListener {
            val bytes = JunkCleaner.cleanCache(this)
            toast("Junk cleaned: ${bytes / 1024} KB")
        }

        hiddenCacheButton.setOnClickListener {
            val hiddenCount = HiddenCacheCleaner.scanHiddenFiles(this)
            toast("Hidden cache files found: $hiddenCount")
        }

        dynamicSwitch.setOnCheckedChangeListener { _, isChecked ->
            toast(if (isChecked) "Dynamic Optimization ON" else "Dynamic Optimization OFF")
        }

        touchSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                TouchBooster.enable()
                toast("Touch Boost ON")
            } else {
                TouchBooster.disable()
                toast("Touch Boost OFF")
            }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
