package com.example.nasa.ui.description

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.nasa.databinding.FragmentDescriptionBinding
import com.example.nasa.domain.model.Description
import com.example.nasa.domain.model.Resource
import com.example.nasa.domain.util.emptyDescription
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DescriptionFragment : Fragment() {


    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val args by navArgs<DescriptionFragmentArgs>()

    private val nasaId by lazy { args.itemId }

    private val viewModel by viewModel<DescriptionViewModel> { parametersOf(nasaId) }


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

        setInsets()
        initButtons()
        subscribeOnDataFlow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initButtons() = with(binding) {
        toolbar.setupWithNavController(findNavController())
    }

    private fun subscribeOnDataFlow() {
        viewModel.getDescriptionFlow()
            .onEach(::render)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun render(resource: Resource<List<Description>>) = with(binding) {
        val data = resource.data
        val throwable = resource.throwable
        val description = data?.firstOrNull() ?: emptyDescription

        renderData(description)

        when (resource) {
            is Resource.Success -> {
                progressCircular.isVisible = false
            }
            is Resource.Error -> {
                progressCircular.isVisible = false
                showToast(throwable?.message ?: "")
            }
            is Resource.Loading -> {
                progressCircular.isVisible = data?.firstOrNull()?.description.isNullOrEmpty()
            }
        }
    }

    private fun renderData(imageDescription: Description) = with(binding) {
        image.load(imageDescription.imageUrl)
        title.text = imageDescription.title
        description.text = imageDescription.description
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
}