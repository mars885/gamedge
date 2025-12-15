package com.paulrybitskyi.gamedge.navigation.decorators

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.navigation3.runtime.NavEntryDecorator

/**
 * A copy of [androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator]
 * that allows configuring whether to remove the saved state for a given content key upon
 * popping the entry.
 */
@Composable
internal fun <T : Any> rememberConfigurableSaveableStateHolderNavEntryDecorator(
    saveableStateHolder: SaveableStateHolder = rememberSaveableStateHolder(),
    removeSavedStateOnPop: (contentKey: Any) -> Boolean = { true },
): ConfigurableSaveableStateHolderNavEntryDecorator<T> {
    val currentRemoveSavedStateOnPop = rememberUpdatedState(removeSavedStateOnPop)

    return remember(saveableStateHolder, currentRemoveSavedStateOnPop) {
        ConfigurableSaveableStateHolderNavEntryDecorator(saveableStateHolder, removeSavedStateOnPop)
    }
}

internal class ConfigurableSaveableStateHolderNavEntryDecorator<T : Any>(
    saveableStateHolder: SaveableStateHolder,
    removeSavedStateOnPop: (contentKey: Any) -> Boolean = { true },
) : NavEntryDecorator<T>(
    onPop = { contentKey ->
        if (removeSavedStateOnPop(contentKey)) {
            saveableStateHolder.removeState(contentKey)
        }
    },
    decorate = { entry ->
        saveableStateHolder.SaveableStateProvider(entry.contentKey) {
            entry.Content()
        }
    },
)
