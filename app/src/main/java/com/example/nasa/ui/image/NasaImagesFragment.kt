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
import com.example.nasa.model.LceState
import com.example.nasa.paging.PagingItem
import com.example.nasa.utils.addBottomSpaceDecorationRes
import com.example.nasa.utils.addScrollListenerFlow
import com.example.nasa.utils.log
import com.example.nasa.utils.mapToPage
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class NasaImagesFragment : Fragment() {

    private var _binding: FragmentNasaImagesBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val viewModel by viewModel<NasaImageViewModel>()

    private val nasaImagesAdapter by lazy {
        NasaImagesAdapter(requireContext())
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
        subscribeOnPagingData()
        viewModel.onLoadMore()
    }

    private fun subscribeOnPagingData() {
        viewModel
            .getImagesPagingSource()
            .onEach {
                if (it != LceState.Loading) {
                    binding.swipeRefresh.isRefreshing = false
                }
            }
            .onEach { lceState ->
                binding.progressCircular.isVisible = lceState == LceState.Loading
                when (lceState) {
                    is LceState.Content -> {
                        val networkList = lceState.data.mapToPage + PagingItem.Loading

                        nasaImagesAdapter.submitList(networkList)
                    }
                    is LceState.Error -> {
                        val cacheList = lceState.data.mapToPage + PagingItem.Loading

                        nasaImagesAdapter.submitList(cacheList)
                    }
                    LceState.Loading -> {
                        println()
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initSwipeRefresh() = with(binding) {
        swipeRefresh.setOnRefreshListener {
            viewModel.onRefresh()
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
                .debounce(400)
                .onEach {
                    viewModel.onLoadMore()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
