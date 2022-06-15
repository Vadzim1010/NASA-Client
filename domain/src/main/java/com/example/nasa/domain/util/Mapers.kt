package com.example.nasa.domain.util

import com.example.nasa.domain.model.Apod
import com.example.nasa.domain.model.Description
import com.example.nasa.domain.model.PagingItem

val <T> List<T>.mapToPage: List<PagingItem.Content<T>>
    get() = this.map { PagingItem.Content(it) }

val emptyDescription: Description
    get() = Description("", "", "", "")

val emptyApod: Apod
    get() = Apod("", "", "")