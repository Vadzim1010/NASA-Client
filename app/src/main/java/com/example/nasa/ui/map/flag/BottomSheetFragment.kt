package com.example.nasa.ui.map.flag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.nasa.databinding.BottomSheetBinding
import com.example.nasa.domain.model.CountryFlag
import com.example.nasa.domain.model.Resource
import com.example.nasa.utils.applyWindowInsets
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetBinding? = null

    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val args by navArgs<BottomSheetFragmentArgs>()

    private val viewModel by viewModel<BottomSheetViewModel> { parametersOf(args.countryName) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = BottomSheetBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyInsets()
        setDialogBehavior()
        subscribeCountryDescFlow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeCountryDescFlow() {
        viewModel
            .getCountryDescFlow
            .onEach(::render)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun render(resource: Resource<CountryFlag>) = with(binding) {
        when (resource) {
            is Resource.Success -> {
                progressCircular.isVisible = false

                countryName.text = resource.data?.countryName ?: ""
                image.load(resource.data?.flagImageUrl)
            }
            is Resource.Error -> {
                countryName.text = resource.throwable?.message ?: ""
            }
            is Resource.Loading -> {
                progressCircular.isVisible = true
            }
        }
    }

    private fun setDialogBehavior() {
        val behavior = (dialog as BottomSheetDialog).behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun applyInsets() {
        dialog?.window?.decorView?.applyWindowInsets()
    }
}
