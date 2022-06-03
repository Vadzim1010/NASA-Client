package com.example.nasa.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasa.R
import com.example.nasa.adapter.NasaImagesAdapter
import com.example.nasa.domain.util.MAX_YEAR
import com.example.nasa.domain.util.MIN_YEAR
import com.example.nasa.data.util.log
import com.example.nasa.databinding.FragmentNasaImagesBinding
import com.example.nasa.domain.model.LceState
import com.example.nasa.domain.model.PagingItem
import com.example.nasa.domain.util.mapToPage
import com.example.nasa.ui.navigate
import com.example.nasa.utils.addBottomSpaceDecorationRes
import com.example.nasa.utils.addScrollListenerFlow
import com.example.nasa.utils.onSearchListenerFlow
import com.example.nasa.utils.onTextChangedListener
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class NasaImagesFragment : Fragment() {


    private var _binding: FragmentNasaImagesBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val viewModel by viewModel<NasaImageViewModel>()

    private val nasaImagesAdapter by lazy {
        NasaImagesAdapter(requireContext()) { nasaId ->
            navigate(nasaId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentNasaImagesBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var startYear = 1920
        var endYear = 2022
        var searchQuery = ""

        with(binding) {
            yearStartEditText.onTextChangedListener()
                .onEach { year ->
                    startYear = year
                        ?.toString()
                        ?.toIntOrNull()
                        ?.takeIf { it in MIN_YEAR until MAX_YEAR }
                        ?.also { yearStartEditContainer.error = "" } ?: run {
                        yearStartEditContainer.error = "incorrect format"
                        0
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            yearEndEditText.onTextChangedListener()
                .onEach { year ->
                    endYear = year
                        ?.toString()
                        ?.toIntOrNull()
                        ?.takeIf { it in MIN_YEAR + 1..MAX_YEAR }
                        ?.also { yearEndEditContainer.error = "" } ?: run {
                        yearEndEditContainer.error = "incorrect format"
                        0
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            toolbar.onSearchListenerFlow()
                .onEach { text ->
                    searchQuery = text ?: ""

                    viewModel.onRefresh(searchQuery, startYear, endYear)
                    nasaImagesAdapter.submitList(emptyList())
                    binding.progressCircular.isVisible = true
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }

        initRecycler(searchQuery, startYear, endYear)
        initSwipeRefresh(searchQuery, startYear, endYear)
        subscribeOnPagingData()
    }

    private fun subscribeOnPagingData() =
        with(binding) {
            viewModel
                .getImagesPagingSource()
                .onEach { resource ->
                    when (resource) {
                        is LceState.Content -> {
                            progressCircular.isVisible = false
                            swipeRefresh.isRefreshing = false

                            log("content size: ${resource.data.size}")

                            val networkList = if (!resource.hasMoreData) {
                                resource.data.mapToPage.plus(PagingItem.Loading)
                            } else {
                                resource.data.mapToPage
                            }

                            nasaImagesAdapter.submitList(networkList)
                        }
                        is LceState.Error -> {
                            progressCircular.isVisible = false
                            swipeRefresh.isRefreshing = false

                            val cacheList = resource.data?.mapToPage

                            nasaImagesAdapter.submitList(cacheList)
                        }
                        is LceState.Loading -> {
                            val cacheList = resource.data?.mapToPage ?: emptyList()

                            progressCircular.isVisible = cacheList.isNullOrEmpty()

                            if (cacheList.size > nasaImagesAdapter.currentList.size) {
                                nasaImagesAdapter.submitList(cacheList)
                            }
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }

    private fun initSwipeRefresh(searchQuery: String, yearStart: Int, yearEnd: Int) =
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                viewModel.onRefresh(searchQuery, yearStart, yearEnd)
                nasaImagesAdapter.submitList(emptyList())
                binding.progressCircular.isVisible = true
            }
        }

    private fun initRecycler(searchQuery: String, yearStart: Int, yearEnd: Int) = with(binding) {
        val manager = LinearLayoutManager(requireContext())
        recycler.apply {
            layoutManager = manager
            adapter = nasaImagesAdapter
            addBottomSpaceDecorationRes(resources.getDimensionPixelSize(R.dimen.app_padding_medium))
            addScrollListenerFlow(
                layoutManager = manager,
                itemsToLoad = 30,
            )
                .onEach {
                    viewModel.onLoadMore(searchQuery, yearStart, yearEnd)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.onStopLoading()
    }
}
