package com.example.nasa.data.service.prefs

import android.content.Context
import com.example.nasa.domain.model.NightMode
import com.example.nasa.domain.model.SearchParams
import com.example.nasa.domain.service.NightModeService
import com.example.nasa.domain.service.SearchParamsService
import com.example.nasa.domain.util.DEFAULT_END_YEAR
import com.example.nasa.domain.util.DEFAULT_SEARCH_QUERY
import com.example.nasa.domain.util.DEFAULT_START_YEAR

class PrefsServiceImpl(context: Context) : NightModeService, SearchParamsService {

    private val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override var nightMode: NightMode by enumPref(KEY_NIGHT_MODE, NightMode.SYSTEM)

    override var searchParams: SearchParams by PrefsDelegate(
        sharedPrefs = sharedPrefs,
        getValue = {
            val query = getString(KEY_SEARCH_QUERY, null) ?: DEFAULT_SEARCH_QUERY
            val startYear = getInt(KEY_START_YEAR, DEFAULT_START_YEAR)
            val endYear = getInt(KEY_END_YEAR, DEFAULT_END_YEAR)

            SearchParams(query, startYear, endYear)
        },
        setValue = {
            putString(KEY_SEARCH_QUERY, it.query)
            putInt(KEY_START_YEAR, it.startYear)
            putInt(KEY_END_YEAR, it.endYear)
        }
    )


    private inline fun <reified E : Enum<E>> enumPref(key: String, defaultValue: E) =
        PrefsDelegate(
            sharedPrefs,
            getValue = { getString(key, null)?.let(::enumValueOf) ?: defaultValue },
            setValue = { putString(key, it.name) }
        )

    companion object {
        private const val PREFS_NAME = "prefs_name"

        private const val KEY_NIGHT_MODE = "night_mode"
        private const val KEY_SEARCH_QUERY = "search_query"
        private const val KEY_START_YEAR = "start_year"
        private const val KEY_END_YEAR = "end_year"
    }
}