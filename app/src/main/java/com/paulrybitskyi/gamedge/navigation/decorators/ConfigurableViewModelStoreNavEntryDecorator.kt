package com.paulrybitskyi.gamedge.navigation.decorators

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner

/**
 * A copy of [androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator]
 * that allows configuring whether to remove the ViewModelStore for a given content key upon
 * popping the entry.
 */
@Composable
internal fun <T : Any> rememberConfigurableViewModelStoreNavEntryDecorator(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
    removeViewModelStoreOnPop: (contentKey: Any) -> Boolean =
        ConfigurableViewModelStoreNavEntryDecoratorDefaults.removeViewModelStoreOnPop(),
): ConfigurableViewModelStoreNavEntryDecorator<T> {
    val currentRemoveViewModelStoreOnPop = rememberUpdatedState(removeViewModelStoreOnPop)

    return remember(viewModelStoreOwner, currentRemoveViewModelStoreOnPop) {
        ConfigurableViewModelStoreNavEntryDecorator(
            viewModelStoreOwner.viewModelStore,
            removeViewModelStoreOnPop,
        )
    }
}

internal class ConfigurableViewModelStoreNavEntryDecorator<T : Any>(
    viewModelStore: ViewModelStore,
    removeViewModelStoreOnPop: (contentKey: Any) -> Boolean,
) : NavEntryDecorator<T>(
        onPop = { key ->
            if (removeViewModelStoreOnPop(key)) {
                viewModelStore.getEntryViewModel().clearViewModelStoreOwnerForKey(key)
            }
        },
        decorate = { entry ->
            val viewModelStore = viewModelStore.getEntryViewModel().viewModelStoreForKey(
                entry.contentKey,
            )
            val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current
            val childViewModelStoreOwner = remember {
                object :
                    ViewModelStoreOwner,
                    SavedStateRegistryOwner by savedStateRegistryOwner,
                    HasDefaultViewModelProviderFactory {

                    override val viewModelStore: ViewModelStore
                        get() = viewModelStore

                    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
                        get() = SavedStateViewModelFactory()

                    override val defaultViewModelCreationExtras: CreationExtras
                        get() = MutableCreationExtras().also {
                            it[SAVED_STATE_REGISTRY_OWNER_KEY] = this
                            it[VIEW_MODEL_STORE_OWNER_KEY] = this
                        }

                    init {
                        require(this.lifecycle.currentState == Lifecycle.State.INITIALIZED) {
                            "The Lifecycle state is already beyond INITIALIZED. The " +
                                "ViewModelStoreNavEntryDecorator requires adding the " +
                                "SavedStateNavEntryDecorator to ensure support for " +
                                "SavedStateHandles."
                        }
                        enableSavedStateHandles()
                    }
                }
            }

            CompositionLocalProvider(LocalViewModelStoreOwner provides childViewModelStoreOwner) {
                entry.Content()
            }
        },
    )

private class EntryViewModel : ViewModel() {
    private val owners = mutableMapOf<Any, ViewModelStore>()

    fun viewModelStoreForKey(key: Any): ViewModelStore = owners.getOrPut(key) { ViewModelStore() }

    fun clearViewModelStoreOwnerForKey(key: Any) {
        owners.remove(key)?.clear()
    }

    override fun onCleared() {
        owners.forEach { (_, store) -> store.clear() }
    }
}

internal object ConfigurableViewModelStoreNavEntryDecoratorDefaults {

    @Composable
    fun removeViewModelStoreOnPop(): (contentKey: Any) -> Boolean {
        val activity = LocalActivity.current
        return { activity?.isChangingConfigurations != true }
    }
}

private fun ViewModelStore.getEntryViewModel(): EntryViewModel {
    val provider = ViewModelProvider.create(
        store = this,
        factory = viewModelFactory { initializer { EntryViewModel() } },
    )

    return provider[EntryViewModel::class]
}
