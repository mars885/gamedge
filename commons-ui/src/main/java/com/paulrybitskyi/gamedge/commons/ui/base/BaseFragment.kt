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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.paulrybitskyi.commons.ktx.showLongToast
import com.paulrybitskyi.commons.ktx.showShortToast
import com.paulrybitskyi.gamedge.commons.ui.base.events.Command
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.commons.ui.base.navigation.Navigator
import com.paulrybitskyi.gamedge.commons.ui.observeIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

abstract class BaseFragment<
    VB : ViewBinding,
    VM : BaseViewModel,
    NA : Navigator
>(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {


    private var isViewCreated = false

    protected abstract val viewBinding: VB
    protected abstract val viewModel: VM

    @Inject lateinit var navigator: NA


    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Prevent the view from recreation until onDestroy is called
        return if(isViewCreated) {
            viewBinding.root
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }


    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wasViewCreated = isViewCreated
        isViewCreated = true

        if(!wasViewCreated) {
            onPreInit()
            onInit()
            onPostInit()
        }

        onBindViewModel()

        if(!wasViewCreated) {
            savedInstanceState?.let(::onRestoreState)
        }
    }


    @CallSuper
    protected open fun onPreInit() {
        // Stub
    }


    @CallSuper
    protected open fun onInit() {
        // Stub
    }


    @CallSuper
    protected open fun onPostInit() {
        loadData()
    }


    private fun loadData() {
        lifecycleScope.launchWhenResumed {
            onLoadData()
        }
    }


    protected open fun onLoadData() {
        // Stub
    }


    @CallSuper
    protected open fun onBindViewModel() {
        bindViewModelCommands()
        bindViewModelRoutes()
    }


    private fun bindViewModelCommands() {
        viewModel.commandFlow
            .onEach(::onHandleCommand)
            .observeIn(this)
    }


    private fun bindViewModelRoutes() {
        viewModel.routeFlow
            .onEach(::onRoute)
            .observeIn(this)
    }


    @CallSuper
    protected open fun onHandleCommand(command: Command) {
        when(command) {
            is GeneralCommand.ShowShortToast -> showShortToast(command.message)
            is GeneralCommand.ShowLongToast -> showLongToast(command.message)
        }
    }


    @CallSuper
    protected open fun onRoute(route: Route) {
        // Stub
    }


    @CallSuper
    protected open fun onRestoreState(state: Bundle) {
        // Stub
    }


    final override fun onSaveInstanceState(state: Bundle) {
        onSaveState(state)

        super.onSaveInstanceState(state)
    }


    @CallSuper
    protected open fun onSaveState(state: Bundle) {
        // Stub
    }


    override fun onDestroy() {
        super.onDestroy()

        isViewCreated = false
    }


}