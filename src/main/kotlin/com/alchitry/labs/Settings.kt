package com.alchitry.labs

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
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

    private class WindowStateSetting(private val prefix: String, private val default: WindowState) {
        private var width by FloatSetting("${prefix}_WINDOW_WIDTH", default.size.width.value)
        private var height by FloatSetting("${prefix}_WINDOW_HEIGHT", default.size.height.value)
        private var x by FloatSetting("${prefix}_WINDOW_X", default.position.x.value)
        private var y by FloatSetting("${prefix}_WINDOW_Y", default.position.x.value)
        private var positionDefined by BooleanSetting("${prefix}_WINDOW_POS_SET", default.position.isSpecified)
        private var maximized by BooleanSetting(
            "${prefix}_WINDOW_MAXIMIZED",
            default.placement == WindowPlacement.Maximized
        )

        operator fun getValue(thisRef: Any?, property: KProperty<*>): WindowState {
            return WindowState(
                placement = if (maximized) WindowPlacement.Maximized else WindowPlacement.Floating,
                position = if (positionDefined) WindowPosition.Absolute(
                    x = x.dp,
                    y = y.dp
                ) else WindowPosition.PlatformDefault,
                size = DpSize(width.dp, height.dp)
            )
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: WindowState) {
            width = value.size.width.value
            height = value.size.height.value
            x = value.position.x.value
            y = value.position.y.value
            positionDefined = value.position.isSpecified
            maximized = value.placement == WindowPlacement.Maximized
        }
    }


    fun commit() {
        try {
            pref.flush()
        } catch (e: BackingStoreException) {
            // TODO: Util.showError("Failed to save settings!")
        }
    }

    var version by StringSetting("VERSION", "0")
    var labsWindowState by WindowStateSetting("LABS", WindowState(size = DpSize(1000.dp, 700.dp)))
    var loaderWindowState by WindowStateSetting("LOADER", WindowState(size = DpSize(500.dp, 200.dp)))
    var fileListWidth by IntSetting("FILE_LIST_WIDTH", 200)
    var consoleHeight by IntSetting("CONSOLE_HEIGHT", 200)
    var openProject by StringSetting("OPEN_PROJECT", null)
    var workspace by StringSetting("WORKSPACE", null)
    var darkTheme by BooleanSetting("DARK_THEME", true)
    var wordWrap by BooleanSetting("WORD_WRAP", true)

    var vivadoLocation by StringSetting("VIVADO_LOC", null)
    var iceCubeLocation by StringSetting("ICECUBE_LOC", null)
    var iceCubeLicense by StringSetting("ICECUBE_LICENSE", null)
    var useIceCube by BooleanSetting("USE_ICECUBE", true)

    var fontScale by FloatSetting("FONT_SCALE", 1.0f)
    var errorReporting by BooleanSetting("ERROR_REPORTING", false)
    var errorReportingPrompted by BooleanSetting("ERROR_REPORTING_PROMPTED", false)
    var loaderBinFile by StringSetting("LOADER_BIN_FILE", null)
}