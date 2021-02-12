/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.commons.ui.base

import androidx.lifecycle.ViewModel
import com.paulrybitskyi.gamedge.commons.ui.base.events.Command
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.core.markers.Loggable
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

abstract class BaseViewModel : ViewModel(), Loggable {


    override val logTag: String = javaClass.simpleName

    private val _commandChannel = BroadcastChannel<Command>(Channel.BUFFERED)
    private val _routeChannel = BroadcastChannel<Route>(Channel.BUFFERED)

    val commandFlow: Flow<Command> =_commandChannel.asFlow()
    val routeFlow: Flow<Route> = _routeChannel.asFlow()


    protected fun dispatchCommand(command: Command) {
        _commandChannel.offer(command)
    }


    protected fun route(route: Route) {
        _routeChannel.offer(route)
    }


}