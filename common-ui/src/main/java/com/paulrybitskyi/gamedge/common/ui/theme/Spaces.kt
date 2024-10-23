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

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Suppress("ConstructorParameterNaming", "VariableNaming")
@Stable
class Spaces(
    spacing_0_5: Dp = 2.dp,
    spacing_1_0: Dp = 4.dp,
    spacing_1_5: Dp = 6.dp,
    spacing_2_0: Dp = 8.dp,
    spacing_2_5: Dp = 10.dp,
    spacing_3_0: Dp = 12.dp,
    spacing_3_5: Dp = 14.dp,
    spacing_4_0: Dp = 16.dp,
    spacing_4_5: Dp = 18.dp,
    spacing_5_0: Dp = 20.dp,
    spacing_5_5: Dp = 22.dp,
    spacing_6_0: Dp = 24.dp,
    spacing_6_5: Dp = 26.dp,
    spacing_7_0: Dp = 28.dp,
    spacing_7_5: Dp = 30.dp,
    spacing_8_0: Dp = 32.dp,
    spacing_8_5: Dp = 34.dp,
    spacing_9_0: Dp = 36.dp,
) {
    var spacing_0_5 by mutableStateOf(spacing_0_5)
        private set
    var spacing_1_0 by mutableStateOf(spacing_1_0)
        private set
    var spacing_1_5 by mutableStateOf(spacing_1_5)
        private set
    var spacing_2_0 by mutableStateOf(spacing_2_0)
        private set
    var spacing_2_5 by mutableStateOf(spacing_2_5)
        private set
    var spacing_3_0 by mutableStateOf(spacing_3_0)
        private set
    var spacing_3_5 by mutableStateOf(spacing_3_5)
        private set
    var spacing_4_0 by mutableStateOf(spacing_4_0)
        private set
    var spacing_4_5 by mutableStateOf(spacing_4_5)
        private set
    var spacing_5_0 by mutableStateOf(spacing_5_0)
        private set
    var spacing_5_5 by mutableStateOf(spacing_5_5)
        private set
    var spacing_6_0 by mutableStateOf(spacing_6_0)
        private set
    var spacing_6_5 by mutableStateOf(spacing_6_5)
        private set
    var spacing_7_0 by mutableStateOf(spacing_7_0)
        private set
    var spacing_7_5 by mutableStateOf(spacing_7_5)
        private set
    var spacing_8_0 by mutableStateOf(spacing_8_0)
        private set
    var spacing_8_5 by mutableStateOf(spacing_8_5)
        private set
    var spacing_9_0 by mutableStateOf(spacing_9_0)
        private set

    override fun toString(): String {
        return "Spaces(" +
            "spacing_0_5=$spacing_0_5, " +
            "spacing_1_0=$spacing_1_0, " +
            "spacing_1_5=$spacing_1_5, " +
            "spacing_2_0=$spacing_2_0, " +
            "spacing_2_5=$spacing_2_5, " +
            "spacing_3_0=$spacing_3_0, " +
            "spacing_3_5=$spacing_3_5, " +
            "spacing_4_0=$spacing_4_0, " +
            "spacing_4_5=$spacing_4_5, " +
            "spacing_5_0=$spacing_5_0, " +
            "spacing_5_5=$spacing_5_5, " +
            "spacing_6_0=$spacing_6_0, " +
            "spacing_6_5=$spacing_6_5, " +
            "spacing_7_0=$spacing_7_0, " +
            "spacing_7_5=$spacing_7_5, " +
            "spacing_8_0=$spacing_8_0, " +
            "spacing_8_5=$spacing_8_5, " +
            "spacing_9_0=$spacing_9_0" +
            ")"
    }
}

internal val LocalSpaces = staticCompositionLocalOf {
    Spaces()
}
