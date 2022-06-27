package com.example.nasa.ui.image.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nasa.NavigationBottomDirections
import com.example.nasa.databinding.BottomSheetSearchBinding
import com.example.nasa.domain.model.SearchParams
import com.example.nasa.domain.util.DEFAULT_END_YEAR
import com.example.nasa.domain.util.DEFAULT_START_YEAR
import com.example.nasa.utils.onTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetSearchFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSearchBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val viewModel by viewModel<BottomSheetSearchViewModel>()

    private val searchParams by lazy {
        SearchParams(
            query = viewModel.searchParams.query,
            startYear = viewModel.searchParams.startYear,
            endYear = viewModel.searchParams.endYear,
        )
    }


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

        initSearchListeners()

        with(binding) {
            yearStartEditText.setText(searchParams.startYear.toString())
            yearEndEditText.setText(searchParams.endYear.toString())
            searchQueryEditText.setText(searchParams.query)

            searchButton.setOnClickListener {
                findNavController().navigate(
                    BottomSheetSearchFragmentDirections.toImagesList(
                        searchParams
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initSearchListeners() = with(binding) {
        searchQueryEditText.onTextChangedListener()
            .onEach { query ->
                viewModel.searchParams = searchParams.copy(
                    query = query
                        ?.toString() ?: ""
                )
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        yearStartEditText.onTextChangedListener()
            .onEach { year ->
                viewModel.searchParams = searchParams.copy(
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
                viewModel.searchParams = searchParams.copy(
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
    }
}