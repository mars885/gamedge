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

package com.paulrybitskyi.gamedge.feature.info.widgets.videos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.paulrybitskyi.gamedge.commons.ui.images.defaultImageRequest
import com.paulrybitskyi.gamedge.commons.ui.images.secondaryImage
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.theme.darkScrim
import com.paulrybitskyi.gamedge.commons.ui.widgets.GamedgeCard
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.widgets.utils.GameInfoSectionWithInnerList

@Composable
internal fun GameInfoVideos(
    videos: List<GameInfoVideoModel>,
    onVideClicked: (GameInfoVideoModel) -> Unit,
) {
    GameInfoSectionWithInnerList(title = stringResource(R.string.game_videos_title)) {
        items(items = videos, key = GameInfoVideoModel::id) { video ->
            Video(
                video = video,
                thumbnailHeight = 150.dp,
                modifier = Modifier.width(268.dp),
                onVideoClicked = { onVideClicked(video) },
            )
        }
    }
}

@Composable
private fun Video(
    video: GameInfoVideoModel,
    thumbnailHeight: Dp,
    modifier: Modifier,
    onVideoClicked: () -> Unit,
) {
    GamedgeCard(
        onClick = onVideoClicked,
        modifier = modifier,
        shape = GamedgeTheme.shapes.medium,
        backgroundColor = Color.Transparent,
        contentColor = GamedgeTheme.colors.onPrimary,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(thumbnailHeight),
            ) {
                AsyncImage(
                    model = defaultImageRequest(video.thumbnailUrl) {
                        secondaryImage(R.drawable.game_landscape_placeholder)
                    },
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                Icon(
                    painter = painterResource(R.drawable.play),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(45.dp)
                        .border(
                            width = 2.dp,
                            color = LocalContentColor.current,
                            shape = CircleShape,
                        )
                        .background(
                            color = GamedgeTheme.colors.darkScrim,
                            shape = CircleShape,
                        )
                        .padding(GamedgeTheme.spaces.spacing_2_0),
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = GamedgeTheme.colors.primaryVariant,
                contentColor = GamedgeTheme.colors.onSurface,
            ) {
                Text(
                    text = video.title,
                    modifier = Modifier.padding(GamedgeTheme.spaces.spacing_2_5),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = GamedgeTheme.typography.caption,
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameInfoVideosPreview() {
    GamedgeTheme {
        GameInfoVideos(
            videos = listOf(
                GameInfoVideoModel(
                    id = "1",
                    thumbnailUrl = "",
                    videoUrl = "",
                    title = "Announcement Trailer",
                ),
                GameInfoVideoModel(
                    id = "2",
                    thumbnailUrl = "",
                    videoUrl = "",
                    title = "Gameplay Trailer",
                ),
            ),
            onVideClicked = {},
        )
    }
}
