package com.example.nasa.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasa.R
import com.example.nasa.adapter.NasaImagesAdapter
import com.example.nasa.databinding.FragmentNasaImagesBinding
import com.example.nasa.paging.LceState
import com.example.nasa.paging.PagingItem
import com.example.nasa.utils.addBottomSpaceDecorationRes
import com.example.nasa.utils.addPagingScrollFlow
import com.example.nasa.utils.log
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class NasaImagesFragment : Fragment() {

    private var _binding: FragmentNasaImagesBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val nasaImagesAdapter by lazy {
        NasaImagesAdapter(requireContext())
    }

    private val viewModel by viewModel<NasaImageViewModel>()

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
        viewModel.onLoadMore()

        with(binding) {
            swipeRefresh.setOnRefreshListener {
                viewModel.onRefresh()
            }

            val manager = LinearLayoutManager(requireContext())
            recycler.layoutManager = manager
            recycler.adapter = nasaImagesAdapter
            recycler.addPagingScrollFlow(
                layoutManager = manager,
                itemsToLoad = 30,
            )
                .debounce(100)
                .onEach {
                    viewModel.onLoadMore()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
            recycler.addBottomSpaceDecorationRes(resources.getDimensionPixelSize(R.dimen.app_padding_medium))

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
                            val currentList = nasaImagesAdapter.currentList.dropLast(1)
                            val resultList = currentList
                                .plus(lceState.data
                                    .map {
                                        PagingItem.Content(it)
                                    })
                                .plus(PagingItem.Loading)

                            nasaImagesAdapter.submitList(resultList)
                        }
                        is LceState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                lceState.throwable.message ?: "",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        LceState.Loading -> {
                            Toast.makeText(
                                requireContext(),
                                "some message",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                .onEach { log(nasaImagesAdapter.currentList.size.toString()) }
        }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
