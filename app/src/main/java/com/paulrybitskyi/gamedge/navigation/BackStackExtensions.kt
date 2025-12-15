package com.paulrybitskyi.gamedge.navigation

import androidx.compose.runtime.snapshots.Snapshot
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

internal inline fun NavBackStack<NavKey>.atomically(block: NavBackStack<NavKey>.() -> Unit) {
    Snapshot.withMutableSnapshot {
        block(this@atomically)
    }
}

internal fun NavBackStack<NavKey>.popUpToRoot() {
    while (size > 1) {
        removeLastOrNull()
    }
}
