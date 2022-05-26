package com.example.nasa.ui.day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.nasa.databinding.FragmentPictureOfDayBinding
import com.example.nasa.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


class ImageOfDayFragment : Fragment() {

    private var _binding: FragmentPictureOfDayBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val viewModel by viewModel<ImageOfDayViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPictureOfDayBinding.inflate(layoutInflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            viewModel.fetchPictureOfDay()
                .onEach { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            image.load(resource.data.imageUrl)
                            title.text = resource.data.title
                            date.text = resource.data.date
                        }
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {
                            progressCircular.isVisible = true
                        }
                    }
                }
                .onEach {
                    if (it !is Resource.Loading) {
                        progressCircular.isVisible = false
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}