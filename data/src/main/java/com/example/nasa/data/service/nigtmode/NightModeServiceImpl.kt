package com.example.nasa.data.service.nigtmode

import android.content.Context
import com.example.nasa.domain.model.NightMode
import com.example.nasa.domain.service.NightModeService

class NightModeServiceImpl(context: Context) : NightModeService {

    private val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override var nightMode: NightMode by enumPref(KEY_NIGHT_MODE, NightMode.SYSTEM)

    private inline fun <reified E : Enum<E>> enumPref(key: String, defaultValue: E) =
        PrefsDelegate(
            sharedPrefs,
            getValue = { getString(key, null)?.let(::enumValueOf) ?: defaultValue },
            setValue = { putString(key, it.name) }
        )

    companion object {
        private const val PREFS_NAME = "prefs_name"

        private const val KEY_NIGHT_MODE = "night_mode"
    }
}