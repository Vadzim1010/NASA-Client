package com.example.nasa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.ui.setupWithNavController
import com.example.nasa.databinding.FragmentBottomNavBinding
import com.example.nasa.utils.findNestedController

class NavHomeFragment : Fragment() {

    private var _binding: FragmentBottomNavBinding? = null

    private val binding get() = requireNotNull(_binding) { "binding is null $_binding" }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBottomNavBinding.inflate(layoutInflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            bottomNavigation.setupWithNavController(findNestedController())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
