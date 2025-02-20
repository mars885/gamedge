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

package com.paulrybitskyi.gamedge.common.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.paulrybitskyi.commons.ktx.showLongToast
import com.paulrybitskyi.commons.ktx.showShortToast
import com.paulrybitskyi.gamedge.common.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.common.ui.base.events.Command
import com.paulrybitskyi.gamedge.common.ui.base.events.Direction
import com.paulrybitskyi.gamedge.common.ui.base.events.common.GeneralCommand

@Composable
fun CommandsHandler(
    viewModel: BaseViewModel,
    onCommand: ((Command) -> Unit)? = null,
) {
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.commandFlow.collect { command ->
            when (command) {
                is GeneralCommand.ShowShortToast -> context.showShortToast(command.message)
                is GeneralCommand.ShowLongToast -> context.showLongToast(command.message)
                else -> onCommand?.invoke(command)
            }
        }
    }
}

@Composable
fun DirectionsHandler(
    viewModel: BaseViewModel,
    onNavigate: (Direction) -> Unit,
) {
    LaunchedEffect(viewModel) {
        viewModel.directionFlow
            .collect(onNavigate)
    }
}
