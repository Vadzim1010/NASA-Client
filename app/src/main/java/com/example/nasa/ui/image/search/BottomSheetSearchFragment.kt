package com.example.nasa.ui.image.search

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nasa.databinding.BottomSheetSearchBinding
import com.example.nasa.domain.util.DEFAULT_END_YEAR
import com.example.nasa.domain.util.DEFAULT_START_YEAR
import com.example.nasa.utils.applyWindowInsets
import com.example.nasa.utils.onTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetSearchFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSearchBinding? = null

    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val viewModel by viewModel<BottomSheetSearchViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return BottomSheetSearchBinding.inflate(layoutInflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyInsets()
        setDialogBehavior()
        initSearchParamsListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDialogBehavior() {
        val behavior = (dialog as BottomSheetDialog).behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun applyInsets() {
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dialog?.window?.decorView?.applyWindowInsets()
        }
    }

    private fun initSearchParamsListeners() = with(binding) {
        var searchParams = viewModel.searchParams

        searchQueryEditText.onTextChangedListener()
            .onEach { query ->
                searchParams = searchParams.copy(
                    query = query
                        ?.toString() ?: ""
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        yearStartEditText.onTextChangedListener()
            .onEach { year ->
                searchParams = searchParams.copy(
                    startYear = year
                        ?.toString()
                        ?.toIntOrNull()
                        ?.takeIf { it in DEFAULT_START_YEAR until DEFAULT_END_YEAR }
                        ?.also { yearStartEditContainer.error = "" } ?: run {
                        yearStartEditContainer.error = "incorrect format"
                        0
                    })
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        yearEndEditText.onTextChangedListener()
            .onEach { year ->
                searchParams = searchParams.copy(
                    endYear = year
                        ?.toString()
                        ?.toIntOrNull()
                        ?.takeIf { it in DEFAULT_START_YEAR + 1..DEFAULT_END_YEAR }
                        ?.also { yearEndEditContainer.error = "" } ?: run {
                        yearEndEditContainer.error = "incorrect format"
                        0
                    })
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        yearStartEditText.setText(searchParams.startYear.toString())
        yearEndEditText.setText(searchParams.endYear.toString())
        searchQueryEditText.setText(searchParams.query)

        searchButton.setOnClickListener {
            viewModel.searchParams = searchParams
            setFragmentResult(REQUEST_KEY, bundleOf(IS_PARAMS_CHANGED_KEY to true))
            findNavController().navigateUp()
        }
    }


    companion object {

        const val REQUEST_KEY = "request_key"
        const val IS_PARAMS_CHANGED_KEY = "is_params_changed"
    }
}
