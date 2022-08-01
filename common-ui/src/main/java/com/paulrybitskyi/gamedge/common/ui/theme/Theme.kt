/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.common.ui.theme

import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.ProvideWindowInsets

private const val DefaultContentAlpha = 1f

object GamedgeTheme {

    val colors: Colors
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colors

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val spaces: Spaces
        @Composable
        @ReadOnlyComposable
        get() = LocalSpaces.current
}

@Composable
fun GamedgeTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    ProvideWindowInsets {
        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            CompositionLocalProvider(LocalElevationOverlay provides null) {
                MaterialTheme(
                    colors = if (useDarkTheme) darkColors() else lightColors(),
                    typography = typography,
                    shapes = shapes,
                ) {
                    CompositionLocalProvider(LocalRippleTheme provides GamedgeRippleTheme) {
                        CompositionLocalProvider(LocalContentAlpha provides DefaultContentAlpha) {
                            content()
                        }
                    }
                }
            }
        }
    }
}

@Immutable
private object GamedgeRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = Color.Black,
        lightTheme = true,
    )

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(
        contentColor = Color.Black,
        lightTheme = true,
    )
}
