package com.alchitry.labs

import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences
import kotlin.reflect.KProperty

object Settings {
    private val pref = Preferences.userNodeForPackage(Settings::class.java)

    private class StringSetting(private val key: String, private val default: String?) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String? = pref.get(key, default)
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) =
            if (value != null) pref.put(key, value) else pref.remove(key)
    }

    private class BooleanSetting(private val key: String, private val default: Boolean) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean = pref.getBoolean(key, default)
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) = pref.putBoolean(key, value)
    }

    private class IntSetting(private val key: String, private val default: Int) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Int = pref.getInt(key, default)
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) = pref.putInt(key, value)
    }

    private class FloatSetting(private val key: String, private val default: Float) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): Float = pref.getFloat(key, default)
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) = pref.putFloat(key, value)
    }


    fun commit() {
        try {
            pref.flush()
        } catch (e: BackingStoreException) {
            // TODO: Util.showError("Failed to save settings!")
        }
    }

    var version by StringSetting("VERSION", "0")
    var windowWidth by IntSetting("WINDOW_WIDTH", 1000)
    var windowHeight by IntSetting("WINDOW_HEIGHT", 700)
    var fileListWidth by IntSetting("FILE_LIST_WIDTH", 200)
    var consoleHeight by IntSetting("CONSOLE_HEIGHT", 200)
    var maximized by BooleanSetting("MAXIMIZED", false)
    var xilinxLoc by StringSetting("XILINX_LOC", null)
    var openProject by StringSetting("OPEN_PROJECT", null)
    var workspace by StringSetting("WORKSPACE", null)
    var theme by BooleanSetting("THEME", false)
    var wordWrap by BooleanSetting("WORD_WRAP", true)
    var vivadoLoc by StringSetting("VIVADO_LOC", null)
    var icecubeLoc by StringSetting("ICECUBE_LOC", null)
    var icecubeLicense by StringSetting("ICECUBE_LICENSE", null)
    var useIcestorm by BooleanSetting("USE_ICESTORM", false)
    var icepackLoc by StringSetting("ICEPACK_LOC", null)
    var yosysLoc by StringSetting("YOSYS_LOC", null)
    var aracheLoc by StringSetting("ARACHNE_LOC", null)
    var fontScale by FloatSetting("FONT_SCALE", 1.0f)
    var checkForUpdates by BooleanSetting("CHECK_FOR_UPDATES", true)
    var betaUpdates by BooleanSetting("BETA_UPDATES", false)
    var betaUpdatesPrompted by BooleanSetting("BETA_UPDATES_PROMPTED", false)
    var errorReporting by BooleanSetting("ERROR_REPORTING", false)
    var errorReportingPrompted by BooleanSetting("ERROR_REPORTING_PROMPTED", false)
}