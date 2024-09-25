/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paulrybitskyi.gamedge.common.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import androidx.annotation.DimenRes
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun textSizeResource(@DimenRes id: Int): TextUnit {
    return with(LocalDensity.current) {
        dimensionResource(id).toSp()
    }
}

@Composable
fun OnLifecycleEvent(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onCreate: (() -> Unit)? = null,
    onStart: (() -> Unit)? = null,
    onResume: (() -> Unit)? = null,
    onPause: (() -> Unit)? = null,
    onStop: (() -> Unit)? = null,
    onDestroy: (() -> Unit)? = null,
    onAny: (() -> Unit)? = null,
) {
    val currentOnCreate by rememberUpdatedState(onCreate)
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnResume by rememberUpdatedState(onResume)
    val currentOnPause by rememberUpdatedState(onPause)
    val currentOnStop by rememberUpdatedState(onStop)
    val currentOnDestroy by rememberUpdatedState(onDestroy)
    val currentOnAny by rememberUpdatedState(onAny)

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> currentOnCreate?.invoke()
                Lifecycle.Event.ON_START -> currentOnStart?.invoke()
                Lifecycle.Event.ON_RESUME -> currentOnResume?.invoke()
                Lifecycle.Event.ON_PAUSE -> currentOnPause?.invoke()
                Lifecycle.Event.ON_STOP -> currentOnStop?.invoke()
                Lifecycle.Event.ON_DESTROY -> currentOnDestroy?.invoke()
                Lifecycle.Event.ON_ANY -> currentOnAny?.invoke()
            }
        }

        lifecycle.addObserver(lifecycleObserver)
        onDispose { lifecycle.removeObserver(lifecycleObserver) }
    }
}

@Composable
fun Modifier.clickable(
    indication: Indication?,
    onClick: () -> Unit,
) = this.composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = indication,
        onClick = onClick,
    )
}

@Composable
fun findWindow(): Window? {
    return (LocalView.current.parent as? DialogWindowProvider)?.window
        ?: LocalView.current.context.findWindow()
}

private tailrec fun Context.findWindow(): Window? {
    return when (this) {
        is Activity -> window
        is ContextWrapper -> baseContext.findWindow()
        else -> null
    }
}

@Composable
fun rememberWindowInsetsController(
    window: Window? = findWindow(),
): WindowInsetsControllerDecorator? {
    val view = LocalView.current

    return remember(view, window) {
        if (window != null) {
            WindowInsetsControllerDecorator(
                controller = WindowCompat.getInsetsController(window, view),
            )
        } else {
            null
        }
    }
}

class WindowInsetsControllerDecorator(
    private val controller: WindowInsetsControllerCompat,
) {

    private companion object {
        const val DEFAULT_DELAY_IN_MILLIS = 0L
    }

    val areStatusBarIconsLight: Boolean
        get() = !controller.isAppearanceLightStatusBars

    val areNavigationBarIconsLight: Boolean
        get() = !controller.isAppearanceLightNavigationBars

    fun setStatusBarIconsLight(isLight: Boolean, delay: Long = DEFAULT_DELAY_IN_MILLIS) {
        GlobalWindowInsetsControllerDecorator.setStatusBarIconsLight(
            controller = controller,
            isLight = isLight,
            delay = delay,
        )
    }

    fun setNavigationBarIconsLight(isLight: Boolean, delay: Long = DEFAULT_DELAY_IN_MILLIS) {
        GlobalWindowInsetsControllerDecorator.setNavigationBarIconsLight(
            controller = controller,
            isLight = isLight,
            delay = delay,
        )
    }

    private object GlobalWindowInsetsControllerDecorator {

        private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

        private var statusBarIconsColorJob: Job? = null
        private var navigationBarIconsColorJob: Job? = null

        fun setStatusBarIconsLight(
            controller: WindowInsetsControllerCompat,
            isLight: Boolean,
            delay: Long,
        ) {
            statusBarIconsColorJob = performAction(statusBarIconsColorJob, delay) {
                controller.isAppearanceLightStatusBars = !isLight
            }
        }

        fun setNavigationBarIconsLight(
            controller: WindowInsetsControllerCompat,
            isLight: Boolean,
            delay: Long,
        ) {
            navigationBarIconsColorJob = performAction(navigationBarIconsColorJob, delay) {
                controller.isAppearanceLightNavigationBars = !isLight
            }
        }

        private inline fun performAction(
            job: Job?,
            delay: Long,
            crossinline action: () -> Unit
        ): Job {
            if (job?.isActive == true) {
                job.cancel()
            }

            return coroutineScope.launch {
                delay(delay)
                action()
            }
        }
    }
}
