package com.hypergms

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings

object GmsSettingsLauncher {

    fun createIntent(context: Context): Intent {
        // Strategy 1: Xiaomi Google Basic Services settings page.
        // Verified working on HyperOS 3: com.miui.securitycenter/com.miui.googlebase.ui.GmsCoreSettings
        val gmsCoreIntent = buildComponentIntent(
            "com.miui.securitycenter",
            "com.miui.googlebase.ui.GmsCoreSettings"
        )
        if (gmsCoreIntent != null && canResolve(context, gmsCoreIntent)) {
            return gmsCoreIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        // Strategy 2: Account sync settings.
        val syncIntent = Intent(Settings.ACTION_SYNC_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (canResolve(context, syncIntent)) return syncIntent

        // Strategy 3: Main Settings.
        return Intent(Settings.ACTION_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun buildComponentIntent(pkg: String, cls: String): Intent? {
        return try {
            Intent().setComponent(ComponentName(pkg, cls))
        } catch (e: Exception) {
            null
        }
    }

    private fun canResolve(context: Context, intent: Intent): Boolean {
        val flags = PackageManager.ResolveInfoFlags.of(0L)
        return context.packageManager.resolveActivity(intent, flags) != null
    }
}
