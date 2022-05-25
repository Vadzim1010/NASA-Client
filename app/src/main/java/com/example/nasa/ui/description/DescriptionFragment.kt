package com.example.nasa.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.example.nasa.databinding.FragmentDescriptionBinding

class DescriptionFragment : Fragment() {

    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is $_binding" }

    private val args by navArgs<DescriptionFragmentArgs>()

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
            title.text = nasaId
            toolbar.setupWithNavController(findNavController())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}