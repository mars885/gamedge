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

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mxalbert.zoomable.OverZoomConfig
import com.mxalbert.zoomable.Zoomable
import com.mxalbert.zoomable.rememberZoomableState
import com.paulrybitskyi.gamedge.commons.ui.images.CROSSFADE_ANIMATION_DURATION
import com.paulrybitskyi.gamedge.commons.ui.CommandsHandler
import com.paulrybitskyi.gamedge.commons.ui.RoutesHandler
import com.paulrybitskyi.gamedge.commons.ui.LocalNetworkStateProvider
import com.paulrybitskyi.gamedge.commons.ui.LocalTextSharer
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.theme.navBar
import com.paulrybitskyi.gamedge.commons.ui.theme.statusBar
import com.paulrybitskyi.gamedge.commons.ui.widgets.Info
import com.paulrybitskyi.gamedge.commons.ui.widgets.toolbars.Toolbar

private const val ZoomScaleMin = 0.5f
private const val ZoomScaleMax = 5f
private const val ZoomOverSnapScaleMin = 1f
private const val ZoomOverSnapScaleMax = 3f

@Composable
fun ImageViewer(onRoute: (Route) -> Unit) {
    ImageViewer(
        viewModel = hiltViewModel(),
        onRoute = onRoute,
    )
}

@Composable
private fun ImageViewer(
    viewModel: ImageViewerViewModel,
    onRoute: (Route) -> Unit,
) {
    val textSharer = LocalTextSharer.current
    val context = LocalContext.current

    CommandsHandler(viewModel = viewModel) { command ->
        when (command) {
            is ImageViewerCommand.ShareText -> {
                textSharer.share(context, command.text)
            }
        }
    }
    RoutesHandler(viewModel = viewModel, onRoute = onRoute)
    ImageViewer(
        uiState = viewModel.uiState.collectAsState().value,
        onBackPressed = viewModel::onBackPressed,
        onToolbarRightBtnClicked = viewModel::onToolbarRightButtonClicked,
        onImageChanged = viewModel::onImageChanged,
        onDismiss = viewModel::onBackPressed,
    )
}

@Composable
private fun ImageViewer(
    uiState: ImageViewerUiState,
    onBackPressed: () -> Unit,
    onToolbarRightBtnClicked: () -> Unit,
    onImageChanged: (imageIndex: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    StatusBarColorHandler()
    BackHandler(onBack = onBackPressed)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black,
        contentColor = Color.White,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Pager(
                uiState = uiState,
                modifier = Modifier.matchParentSize(),
                onImageChanged = onImageChanged,
                onDismiss = onDismiss,
            )

            Toolbar(
                title = uiState.toolbarTitle,
                modifier = Modifier.align(Alignment.TopCenter),
                contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.statusBars,
                ),
                backgroundColor = GamedgeTheme.colors.statusBar,
                contentColor = LocalContentColor.current,
                elevation = 0.dp,
                leftButtonIcon = painterResource(R.drawable.arrow_left),
                rightButtonIcon = painterResource(R.drawable.share_variant),
                onLeftButtonClick = onBackPressed,
                onRightButtonClick = onToolbarRightBtnClicked,
            )
        }
    }
}

@Composable
private fun StatusBarColorHandler() {
    val systemUiController = rememberSystemUiController()
    val defaultStatusBarColor = GamedgeTheme.colors.statusBar
    val defaultNavigationBarColor = GamedgeTheme.colors.navBar

    DisposableEffect(Unit) {
        // We want to make the system bars translucent when viewing images
        with(systemUiController) {
            // Making the status bar transparent causes it to use the color
            // of the toolbar (which uses the status bar color)
            setStatusBarColor(Color.Transparent)
            // We want the color of the navigation bar to be
            // the same as the color of the status bar
            setNavigationBarColor(defaultStatusBarColor)
        }

        onDispose {
            with(systemUiController) {
                setStatusBarColor(defaultStatusBarColor)
                setNavigationBarColor(defaultNavigationBarColor)
            }
        }
    }
}

@Composable
private fun Pager(
    uiState: ImageViewerUiState,
    modifier: Modifier,
    onImageChanged: (imageIndex: Int) -> Unit,
    onDismiss: () -> Unit,
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
        itemSpacing = GamedgeTheme.spaces.spacing_2_0,
    ) { pageIndex ->
        ImageItem(
            imageUrl = uiState.imageUrls[pageIndex],
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun ImageItem(
    imageUrl: String,
    onDismiss: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        var imageState by remember { mutableStateOf<State>(State.Empty) }
        val zoomableState = rememberZoomableState(
            minScale = ZoomScaleMin,
            maxScale = ZoomScaleMax,
            overZoomConfig = OverZoomConfig(
                minSnapScale = ZoomOverSnapScaleMin,
                maxSnapScale = ZoomOverSnapScaleMax,
            ),
        )

        if (imageState is State.Error) {
            Info(
                icon = painterResource(R.drawable.alert_circle_outline),
                title = stringResource(
                    if (!LocalNetworkStateProvider.current.isNetworkAvailable) {
                        R.string.error_no_network_message
                    } else {
                        R.string.error_unknown_message
                    }
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = GamedgeTheme.spaces.spacing_7_5),
            )
        }

        // For Zoomable to work, a couple of rules have to be followed:
        // - size(Size.ORIGINAL) has to be specified in the Coil request
        // - Modifier.aspectRatio() set
        Zoomable(
            state = zoomableState,
            dismissGestureEnabled = true,
            onDismiss = {
                onDismiss()
                true
            },
        ) {
            val aspectRatioModifier = if (imageState is State.Success) {
                val size = checkNotNull(imageState.painter).intrinsicSize
                Modifier.aspectRatio(size.width / size.height)
            } else {
                Modifier
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .size(Size.ORIGINAL)
                    .crossfade(CROSSFADE_ANIMATION_DURATION)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .then(aspectRatioModifier)
                    .fillMaxSize(),
                onState = { state ->
                    imageState = state
                },
            )
        }
    }
}

@Preview
@Composable
private fun ImageViewerPreview() {
    GamedgeTheme {
        ImageViewer(
            uiState = ImageViewerUiState(
                toolbarTitle = "Image",
                imageUrls = emptyList(),
                selectedImageUrlIndex = 0,
            ),
            onBackPressed = {},
            onToolbarRightBtnClicked = {},
            onImageChanged = {},
            onDismiss = {},
        )
    }
}
