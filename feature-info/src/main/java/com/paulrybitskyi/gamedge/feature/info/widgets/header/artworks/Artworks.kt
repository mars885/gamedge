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

package com.paulrybitskyi.gamedge.feature.info.widgets.header.artworks

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.paulrybitskyi.gamedge.commons.ui.clickable
import com.paulrybitskyi.gamedge.commons.ui.images.defaultImageRequest
import com.paulrybitskyi.gamedge.commons.ui.images.secondaryImage
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.feature.info.R

@Composable
internal fun Artworks(
    artworks: List<GameInfoArtworkModel>,
    @Suppress("UnusedPrivateMember")
    isScrollingEnabled: Boolean, // todo
    modifier: Modifier,
    onArtworkChanged: (artworkIndex: Int) -> Unit,
    onArtworkClicked: (artworkIndex: Int) -> Unit,
) {
    val pagerState = rememberPagerState()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page -> onArtworkChanged(page) }
    }

    HorizontalPager(
        count = artworks.size,
        modifier = modifier,
        state = pagerState,
        key = { page -> artworks[page].id },
    ) { page ->
        Artwork(
            artwork = artworks[page],
            onArtworkClicked = { onArtworkClicked(page) },
        )
    }
}

@Composable
private fun Artwork(
    artwork: GameInfoArtworkModel,
    onArtworkClicked: () -> Unit,
) {
    val data = when (artwork) {
        is GameInfoArtworkModel.DefaultImage -> R.drawable.game_background_placeholder
        is GameInfoArtworkModel.UrlImage -> artwork.url
    }

    AsyncImage(
        model = defaultImageRequest(data) {
            secondaryImage(R.drawable.game_background_placeholder)
        },
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                onClick = onArtworkClicked,
            ),
        contentScale = ContentScale.Crop,
    )
}

@Preview(heightDp = 240)
@Composable
private fun ArtworksPreview() {
    GamedgeTheme {
        Artworks(
            artworks = listOf(
                GameInfoArtworkModel.DefaultImage,
            ),
            isScrollingEnabled = true,
            modifier = Modifier,
            onArtworkChanged = {},
            onArtworkClicked = {},
        )
    }
}
