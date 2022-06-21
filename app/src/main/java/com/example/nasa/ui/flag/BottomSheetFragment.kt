package com.example.nasa.ui.flag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.nasa.data.util.log
import com.example.nasa.databinding.BottomSheetBinding
import com.example.nasa.domain.model.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BottomSheetFragment private constructor() : BottomSheetDialogFragment() {


    private var _binding: BottomSheetBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val name by lazy { requireArguments().getString(NAME_KEY) }

    private val viewModel by viewModel<BottomSheetViewModel> { parametersOf(name) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = BottomSheetBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel
                .getCountryDescFlow()
                .onEach { countryDesc ->

                    when (countryDesc) {
                        is Resource.Success -> {
                            progressCircular.isVisible = false

                            log(countryDesc.toString())
                            image.load(countryDesc.data?.flagImageUrl)
                            log(countryDesc.toString())
                        }
                        is Resource.Error -> {
                            showToast(countryDesc.throwable?.message ?: "")
                        }
                        is Resource.Loading -> {
                            log(countryDesc.toString())
                            progressCircular.isVisible = true
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(massage: String) {
        Toast.makeText(requireContext(), massage, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "ModalBottomSheet"

        private const val NAME_KEY = "name_key"

        fun getInstance(name: String): BottomSheetFragment {
            return BottomSheetFragment().apply {
                arguments = bundleOf(NAME_KEY to name)
            }
        }
    }
}
