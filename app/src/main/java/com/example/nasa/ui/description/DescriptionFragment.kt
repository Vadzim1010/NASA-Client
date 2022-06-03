package com.example.nasa.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.nasa.databinding.FragmentDescriptionBinding
import com.example.nasa.domain.model.Resource
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
            viewModel.fetchDescription(nasaId)
                .onEach { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            progressCircular.isVisible = false

                            image.load(resource.data.first().imageUrl)
                            title.text = resource.data.first().title
                            description.text = resource.data.first().description
                        }
                        is Resource.Error -> {
                            progressCircular.isVisible = false

                            if (!resource.data.isNullOrEmpty()) {
                                image.load(resource.data?.first()?.imageUrl)
                                title.text = resource.data?.first()?.title
                                description.text = resource.data?.first()?.description
                            }
                            showToast(resource.throwable.message)
                        }
                        is Resource.Loading -> {
                            progressCircular.isVisible =
                                resource.data?.first()?.description.isNullOrEmpty()

                            if (!resource.data.isNullOrEmpty()) {
                                image.load(resource.data?.first()?.imageUrl)
                                title.text = resource.data?.first()?.title
                                description.text = resource.data?.first()?.description
                            }
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun showToast(massage: String?) {
        Toast.makeText(requireContext(), massage, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}