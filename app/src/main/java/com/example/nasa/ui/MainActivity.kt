package com.example.nasa.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.nasa.R
import com.example.nasa.domain.model.NightMode
import com.example.nasa.domain.service.NightModeService
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(R.layout.activity_main) {


    private val nightModeService by inject<NightModeService>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNightModeService()

        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    private fun initNightModeService() {
        AppCompatDelegate.setDefaultNightMode(
            when (nightModeService.nightMode) {
                NightMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
                NightMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                NightMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }

    // не разобрался можно ли как то навигацию сделать напрямую из фрагмента ипользуя Bottom Navigation
    fun navigateToDesc(id: String) {
        findNavController(R.id.container).navigate(
            BottomNavFragmentDirections.toDescription(id)
        )
    }

    fun navigateToSettings() {
        findNavController(R.id.container).navigate(
            BottomNavFragmentDirections.toSettings()
        )
    }
}

fun Fragment.navigateToDesc(id: String) {
    (requireActivity() as MainActivity).navigateToDesc(id)
}

fun Fragment.navigateToSettings() {
    (requireActivity() as MainActivity).navigateToSettings()
}