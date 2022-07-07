package com.example.nasa

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.nasa.data.service.prefs.PrefsServiceImpl
import com.example.nasa.domain.model.NightMode
import com.example.nasa.domain.model.SearchParams
import com.example.nasa.domain.util.DEFAULT_END_YEAR
import com.example.nasa.domain.util.DEFAULT_SEARCH_QUERY
import com.example.nasa.domain.util.DEFAULT_START_YEAR
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PreferencesServiceTest {

    @Test
    fun testDefaultValues() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefsService = PrefsServiceImpl(context)

        val defaultSearchParams = SearchParams(DEFAULT_SEARCH_QUERY, DEFAULT_START_YEAR, DEFAULT_END_YEAR)

        Assert.assertEquals(NightMode.SYSTEM, prefsService.nightMode)
        Assert.assertEquals(defaultSearchParams, prefsService.searchParams)
    }

    @Test
    fun testSearchParamsChange() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefsService = PrefsServiceImpl(context)

        val searchQuery = "some"
        val startYearValue = 1940
        val endYearValue = 1960

        val searchParamsValue = SearchParams(searchQuery, startYearValue, endYearValue)

        prefsService.searchParams = searchParamsValue

        Assert.assertEquals(searchParamsValue, prefsService.searchParams)
    }

    @Test
    fun testNightModeChange() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefsService = PrefsServiceImpl(context)

        prefsService.nightMode = NightMode.DARK

        Assert.assertEquals(NightMode.DARK, prefsService.nightMode)
    }
}
