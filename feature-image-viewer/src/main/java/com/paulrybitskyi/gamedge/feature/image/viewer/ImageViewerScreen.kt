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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State
import coil.size.Size
import com.mxalbert.zoomable.OverZoomConfig
import com.mxalbert.zoomable.Zoomable
import com.mxalbert.zoomable.rememberZoomableState
import com.paulrybitskyi.gamedge.common.ui.CommandsHandler
import com.paulrybitskyi.gamedge.common.ui.LocalNetworkStateProvider
import com.paulrybitskyi.gamedge.common.ui.LocalTextSharer
import com.paulrybitskyi.gamedge.common.ui.RoutesHandler
import com.paulrybitskyi.gamedge.common.ui.base.events.Route
import com.paulrybitskyi.gamedge.common.ui.findWindow
import com.paulrybitskyi.gamedge.common.ui.images.defaultImageRequest
import com.paulrybitskyi.gamedge.common.ui.rememberWindowInsetsController
import com.paulrybitskyi.gamedge.common.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.common.ui.theme.darkScrim
import com.paulrybitskyi.gamedge.common.ui.widgets.Info
import com.paulrybitskyi.gamedge.common.ui.widgets.toolbars.Toolbar
import com.paulrybitskyi.gamedge.core.R as CoreR

private const val ZoomScaleMin = 0.5f
private const val ZoomScaleMax = 5f
private const val ZoomScaleInitial = 1f
private const val ZoomOverSnapScaleMin = 1f
private const val ZoomOverSnapScaleMax = 3f

@Composable
fun ImageViewerScreen(onRoute: (Route) -> Unit) {
    ImageViewerScreen(
        viewModel = hiltViewModel(),
        onRoute = onRoute,
    )
}

@Composable
private fun ImageViewerScreen(
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
    ImageViewerScreen(
        uiState = viewModel.uiState.collectAsState().value,
        onBackPressed = viewModel::onBackPressed,
        onToolbarRightBtnClicked = viewModel::onToolbarRightButtonClicked,
        onImageChanged = viewModel::onImageChanged,
        onDismiss = viewModel::onBackPressed,
    )
}

@Composable
private fun ImageViewerScreen(
    uiState: ImageViewerUiState,
    onBackPressed: () -> Unit,
    onToolbarRightBtnClicked: () -> Unit,
    onImageChanged: (imageIndex: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val toolbarBackgroundColor = GamedgeTheme.colors.darkScrim

    SystemBarsColorHandler(navBarColor = toolbarBackgroundColor)
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
                backgroundColor = toolbarBackgroundColor,
                contentColor = LocalContentColor.current,
                elevation = 0.dp,
                leftButtonIcon = painterResource(CoreR.drawable.arrow_left),
                rightButtonIcon = painterResource(CoreR.drawable.share_variant),
                onLeftButtonClick = onBackPressed,
                onRightButtonClick = onToolbarRightBtnClicked,
            )
        }
    }
}

@Composable
private fun SystemBarsColorHandler(navBarColor: Color) {
    val window = findWindow()
    val windowsInsetsController = rememberWindowInsetsController(window = window)

    DisposableEffect(windowsInsetsController, window, navBarColor) {
        if (windowsInsetsController == null || window == null) {
            return@DisposableEffect onDispose {}
        }

        val previousStatusBarHasDarkIcons = windowsInsetsController.isAppearanceLightStatusBars
        val previousNavBarHasDarkButtons = windowsInsetsController.isAppearanceLightNavigationBars
        val previousStatusBarColor = window.statusBarColor
        val previousNavBarColor = window.navigationBarColor

        with(windowsInsetsController) {
            // Make the status bar's icons always white on this screen
            isAppearanceLightStatusBars = false
            // Make the nav bar's buttons always white on this screen
            isAppearanceLightNavigationBars = false
        }

        with(window) {
            // Make the status bar transparent for it to use the color of the toolbar
            statusBarColor = Color.Transparent.toArgb()
            // Make the nav bar's color the same as the toolbar's color
            navigationBarColor = navBarColor.toArgb()
        }

        onDispose {
            with(windowsInsetsController) {
                isAppearanceLightStatusBars = previousStatusBarHasDarkIcons
                isAppearanceLightNavigationBars = previousNavBarHasDarkButtons
            }

            with(window) {
                statusBarColor = previousStatusBarColor
                navigationBarColor = previousNavBarColor
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
    val pagerState = rememberPagerState(
        initialPage = uiState.selectedImageUrlIndex,
        pageCount = uiState.imageUrls::size,
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { pageIndex -> onImageChanged(pageIndex) }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        beyondBoundsPageCount = 1,
        pageSpacing = GamedgeTheme.spaces.spacing_2_0,
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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        var imageState by remember { mutableStateOf<State>(State.Empty) }
        val zoomableState = rememberZoomableState(
            minScale = ZoomScaleMin,
            maxScale = ZoomScaleMax,
            overZoomConfig = OverZoomConfig(
                minSnapScale = ZoomOverSnapScaleMin,
                maxSnapScale = ZoomOverSnapScaleMax,
            ),
            initialScale = ZoomScaleInitial,
        )

        if (imageState is State.Error) {
            Info(
                icon = painterResource(CoreR.drawable.alert_circle_outline),
                title = stringResource(
                    if (!LocalNetworkStateProvider.current.isNetworkAvailable) {
                        CoreR.string.error_no_network_message
                    } else {
                        CoreR.string.error_unknown_message
                    },
                ),
                modifier = Modifier.padding(horizontal = GamedgeTheme.spaces.spacing_7_5),
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
                model = defaultImageRequest(imageUrl) {
                    size(Size.ORIGINAL)
                },
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

@PreviewLightDark
@Composable
private fun ImageViewerScreenPreview() {
    GamedgeTheme {
        ImageViewerScreen(
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
