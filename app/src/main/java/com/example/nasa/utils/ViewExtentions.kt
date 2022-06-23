package com.example.nasa.utils

import android.graphics.Rect
import android.view.View
import android.widget.EditText
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nasa.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


fun RecyclerView.addScrollListenerFlow(
    layoutManager: LinearLayoutManager,
    itemsToLoad: Int
) = callbackFlow {
    val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val totalItemCount = layoutManager.itemCount
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

            if (dy != 0 && totalItemCount <= (lastVisibleItem + itemsToLoad)) {
                trySend(Unit)
            }
        }
    }

    addOnScrollListener(listener)
    awaitClose {
        removeOnScrollListener(listener)
    }
}

fun RecyclerView.addBottomSpaceDecorationRes(@DimenRes spaceRes: Int) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val itemCount = parent.adapter?.itemCount ?: return
            val position = parent.getChildAdapterPosition(view)
            if (position != itemCount - 1) {
                outRect.bottom = spaceRes
            }
        }
    })
}

fun Toolbar.onSearchListenerFlow() = callbackFlow {
    val searchView = menu.findItem(R.id.search).actionView as SearchView
    var isSearchStarted = false

    val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            this@callbackFlow.trySend(SearchStatus.QueryTextSubmit(query ?: ""))
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            // to not start search with empty query when init search line
            if (isSearchStarted && newText.isNullOrBlank()) {
                this@callbackFlow.trySend(SearchStatus.QueryTextSubmit(newText ?: ""))
            } else {
                this@callbackFlow.trySend(SearchStatus.QueryTextChange(newText ?: ""))
            }
            isSearchStarted = true
            return true
        }
    }

    searchView.setOnQueryTextListener(queryTextListener)

    awaitClose {
        searchView.setOnQueryTextListener(null)
    }
}

fun EditText.onTextChangedListener() = callbackFlow {
    val watcher = this@onTextChangedListener.addTextChangedListener { editable ->
        this.trySend(editable)
    }
    this.awaitClose {
        this@onTextChangedListener.removeTextChangedListener(watcher)
    }
}

// find one nav controller by fragment id
fun Fragment.findNavControllerById(@IdRes id: Int): NavController {
    var parent = parentFragment
    while (parent != null) {
        if (parent is NavHostFragment && parent.id == id) {
            return parent.navController
        }
        parent = parent.parentFragment
    }
    throw RuntimeException("NavController with specified id not found")
}

sealed class SearchStatus() {
    data class QueryTextChange(val text: String) : SearchStatus()
    data class QueryTextSubmit(val text: String) : SearchStatus()
}
