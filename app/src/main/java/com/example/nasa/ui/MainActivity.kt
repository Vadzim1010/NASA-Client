package com.example.nasa.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nasa.R
import com.example.nasa.ui.image.NasaImagesFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, NasaImagesFragment())
                .commit()
        }
    }
}