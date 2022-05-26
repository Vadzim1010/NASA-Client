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
import com.example.nasa.databinding.FragmentNasaImagesBinding
import com.example.nasa.paging.PagingItem
import com.example.nasa.ui.navigate
import com.example.nasa.utils.*
import kotlinx.coroutines.flow.debounce
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
        initRecycler()
        initSwipeRefresh()
        initSearchListener()
        subscribeOnPagingData()
        viewModel.onLoadMore()
    }

    private fun subscribeOnPagingData() = with(binding) {
        viewModel
            .getImagesPagingSource()
            .onEach { resource ->
                when (resource) {
                    is Resource.Success -> {
                        log("content size: ${resource.data.size}")

                        val networkList = if (!resource.isLastPage) {
                            resource.data.mapToPage + PagingItem.Loading
                        } else {
                            resource.data.mapToPage
                        }

                        nasaImagesAdapter.submitList(networkList)
                    }
                    is Resource.Error -> {
                        val cacheList = resource.data?.mapToPage

                        nasaImagesAdapter.submitList(cacheList)
                    }
                    is Resource.Loading -> {
                        val cacheList = resource.data?.mapToPage

                        progressCircular.isVisible = cacheList?.isNullOrEmpty() ?: false

                        nasaImagesAdapter.submitList(cacheList)
                    }
                }
            }
            .onEach {
                if (it !is Resource.Loading) {
                    swipeRefresh.isRefreshing = false
                }
                if (it !is Resource.Loading) {
                    progressCircular.isVisible = false
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initSwipeRefresh() = with(binding) {
        swipeRefresh.setOnRefreshListener {
            viewModel.onRefresh()
            nasaImagesAdapter.submitList(emptyList())
            binding.progressCircular.isVisible = true
        }
    }

    private fun initRecycler() = with(binding) {
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
                    viewModel.onLoadMore()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun initSearchListener() = with(binding) {
        yearStartEditText.onTextChangedListener()
            .onEach { startYear ->
                val year = startYear
                    ?.toString()
                    ?.toIntOrNull()
                    ?.takeIf { it in MIN_YEAR until MAX_YEAR }
                    ?.also { yearStartEditContainer.error = "" } ?: run {
                    yearStartEditContainer.error = "incorrect format"
                    0
                }
                viewModel.setStartYear(year)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        yearEndEditText.onTextChangedListener()
            .onEach { endYear ->
                val year = endYear
                    ?.toString()
                    ?.toIntOrNull()
                    ?.takeIf { it in MIN_YEAR + 1..MAX_YEAR }
                    ?.also { yearEndEditContainer.error = "" } ?: run {
                    yearEndEditContainer.error = "incorrect format"
                    0
                }
                viewModel.setEndYear(year)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        toolbar.onSearchListenerFlow()
            .debounce(3000)
            .onEach { text ->
                val query = text ?: ""
                viewModel.setSearchQuery(query)
            }
            .onEach {
                viewModel.onRefresh()
                nasaImagesAdapter.submitList(emptyList())
                binding.progressCircular.isVisible = true
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
