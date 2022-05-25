package com.example.nasa.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.nasa.databinding.FragmentDescriptionBinding
import com.example.nasa.utils.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DescriptionFragment : Fragment() {

    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val args by navArgs<DescriptionFragmentArgs>()

    private val viewModel by viewModel<DescriptionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDescriptionBinding.inflate(layoutInflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nasaId = args.itemId

        with(binding) {
            toolbar.setupWithNavController(findNavController())
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.fetchDescription(nasaId)
                    .onEach { resource ->
                        when (resource) {
                            is Resource.Success -> {
                                image.load(resource.data.imageUrl)
                                title.text = resource.data.title
                                description.text = resource.data.description
                            }
                            is Resource.Error -> {
                                //TODO
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}