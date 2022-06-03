package com.example.nasa.ui.apod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.nasa.databinding.FragmentPictureOfDayBinding
import com.example.nasa.domain.model.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


class ApodFragment : Fragment() {

    private var _binding: FragmentPictureOfDayBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val viewModel by viewModel<ApodViewModel>()

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
                            progressCircular.isVisible = false

                            image.load(resource.data.first().imageUrl)
                            title.text = resource.data.first().title
                            date.text = resource.data.first().date
                        }
                        is Resource.Error -> {
                            progressCircular.isVisible = false

                            if (!resource.data.isNullOrEmpty()) {
                                image.load(resource.data?.first()?.imageUrl)
                                title.text = resource.data?.first()?.title
                                date.text = resource.data?.first()?.date
                            }
                        }
                        is Resource.Loading -> {
                            progressCircular.isVisible = resource.data.isNullOrEmpty()

                            if (!resource.data.isNullOrEmpty()) {
                                image.load(resource.data?.first()?.imageUrl)
                                title.text = resource.data?.first()?.title
                                date.text = resource.data?.first()?.date
                            }
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