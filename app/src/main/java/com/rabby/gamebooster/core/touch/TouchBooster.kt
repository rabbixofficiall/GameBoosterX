package com.rabby.gamebooster.core.touch

object TouchBooster {

    private var enabled = false

    fun enable(): Boolean {
        enabled = true
        return enabled
    }

    fun disable(): Boolean {
        enabled = false
        return !enabled
    }

    fun isEnabled(): Boolean {
        return enabled
    }
}
