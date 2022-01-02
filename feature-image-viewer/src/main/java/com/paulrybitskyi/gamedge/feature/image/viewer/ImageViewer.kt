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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.paulrybitskyi.gamedge.commons.ui.CROSSFADE_ANIMATION_DURATION
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.commons.ui.widgets.Toolbar
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import kotlinx.coroutines.flow.collect

@Composable
internal fun ImageViewer(
    uiState: ImageViewerUiState,
    networkStateProvider: NetworkStateProvider,
    onToolbarLeftBtnClicked: () -> Unit,
    onToolbarRightBtnClicked: () -> Unit,
    onImageChanged: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.image_viewer_background_color)),
    ) {
        ImageViewerPager(
            uiState = uiState,
            networkStateProvider = networkStateProvider,
            modifier = Modifier.matchParentSize(),
            onImageChanged = onImageChanged,
        )

        Toolbar(
            title = uiState.toolbarTitle,
            modifier = Modifier
                .statusBarsPadding()
                .align(Alignment.TopCenter),
            backgroundColor = colorResource(R.color.image_viewer_bar_background_color),
            leftButtonIcon = painterResource(R.drawable.arrow_left),
            rightButtonIcon = painterResource(R.drawable.share_variant),
            onLeftButtonClick = onToolbarLeftBtnClicked,
            onRightButtonClick = onToolbarRightBtnClicked,
        )
    }
}

@Composable
private fun ImageViewerPager(
    uiState: ImageViewerUiState,
    networkStateProvider: NetworkStateProvider,
    modifier: Modifier,
    onImageChanged: (Int) -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = uiState.selectedImageUrlIndex)

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { pageIndex -> onImageChanged(pageIndex) }
    }

    HorizontalPager(
        count = uiState.imageUrls.size,
        modifier = modifier,
        state = pagerState,
        itemSpacing = dimensionResource(R.dimen.image_viewer_pager_item_spacing),
    ) { pageIndex ->
        ImageViewerItem(
            imageUrl = uiState.imageUrls[pageIndex],
            networkStateProvider = networkStateProvider,
        )
    }
}

@Composable
private fun ImageViewerItem(
    imageUrl: String,
    networkStateProvider: NetworkStateProvider,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val imagePainter = rememberImagePainter(
            data = imageUrl,
            builder = {
                crossfade(CROSSFADE_ANIMATION_DURATION)
            },
        )

        if (imagePainter.state is ImagePainter.State.Error) {
            Info(
                icon = painterResource(R.drawable.alert_circle_outline),
                title = stringResource(
                    if (!networkStateProvider.isNetworkAvailable) {
                        R.string.error_no_network_message
                    } else {
                        R.string.error_unknown_message
                    }
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = dimensionResource(R.dimen.image_viewer_item_info_view_horizontal_padding)),
                iconColor = colorResource(R.color.colorInfoView),
                titleTextColor = colorResource(R.color.colorInfoView),
            )
        }

        Image(
            painter = imagePainter,
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Fit,
        )
    }
}
