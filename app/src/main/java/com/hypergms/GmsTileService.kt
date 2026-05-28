package com.hypergms

import android.app.PendingIntent
import android.content.Intent
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

/**
 * Quick Settings tile that reflects Google Mobile Services (GMS) status
 * and provides one-tap access to the GMS settings page on Xiaomi HyperOS.
 *
 * ## Behavior
 * - **Tile active (highlighted)**: GMS is enabled
 * - **Tile inactive (dimmed)**: GMS is disabled
 * - **Tile unavailable**: GMS is not installed or status check failed
 * - **Tap**: Opens GMS settings page (GmsCoreSettings on HyperOS)
 *
 * ## Lifecycle
 * Uses non-active mode: tile state is refreshed only when the Quick Settings
 * panel is visible ([onStartListening]). This avoids unnecessary background
 * polling since GMS status rarely changes while the panel is closed.
 */
class GmsTileService : TileService() {

    override fun onTileAdded() {
        super.onTileAdded()
        updateTileState()
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onClick() {
        super.onClick()

        val intent = GmsSettingsLauncher.createIntent(this)
        val pendingIntent = PendingIntent.getActivity(
            this,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        startActivityAndCollapse(pendingIntent)
    }

    override fun onStopListening() {
        super.onStopListening()
        // Non-active mode: no cleanup required between listening windows.
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        // No persistent resources to clean up.
    }

    // ── private ──────────────────────────────────────────────────────────

    private fun updateTileState() {
        val tile = qsTile ?: return

        when (val status = GmsStatusChecker.check(this)) {
            is GmsStatusChecker.Status.Enabled -> {
                tile.state = Tile.STATE_ACTIVE
                tile.label = getString(R.string.tile_label_on)
            }
            is GmsStatusChecker.Status.Disabled -> {
                tile.state = Tile.STATE_INACTIVE
                tile.label = getString(R.string.tile_label_off)
            }
            is GmsStatusChecker.Status.NotInstalled -> {
                tile.state = Tile.STATE_UNAVAILABLE
                tile.label = getString(R.string.tile_label_unavailable)
            }
            is GmsStatusChecker.Status.Error -> {
                tile.state = Tile.STATE_UNAVAILABLE
                tile.label = getString(R.string.tile_label_error)
            }
        }
        tile.updateTile()
    }

    companion object {
        private const val REQUEST_CODE = 0
    }
}
