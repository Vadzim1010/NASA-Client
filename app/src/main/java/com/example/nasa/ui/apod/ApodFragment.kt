package com.example.nasa.ui.apod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.nasa.R
import com.example.nasa.databinding.FragmentApodBinding
import com.example.nasa.domain.model.Apod
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.util.emptyApod
import com.example.nasa.ui.navigateToSettings
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


class ApodFragment : Fragment() {

    private var _binding: FragmentApodBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val viewModel by viewModel<ApodViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentApodBinding.inflate(layoutInflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons()
        subscribeOnDataFlow()
        setInsets()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribeOnDataFlow() {
        viewModel.fetchPictureOfDay()
            .onEach(::render)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun render(resource: Resource<List<Apod>>) = with(binding) {
        val data = resource.data
        val throwable = resource.throwable
        val apod = data?.firstOrNull() ?: emptyApod

        renderData(apod)

        when (resource) {
            is Resource.Success -> {
                progressCircular.isVisible = false
            }
            is Resource.Error -> {
                progressCircular.isVisible = false
                showToast(throwable?.message ?: "")
            }
            is Resource.Loading -> {
                progressCircular.isVisible = data.isNullOrEmpty()
            }
        }
    }

    private fun renderData(apod: Apod) = with(binding) {
        image.load(apod.imageUrl)
        title.text = apod.title
        date.text = apod.date
    }

    private fun showToast(massage: String?) {
        Toast.makeText(requireContext(), massage, Toast.LENGTH_SHORT).show()
    }

    private fun setInsets() = with(binding) {
        ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
            appBar.updatePadding(
                top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            )

            WindowInsetsCompat.CONSUMED
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
}