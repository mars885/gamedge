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
import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.navigationBarColor
import com.paulrybitskyi.commons.ktx.statusBarColor
import com.paulrybitskyi.commons.utils.viewBinding
import com.paulrybitskyi.gamedge.commons.ui.addOnBackPressCallback
import com.paulrybitskyi.gamedge.commons.ui.base.BaseFragment
import com.paulrybitskyi.gamedge.commons.ui.base.events.Command
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.commons.ui.extensions.window
import com.paulrybitskyi.gamedge.commons.ui.observeIn
import com.paulrybitskyi.gamedge.core.sharers.TextSharer
import com.paulrybitskyi.gamedge.feature.image.viewer.databinding.FragmentImageViewerBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
internal class ImageViewerFragment : BaseFragment<
    FragmentImageViewerBinding,
    ImageViewerViewModel,
    ImageViewerNavigator
>(R.layout.fragment_image_viewer) {


    override val viewBinding by viewBinding(FragmentImageViewerBinding::bind)
    override val viewModel by viewModels<ImageViewerViewModel>()

    private var originalStatusBarColor: Int = 0

    @Inject lateinit var textSharer: TextSharer


    override fun onInit() {
        super.onInit()

        initSystemWindows()
        initOnBackPress()
        initImageViewerView()
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


    private fun initImageViewerView() = with(viewBinding.imageViewerView) {
        onToolbarLeftBtnClicked = ::onBackPressed
        onToolbarRightBtnClicked = viewModel::onToolbarRightButtonClicked
        onPageChanged = viewModel::onPageChanged
    }


    override fun onBindViewModel() {
        super.onBindViewModel()

        observeSelectedPosition()
        observeImageUrls()
        observeToolbarTitle()
    }


    private fun observeSelectedPosition() {
        viewModel.selectedPosition
            .onEach { viewBinding.imageViewerView.selectedPosition = it }
            .observeIn(this)
    }


    private fun observeImageUrls() {
        viewModel.imageUrls
            .onEach { viewBinding.imageViewerView.imageUrls = it }
            .observeIn(this)
    }


    private fun observeToolbarTitle() {
        viewModel.toolbarTitle
            .onEach { viewBinding.imageViewerView.toolbarTitle = it }
            .observeIn(this)
    }


    private fun onBackPressed() {
        if(viewBinding.imageViewerView.isCurrentImageScaled()) {
            viewBinding.imageViewerView.resetCurrentImageScale()
        } else {
            viewModel.onBackPressed()
        }
    }


    override fun onHandleCommand(command: Command) {
        super.onHandleCommand(command)

        when(command) {
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

        when(route) {
            is ImageViewerRoute.Back -> navigator.goBack()
        }
    }


}