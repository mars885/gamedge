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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.paulrybitskyi.gamedge.commons.ui.CROSSFADE_ANIMATION_DURATION
import com.paulrybitskyi.gamedge.commons.ui.textSizeResource
import com.paulrybitskyi.gamedge.feature.info.R

@Composable
internal fun GameVideos(
    videos: List<GameInfoVideoModel>,
    onVideClicked: (GameInfoVideoModel) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        backgroundColor = colorResource(R.color.game_videos_card_background_color),
        elevation = dimensionResource(R.dimen.game_videos_card_elevation),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.game_videos_card_vertical_padding)),
        ) {
            Text(
                text = stringResource(R.string.game_videos_title),
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.game_videos_title_padding_bottom))
                    .padding(horizontal = dimensionResource(R.dimen.game_videos_title_padding)),
                color = colorResource(R.color.game_videos_title_text_color),
                fontSize = textSizeResource(R.dimen.game_videos_title_text_size),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
            )

            LazyRow(
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(R.dimen.game_videos_horizontal_content_padding),
                ),
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.game_videos_horizontal_arrangement),
                )
            ) {
                items(videos) { video ->
                    GameVideo(
                        thumbnailUrl = video.thumbnailUrl,
                        title = video.title,
                        thumbnailHeight = dimensionResource(R.dimen.game_videos_item_thumbnail_height),
                        modifier = Modifier.width(dimensionResource(R.dimen.game_videos_item_width)),
                        onVideoClicked = { onVideClicked(video) },
                    )
                }
            }
        }
    }
}

@Composable
private fun GameVideo(
    thumbnailUrl: String?,
    title: String,
    thumbnailHeight: Dp,
    modifier: Modifier,
    onVideoClicked: () -> Unit,
) {
    Card(
        onClick = onVideoClicked,
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.game_video_card_corner_radius)),
        backgroundColor = colorResource(R.color.game_video_card_background_color),
        elevation = dimensionResource(R.dimen.game_video_card_elevation),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(thumbnailHeight)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(thumbnailUrl)
                            .fallback(R.drawable.game_landscape_placeholder)
                            .placeholder(R.drawable.game_landscape_placeholder)
                            .error(R.drawable.game_landscape_placeholder)
                            .crossfade(CROSSFADE_ANIMATION_DURATION)
                            .build(),
                    ),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop,
                )
                Icon(
                    painter = painterResource(R.drawable.play),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(dimensionResource(R.dimen.game_video_play_btn_size))
                        .border(
                            width = dimensionResource(R.dimen.game_video_play_btn_background_stroke_width),
                            color = colorResource(R.color.game_video_play_btn_background_stroke_color),
                            shape = CircleShape,
                        )
                        .background(
                            color = colorResource(R.color.game_video_play_btn_background_solid_color),
                            shape = CircleShape,
                        )
                        .padding(dimensionResource(R.dimen.game_video_play_btn_padding)),
                    tint = colorResource(R.color.game_video_play_btn_color),
                )
            }

            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorResource(R.color.game_video_title_background_color))
                    .padding(dimensionResource(R.dimen.game_video_title_padding)),
                color = colorResource(R.color.game_video_title_text_color),
                fontSize = textSizeResource(R.dimen.game_video_title_text_size),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}

@Preview
@Composable
internal fun GameVideosPreview() {
    GameVideos(
        videos = listOf(
            GameInfoVideoModel(
                thumbnailUrl = "",
                videoUrl = "",
                title = "Announcement Trailer",
            ),
            GameInfoVideoModel(
                thumbnailUrl = "",
                videoUrl = "",
                title = "Gameplay Trailer",
            ),
        ),
        onVideClicked = {},
    )
}
