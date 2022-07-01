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

fun RecyclerView.addRightSpaceDecorationRes(@DimenRes spaceRes: Int) {
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
                outRect.right = spaceRes
            }
        }
    })
}

fun EditText.onTextChangedListener() = callbackFlow {
    val watcher = this@onTextChangedListener.addTextChangedListener { editable ->
        this.trySend(editable)
    }
    this.awaitClose {
        this@onTextChangedListener.removeTextChangedListener(watcher)
    }
}
