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

package com.paulrybitskyi.gamedge.common.ui.base

import androidx.lifecycle.ViewModel
import com.paulrybitskyi.gamedge.common.ui.base.events.Command
import com.paulrybitskyi.gamedge.common.ui.base.events.Route
import com.paulrybitskyi.gamedge.core.markers.Loggable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel : ViewModel(), Loggable {

    override val logTag: String = javaClass.simpleName

    private val commandChannel = Channel<Command>(Channel.BUFFERED)
    private val routeChannel = Channel<Route>(Channel.BUFFERED)

    val commandFlow: Flow<Command> = commandChannel.receiveAsFlow()
    val routeFlow: Flow<Route> = routeChannel.receiveAsFlow()

    protected fun dispatchCommand(command: Command) {
        commandChannel.trySend(command)
    }

    protected fun route(route: Route) {
        routeChannel.trySend(route)
    }
}
