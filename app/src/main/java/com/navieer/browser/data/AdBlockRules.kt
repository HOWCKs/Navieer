package com.navieer.browser.data

import android.net.Uri

object AdBlockRules {
    private val blockedDomains = hashSetOf(
        "doubleclick.net",
        "google-analytics.com",
        "googlesyndication.com",
        "adservice.google.com",
        "adnxs.com",
        "criteo.com",
        "taboola.com",
        "outbrain.com",
        "adroll.com",
        "scorecardresearch.com",
        "quantserve.com",
        "hotjar.com",
        "pubmatic.com",
        "rubiconproject.com",
        "popads.net",
        "trafficjunky.com",
        "propellerads.com",
        "mgid.com",
        "admob.com",
        "appsflyer.com",
        "adjust.com",
        "branch.io",
        "facebook.net/en_US/fbevents.js",
        "ads.twitter.com",
        "analytics.twitter.com"
    )

    fun isAdOrTracker(url: String): Boolean {
        return try {
            val host = Uri.parse(url).host?.lowercase() ?: return false
            blockedDomains.any { host == it || host.endsWith(".$it") }
        } catch (e: Exception) {
            false
        }
    }
}
