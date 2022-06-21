package com.example.nasa.ui.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasa.R
import com.example.nasa.adapter.NasaImagesAdapter
import com.example.nasa.data.util.log
import com.example.nasa.databinding.FragmentNasaImagesBinding
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.PagingItem
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.util.*
import com.example.nasa.ui.BottomNavFragmentDirections
import com.example.nasa.ui.navigateToDesc
import com.example.nasa.ui.navigateToSettings
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
            navigateToDesc(nasaId)
        }
    }

    private var startYear = MIN_YEAR

    private var endYear = MAX_YEAR

    private var searchQuery = DEFAULT_SEARCH_QUERY


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

        setInsets()
        initButtons()
        initSearchListeners()
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


    private fun render(resource: Resource<List<NasaImage>>) = with(binding) {
        val imageList = resource.data ?: emptyList()
        val throwable = resource.throwable
        val totalHits = imageList.lastOrNull()?.totalHits ?: 0

        log("content size: ${imageList.size}")
        log("total content length: $totalHits")

        when (resource) {
            is Resource.Success -> {
                progressCircular.isVisible = false
                swipeRefresh.isRefreshing = false

                val pagingList =
                    if (totalHits > imageList.size || imageList.size >= MAX_PAGE * PAGE_SIZE) {
                        imageList.mapToPage
                            .plus(PagingItem.Loading)
                    } else {
                        imageList.mapToPage
                    }

                nasaImagesAdapter.submitList(pagingList)
            }
            is Resource.Error -> {
                progressCircular.isVisible = false
                swipeRefresh.isRefreshing = false

                val pagingList = imageList.mapToPage
                    .plus(throwable
                        ?.let { PagingItem.Error(it) })

                nasaImagesAdapter.submitList(pagingList)
            }
            is Resource.Loading -> {
                progressCircular.isVisible = imageList.isNullOrEmpty()

                if (imageList.size > nasaImagesAdapter.currentList.size) {
                    nasaImagesAdapter.submitList(imageList.mapToPage)
                }
            }
        }
    }

    private fun initSearchListeners() = with(binding) {
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

    private fun initSwipeRefresh() =
        with(binding) {
            swipeRefresh.setOnRefreshListener {
                viewModel.onRefresh(searchQuery, startYear, endYear)
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
                    viewModel.onLoadMore(searchQuery, startYear, endYear)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun initButtons() = with(binding) {
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    navigateToSettings()
                    true
                }
                else -> false
            }
        }
    }

    private fun setInsets() = with(binding) {
        ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
            appBar.updatePadding(
                top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            )

            WindowInsetsCompat.CONSUMED
        }
    }
}
