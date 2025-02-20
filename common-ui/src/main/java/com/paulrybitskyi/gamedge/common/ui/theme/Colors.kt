/*
 * Copyright 2022 Paul Rybitskyi, oss@paulrybitskyi.com
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

@file:Suppress("unused")

package com.paulrybitskyi.gamedge.common.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.paulrybitskyi.gamedge.core.R

@Composable
fun lightColors(): Colors {
    return lightColors(
        primary = colorResource(R.color.light_colorPrimary),
        primaryVariant = colorResource(R.color.light_colorPrimaryVariant),
        secondary = colorResource(R.color.light_colorSecondary),
        secondaryVariant = Color.Unspecified,
        background = colorResource(R.color.light_colorBackground),
        surface = colorResource(R.color.light_colorSurface),
        onPrimary = colorResource(R.color.light_colorOnPrimary),
        onSecondary = colorResource(R.color.light_colorOnSecondary),
        onBackground = colorResource(R.color.light_colorOnBackground),
        onSurface = colorResource(R.color.light_colorOnSurface),
    )
}

@Composable
fun darkColors(): Colors {
    return darkColors(
        primary = colorResource(R.color.dark_colorPrimary),
        primaryVariant = colorResource(R.color.dark_colorPrimaryVariant),
        secondary = colorResource(R.color.dark_colorSecondary),
        secondaryVariant = Color.Unspecified,
        background = colorResource(R.color.dark_colorBackground),
        surface = colorResource(R.color.dark_colorSurface),
        onPrimary = colorResource(R.color.dark_colorOnPrimary),
        onSecondary = colorResource(R.color.dark_colorOnSecondary),
        onBackground = colorResource(R.color.dark_colorOnBackground),
        onSurface = colorResource(R.color.dark_colorOnSurface),
    )
}

val Colors.lightScrim: Color
    @Composable
    @ReadOnlyComposable
    get() = colorResource(R.color.colorLightScrim)

val Colors.darkScrim: Color
    @Composable
    @ReadOnlyComposable
    get() = colorResource(R.color.colorDarkScrim)
