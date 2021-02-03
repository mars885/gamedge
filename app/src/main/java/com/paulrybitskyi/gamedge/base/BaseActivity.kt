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

package com.paulrybitskyi.gamedge.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.paulrybitskyi.commons.ktx.showLongToast
import com.paulrybitskyi.commons.ktx.showShortToast
import com.paulrybitskyi.gamedge.base.events.Command
import com.paulrybitskyi.gamedge.base.events.Route
import com.paulrybitskyi.gamedge.base.events.commons.GeneralCommand
import kotlinx.coroutines.flow.collect

internal abstract class BaseActivity<
    VB : ViewBinding,
    VM : BaseViewModel
> : AppCompatActivity() {


    protected abstract val viewBinding: VB
    protected abstract val viewModel: VM


    // Cannot be made final due to Dagger Hilt
    override fun onCreate(savedInstanceState: Bundle?) {
        onPreInit()

        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        onInit()
        onPostInit()
    }


    @CallSuper
    protected open fun onPreInit() {
        // Stub
    }


    @CallSuper
    protected open fun onInit() {
        onBindViewModel()
    }


    @CallSuper
    protected open fun onBindViewModel() {
        bindViewModelCommands()
        bindViewModelRoutes()
    }


    private fun bindViewModelCommands() {
        lifecycleScope.launchWhenResumed {
            viewModel.commandFlow.collect(::onHandleCommand)
        }
    }


    private fun bindViewModelRoutes() {
        lifecycleScope.launchWhenResumed {
            viewModel.routeFlow.collect(::onRoute)
        }
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


}