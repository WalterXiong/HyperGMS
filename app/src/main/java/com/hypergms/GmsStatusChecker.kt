package com.hypergms

import android.content.Context
import android.content.pm.PackageManager

/**
 * Detects whether Google Mobile Services (GMS) is enabled on the device.
 *
 * On Xiaomi HyperOS, the enabled state of `com.google.android.gms`
 * reflects whether Google services are available on the device.
 */
object GmsStatusChecker {

    private const val GMS_PACKAGE = "com.google.android.gms"

    sealed class Status {
        /** GMS is enabled and available. */
        data object Enabled : Status()

        /** GMS package exists but is explicitly disabled by the user/system. */
        data object Disabled : Status()

        /** GMS package is not installed on the device at all. */
        data object NotInstalled : Status()

        /** An unexpected error occurred while checking GMS status. */
        data class Error(val message: String) : Status()
    }

    /**
     * Checks the current GMS status on the device.
     *
     * Uses [PackageManager.getApplicationEnabledSetting] to read the enabled state
     * of the Google Play Services package. This is the same mechanism Xiaomi uses
     * to control the "Google基础服务" toggle.
     */
    fun check(context: Context): Status {
        return try {
            val state = context.packageManager.getApplicationEnabledSetting(GMS_PACKAGE)
            when (state) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> Status.Enabled

                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT -> {
                    // Default state: check if the package actually exists and is enabled
                    resolveDefaultState(context)
                }

                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED -> Status.Disabled

                else -> Status.Error("Unknown enabled state: $state")
            }
        } catch (e: IllegalArgumentException) {
            // Package not found on device
            Status.NotInstalled
        } catch (e: Exception) {
            Status.Error(e.message ?: "Unexpected error")
        }
    }

    private fun resolveDefaultState(context: Context): Status {
        return try {
            val appInfo = context.packageManager.getApplicationInfo(GMS_PACKAGE, 0)
            if (appInfo.enabled) Status.Enabled else Status.Disabled
        } catch (e: PackageManager.NameNotFoundException) {
            Status.NotInstalled
        }
    }
}
