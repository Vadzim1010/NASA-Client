package com.example.nasa.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nasa.databinding.FragementSettingsBinding
import com.example.nasa.domain.model.NightMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragementSettingsBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragementSettingsBinding.inflate(layoutInflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons()
    }

    private fun initButtons() = with(binding) {
        toolbar.setupWithNavController(findNavController())

        when (viewModel.nightMode) {
            NightMode.DARK -> buttonModeDark
            NightMode.LIGHT -> buttonModeLight
            NightMode.SYSTEM -> buttonModeSystem
        }.isChecked = true

        radioGroup.setOnCheckedChangeListener { _, buttonId ->
            val (prefsMode, systemMode) = when (buttonId) {
                buttonModeDark.id -> NightMode.DARK to AppCompatDelegate.MODE_NIGHT_YES
                buttonModeLight.id -> NightMode.LIGHT to AppCompatDelegate.MODE_NIGHT_NO
                buttonModeSystem.id -> NightMode.SYSTEM to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                else -> error("incorrect buttonId $buttonId")
            }

            viewModel.nightMode = prefsMode
            AppCompatDelegate.setDefaultNightMode(systemMode)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}