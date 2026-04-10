package com.rabby.gamebooster.presentation.ui.settings

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rabby.gamebooster.R
import com.rabby.gamebooster.core.cleaner.HiddenCacheCleaner
import com.rabby.gamebooster.core.cleaner.JunkCleaner
import com.rabby.gamebooster.core.dpi.DpiManager
import com.rabby.gamebooster.core.network.DnsManager
import com.rabby.gamebooster.core.optimizer.DynamicOptimizer
import com.rabby.gamebooster.core.shizuku.ShizukuHelper
import com.rabby.gamebooster.core.touch.TouchBooster

class SettingsActivity : AppCompatActivity() {

    private lateinit var shizukuStatusText: TextView
    private lateinit var requestShizukuButton: Button

    private lateinit var dpiInput: EditText
    private lateinit var applyDpiButton: Button
    private lateinit var resetDpiButton: Button
    private lateinit var currentDpiButton: Button

    private lateinit var cloudflareDnsButton: Button
    private lateinit var googleDnsButton: Button
    private lateinit var disableDnsButton: Button
    private lateinit var dnsStateButton: Button

    private lateinit var optimizeButton: Button
    private lateinit var junkCleanButton: Button
    private lateinit var hiddenCacheButton: Button
    private lateinit var dynamicSwitch: Switch
    private lateinit var touchSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        shizukuStatusText = findViewById(R.id.shizukuStatusText)
        requestShizukuButton = findViewById(R.id.requestShizukuButton)

        dpiInput = findViewById(R.id.dpiInput)
        applyDpiButton = findViewById(R.id.applyDpiButton)
        resetDpiButton = findViewById(R.id.resetDpiButton)
        currentDpiButton = findViewById(R.id.currentDpiButton)

        cloudflareDnsButton = findViewById(R.id.cloudflareDnsButton)
        googleDnsButton = findViewById(R.id.googleDnsButton)
        disableDnsButton = findViewById(R.id.disableDnsButton)
        dnsStateButton = findViewById(R.id.dnsStateButton)

        optimizeButton = findViewById(R.id.optimizeButton)
        junkCleanButton = findViewById(R.id.junkCleanButton)
        hiddenCacheButton = findViewById(R.id.hiddenCacheButton)
        dynamicSwitch = findViewById(R.id.dynamicSwitch)
        touchSwitch = findViewById(R.id.touchSwitch)

        refreshShizukuStatus()

        requestShizukuButton.setOnClickListener {
            if (!ShizukuHelper.isShizukuAvailable()) {
                toast("Shizuku not running. Start Shizuku first.")
                refreshShizukuStatus()
                return@setOnClickListener
            }

            ShizukuHelper.requestPermission { granted ->
                runOnUiThread {
                    refreshShizukuStatus()
                    toast(if (granted) "Shizuku permission granted" else "Shizuku permission denied")
                }
            }
        }

        applyDpiButton.setOnClickListener {
            if (!ensureShizukuReady()) return@setOnClickListener

            val dpiText = dpiInput.text.toString().trim()
            if (dpiText.isEmpty()) {
                toast("Enter DPI first")
                return@setOnClickListener
            }

            val dpi = dpiText.toIntOrNull()
            if (dpi == null) {
                toast("Invalid DPI")
                return@setOnClickListener
            }

            val result = DpiManager.applyDpi(dpi)
            toast(result.second)
        }

        resetDpiButton.setOnClickListener {
            if (!ensureShizukuReady()) return@setOnClickListener

            val result = DpiManager.resetDpi()
            toast(result.second)
        }

        currentDpiButton.setOnClickListener {
            if (!ensureShizukuReady()) return@setOnClickListener

            val result = DpiManager.readCurrentDpi()
            toast(result.second)
        }

        cloudflareDnsButton.setOnClickListener {
            if (!ensureShizukuReady()) return@setOnClickListener

            val result = DnsManager.applyCloudflareDns()
            toast(result.second)
        }

        googleDnsButton.setOnClickListener {
            if (!ensureShizukuReady()) return@setOnClickListener

            val result = DnsManager.applyGoogleDns()
            toast(result.second)
        }

        disableDnsButton.setOnClickListener {
            if (!ensureShizukuReady()) return@setOnClickListener

            val result = DnsManager.disablePrivateDns()
            toast(result.second)
        }

        dnsStateButton.setOnClickListener {
            if (!ensureShizukuReady()) return@setOnClickListener

            val result = DnsManager.readDnsState()
            toast("DNS mode: ${result.second}")
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

    override fun onResume() {
        super.onResume()
        refreshShizukuStatus()
    }

    private fun ensureShizukuReady(): Boolean {
        return when {
            !ShizukuHelper.isShizukuAvailable() -> {
                toast("Shizuku is not running")
                refreshShizukuStatus()
                false
            }
            !ShizukuHelper.isPermissionGranted() -> {
                toast("Grant Shizuku permission first")
                refreshShizukuStatus()
                false
            }
            else -> true
        }
    }

    private fun refreshShizukuStatus() {
        val status = when {
            !ShizukuHelper.isShizukuAvailable() -> "Shizuku Status: Not running"
            ShizukuHelper.isPermissionGranted() -> "Shizuku Status: Connected & permission granted"
            else -> "Shizuku Status: Connected but permission not granted"
        }
        shizukuStatusText.text = status
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
