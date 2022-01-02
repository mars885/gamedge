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

import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.ktx.addOnBackPressCallback
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.navigationBarColor
import com.paulrybitskyi.commons.ktx.statusBarColor
import com.paulrybitskyi.commons.ktx.window
import com.paulrybitskyi.gamedge.commons.ui.base.BaseComposeFragment
import com.paulrybitskyi.gamedge.commons.ui.base.events.Command
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
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

    private var originalStatusBarColor: Int = 0

    @Inject lateinit var networkStateProvider: NetworkStateProvider
    @Inject lateinit var textSharer: TextSharer

    override fun getContent() = @Composable {
        ImageViewer(
            uiState = viewModel.uiState.collectAsState().value,
            networkStateProvider = networkStateProvider,
            onToolbarLeftBtnClicked = ::onBackPressed,
            onToolbarRightBtnClicked = viewModel::onToolbarRightButtonClicked,
            onImageChanged = viewModel::onImageChanged,
        )
    }

    override fun onInit() {
        super.onInit()

        initSystemWindows()
        initOnBackPress()
    }

    private fun initSystemWindows() {
        originalStatusBarColor = window.statusBarColor

        // Still using this flag even in newer version since
        // window.statusBarColor with translucent color does not
        // seem to work for API 30+
        @Suppress("DEPRECATION")
        window.clearFlags(FLAG_TRANSLUCENT_STATUS)

        statusBarColor = getColor(R.color.image_viewer_bar_background_color)
        navigationBarColor = getColor(R.color.image_viewer_bar_background_color)
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
            is ImageViewerCommand.ResetSystemWindows -> resetSystemWindows()
        }
    }

    private fun shareText(text: String) {
        textSharer.share(requireActivity(), text)
    }

    private fun resetSystemWindows() {
        @Suppress("DEPRECATION")
        window.addFlags(FLAG_TRANSLUCENT_STATUS)

        statusBarColor = originalStatusBarColor
        navigationBarColor = getColor(R.color.colorNavigationBar)
    }

    override fun onRoute(route: Route) {
        super.onRoute(route)

        when (route) {
            is ImageViewerRoute.Back -> navigator.goBack()
        }
    }
}
