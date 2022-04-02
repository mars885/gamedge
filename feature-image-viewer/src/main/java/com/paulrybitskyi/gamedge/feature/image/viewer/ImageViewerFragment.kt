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

package com.paulrybitskyi.gamedge.feature.image.viewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.viewModels
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.paulrybitskyi.commons.ktx.addOnBackPressCallback
import com.paulrybitskyi.commons.ktx.window
import com.paulrybitskyi.gamedge.commons.ui.base.BaseComposeFragment
import com.paulrybitskyi.gamedge.commons.ui.base.events.Command
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.theme.darkScrim
import com.paulrybitskyi.gamedge.commons.ui.theme.navBar
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import com.paulrybitskyi.gamedge.core.sharers.TextSharer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class ImageViewerFragment : BaseComposeFragment<
    ImageViewerViewModel,
    ImageViewerNavigator
>() {

    override val viewModel by viewModels<ImageViewerViewModel>()

    @Inject lateinit var networkStateProvider: NetworkStateProvider
    @Inject lateinit var textSharer: TextSharer

    override fun getContent() = @Composable {
        ChangeStatusBarColor()
        ImageViewer(
            uiState = viewModel.uiState.collectAsState().value,
            networkStateProvider = networkStateProvider,
            onToolbarLeftBtnClicked = ::onBackPressed,
            onToolbarRightBtnClicked = viewModel::onToolbarRightButtonClicked,
            onImageChanged = viewModel::onImageChanged,
        )
    }

    @Composable
    private fun ChangeStatusBarColor() {
        val systemUiController = rememberSystemUiController()
        val defaultNavigationBarColor = GamedgeTheme.colors.navBar
        val systemBarColor = GamedgeTheme.colors.darkScrim
        var originalStatusBarColor by remember { mutableStateOf(0) }

        DisposableEffect(Unit) {
            originalStatusBarColor = window.statusBarColor
            systemUiController.setSystemBarsColor(systemBarColor)

            onDispose {
                with(systemUiController) {
                    setStatusBarColor(Color(originalStatusBarColor))
                    setNavigationBarColor(defaultNavigationBarColor)
                }
            }
        }
    }

    override fun onInit() {
        super.onInit()

        initOnBackPress()
    }

    private fun initOnBackPress() {
        addOnBackPressCallback { onBackPressed() }
    }

    private fun onBackPressed() {
/*        if (viewBinding.imageViewerView.isCurrentImageScaled()) {
            viewBinding.imageViewerView.resetCurrentImageScale()
        } else {
            viewModel.onBackPressed()
        }*/

        viewModel.onBackPressed()
    }

    override fun onHandleCommand(command: Command) {
        super.onHandleCommand(command)

        when (command) {
            is ImageViewerCommand.ShareText -> shareText(command.text)
        }
    }

    private fun shareText(text: String) {
        textSharer.share(requireActivity(), text)
    }

    override fun onRoute(route: Route) {
        super.onRoute(route)

        when (route) {
            is ImageViewerRoute.Back -> navigator.goBack()
        }
    }
}
