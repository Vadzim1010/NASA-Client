package com.example.nasa.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.nasa.R


class MainActivity : AppCompatActivity(R.layout.activity_main) {

// не разобрался можно ли как то навигацию сделать напрямую из фрагмента ипользуя Bottom Navigation
    fun navigate(id: String) {
        findNavController(R.id.container).navigate(
            BottomNavFragmentDirections.toDescription(id)
        )
    }
}

fun Fragment.navigate(id: String) {
    (requireActivity() as MainActivity).navigate(id)
}