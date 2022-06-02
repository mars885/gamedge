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

@file:Suppress("unused")

package com.paulrybitskyi.gamedge.commons.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.paulrybitskyi.gamedge.commons.ui.R

@Composable
fun colors(): Colors {
    return darkColors(
        primary = colorResource(R.color.colorPrimary),
        primaryVariant = colorResource(R.color.colorPrimaryVariant),
        secondary = colorResource(R.color.colorSecondary),
        secondaryVariant = Color.Unspecified,
        background = colorResource(R.color.colorBackground),
        surface = colorResource(R.color.colorSurface),
        onPrimary = colorResource(R.color.colorOnPrimary),
        onSecondary = colorResource(R.color.colorOnSecondary),
        onBackground = colorResource(R.color.colorOnBackground),
        onSurface = colorResource(R.color.colorOnSurface),
    )
}

val Colors.navBar: Color
    @Composable
    @ReadOnlyComposable
    get() = colorResource(R.color.colorNavigationBar)

val Colors.lightScrim: Color
    @Composable
    @ReadOnlyComposable
    get() = colorResource(R.color.colorLightScrim)

val Colors.darkScrim: Color
    @Composable
    @ReadOnlyComposable
    get() = colorResource(R.color.colorDarkScrim)
