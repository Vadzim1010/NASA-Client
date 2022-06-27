package com.example.nasa.ui.image.search

import androidx.lifecycle.ViewModel
import com.example.nasa.domain.service.SearchParamsService

class BottomSheetSearchViewModel(
    private val searchParamsService: SearchParamsService
) : ViewModel() {

    var searchParams by searchParamsService::searchParams
}