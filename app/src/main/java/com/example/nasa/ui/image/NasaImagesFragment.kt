package com.example.nasa.ui.image

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasa.NavGraphDirections
import com.example.nasa.R
import com.example.nasa.adapter.NasaImagesAdapter
import com.example.nasa.data.util.log
import com.example.nasa.databinding.FragmentNasaImagesBinding
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.PagingItem
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.util.MAX_PAGE
import com.example.nasa.domain.util.PAGE_SIZE
import com.example.nasa.domain.util.mapToPage
import com.example.nasa.ui.image.search.BottomSheetSearchFragment.Companion.IS_PARAMS_CHANGED_KEY
import com.example.nasa.ui.image.search.BottomSheetSearchFragment.Companion.REQUEST_KEY
import com.example.nasa.utils.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class NasaImagesFragment : Fragment() {

    private var _binding: FragmentNasaImagesBinding? = null

    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val viewModel by viewModel<NasaImageViewModel>()

    private val nasaImagesAdapter by lazy {
        NasaImagesAdapter(
            context = requireContext(),
            onCardClicked = { nasaId ->
                findNavControllerById(R.id.container).navigate(
                    NavGraphDirections.toDescription(
                        nasaId
                    )
                )
            },
            onReloadButtonClicked = {
                viewModel.onRefresh()
            })
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

        applyInsets()
        initSearchParamsChangeListener()
        initButtons()
        initRecycler()
        initSwipeRefresh()
        subscribeOnDataFlow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeOnDataFlow() {
        viewModel
            .pagingSourceFlow
            .onEach(::render)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun render(resource: Resource<List<NasaImage>>) {
        when (resource) {
            is Resource.Success -> onSuccessScreen(resource)
            is Resource.Error -> onErrorScreen(resource)
            is Resource.Loading -> onLoadingScreen(resource)
        }
    }

    private fun initSwipeRefresh() = with(binding) {
        swipeRefresh.setOnRefreshListener {
            viewModel.onReload()
        }
    }

    private fun initRecycler() = with(binding) {
        val orientation = resources.configuration.orientation

        val manager = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        } else {
            LinearLayoutManager(requireContext())
        }

        recycler.apply {
            layoutManager = manager
            adapter = nasaImagesAdapter

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                addRightSpaceDecorationRes(resources.getDimensionPixelSize(R.dimen.app_padding_medium))
            } else {
                addBottomSpaceDecorationRes(resources.getDimensionPixelSize(R.dimen.app_padding_medium))
            }

            addScrollListenerFlow(layoutManager = manager, itemsToLoad = 30)
                .onEach {
                    viewModel.onLoadMore()
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun initButtons() = with(binding) {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    findNavControllerById(R.id.container).navigate(NavGraphDirections.toSettings())
                    true
                }
                R.id.search -> {
                    findNavController().navigate(NasaImagesFragmentDirections.toBottomSheetSearch())
                    true
                }
                else -> false
            }
        }
    }

    private fun initSearchParamsChangeListener() {
        setFragmentResultListener(REQUEST_KEY) { _, bundle ->
            val isSearchParamsChanged = bundle.getBoolean(IS_PARAMS_CHANGED_KEY)

            if (isSearchParamsChanged) {
                viewModel.onReload()
                binding.progressCircular.isVisible = true
                nasaImagesAdapter.submitList(emptyList())
            }
        }
    }

    private fun applyInsets() = with(binding) {
        appBar.applyWindowInsets()
        recycler.applyHorizontalWindowInsets()
    }

    private fun onSuccessScreen(resource: Resource<List<NasaImage>>) = with(binding) {
        val imageList = resource.data ?: emptyList()
        val totalHits = imageList.lastOrNull()?.totalHits ?: 0

        progressCircular.isVisible = false
        swipeRefresh.isRefreshing = false

        log("content size: ${imageList.size}")
        log("total content length: $totalHits")

        if (imageList.isEmpty()) {
            noDataInfoTextView.isVisible = true
            noDataInfoTextView.text = getString(R.string.no_data)
        } else {
            noDataInfoTextView.isVisible = false
        }

        val pagingList =
            if (totalHits > imageList.size || imageList.size >= MAX_PAGE * PAGE_SIZE) {
                imageList.mapToPage
                    .plus(PagingItem.Loading)
            } else {
                imageList.mapToPage
            }

        nasaImagesAdapter.submitList(pagingList)
    }

    private fun onErrorScreen(resource: Resource<List<NasaImage>>) = with(binding) {
        val imageList = resource.data ?: emptyList()
        val throwable = resource.throwable
        val totalHits = imageList.lastOrNull()?.totalHits ?: 0

        progressCircular.isVisible = false
        swipeRefresh.isRefreshing = false

        log("content size: ${imageList.size}")
        log("total content length: $totalHits")

        if (imageList.isEmpty()) {
            noDataInfoTextView.isVisible = true
            noDataInfoTextView.text = buildSpannedString {
                color(Color.RED) {
                    append(throwable?.message)
                }
            }
        } else {
            noDataInfoTextView.isVisible = false
        }

        val pagingList = if (imageList.isNotEmpty()) {
            imageList.mapToPage
                .plus(throwable
                    ?.let { PagingItem.Error(it) })
        } else {
            imageList.mapToPage
        }

        nasaImagesAdapter.submitList(pagingList)
    }

    private fun onLoadingScreen(resource: Resource<List<NasaImage>>) = with(binding) {
        val imageList = resource.data ?: emptyList()

        progressCircular.isVisible = imageList.isNullOrEmpty()
        noDataInfoTextView.isVisible = false

        if (imageList.size > nasaImagesAdapter.currentList.size) {
            nasaImagesAdapter.submitList(imageList.mapToPage)
        }
    }
}
