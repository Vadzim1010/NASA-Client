package com.example.nasa.ui.image

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasa.NavGraphDirections
import com.example.nasa.R
import com.example.nasa.adapter.NasaImagesAdapter
import com.example.nasa.data.util.log
import com.example.nasa.databinding.FragmentNasaImagesBinding
import com.example.nasa.domain.model.NasaImage
import com.example.nasa.domain.model.PagingItem
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.util.*
import com.example.nasa.utils.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class NasaImagesFragment : Fragment() {


    private var _binding: FragmentNasaImagesBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val viewModel by viewModel<NasaImageViewModel>()

    private var startYear = MIN_YEAR

    private var endYear = MAX_YEAR

    private var searchQuery = DEFAULT_SEARCH_QUERY

    private val nasaImagesAdapter by lazy {
        NasaImagesAdapter(
            context = requireContext(),
            onItemClickListener = { nasaId ->
                findNavControllerById(R.id.container).navigate(
                    NavGraphDirections.toDescription(
                        nasaId
                    )
                )
            },
            onReloadClickListener = {
                viewModel.onRefresh(searchQuery, startYear, endYear)
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
            is Resource.Error -> {
                progressCircular.isVisible = false
                swipeRefresh.isRefreshing = false

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
            is Resource.Loading -> {
                progressCircular.isVisible = imageList.isNullOrEmpty()
                noDataInfoTextView.isVisible = false

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
            .onEach { searchStatus ->
                when (searchStatus) {
                    is SearchStatus.QueryTextChange -> {
                        searchQuery = searchStatus.text
                    }
                    is SearchStatus.QueryTextSubmit -> {
                        searchQuery = searchStatus.text

                        viewModel.onReload(searchQuery, startYear, endYear)
                        nasaImagesAdapter.submitList(emptyList())
                        binding.progressCircular.isVisible = true
                    }
                }
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
        searchButton.setOnClickListener {
            log("$searchQuery $startYear")

            viewModel.onReload(searchQuery, startYear, endYear)
            nasaImagesAdapter.submitList(emptyList())
            binding.progressCircular.isVisible = true
        }

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings -> {
                    findNavControllerById(R.id.container).navigate(NavGraphDirections.toSettings())
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
