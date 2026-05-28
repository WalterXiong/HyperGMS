# ProGuard rules for HyperGMS
# Keep the TileService - it's declared in manifest and called by SystemUI
-keep class com.hypergms.GmsTileService { *; }
-keep class com.hypergms.GmsStatusChecker { *; }
-keep class com.hypergms.GmsSettingsLauncher { *; }
