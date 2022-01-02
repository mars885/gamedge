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

package com.paulrybitskyi.gamedge.feature.info.widgets.links

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.google.accompanist.flowlayout.FlowRow
import com.paulrybitskyi.gamedge.commons.ui.textSizeResource
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.widgets.model.GameInfoLinkModel

@Composable
internal fun GameLinks(
    links: List<GameInfoLinkModel>,
    onLinkClicked: (GameInfoLinkModel) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        backgroundColor = colorResource(R.color.game_links_card_background_color),
        elevation = dimensionResource(R.dimen.game_links_card_elevation),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.game_links_container_margin)),
        ) {
            Text(
                text = stringResource(R.string.game_links_title),
                modifier = Modifier.padding(
                    bottom = dimensionResource(R.dimen.game_links_title_margin),
                ),
                color = colorResource(R.color.game_links_title_text_color),
                fontSize = textSizeResource(R.dimen.game_links_title_text_size),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
            )

            FlowRow(
                mainAxisSpacing = dimensionResource(R.dimen.game_links_flow_row_horizontal_spacing),
                crossAxisSpacing = dimensionResource(R.dimen.game_links_flow_row_vertical_spacing),
            ) {
                for (link in links) {
                    GameLink(
                        link = link,
                        onLinkClicked = { onLinkClicked(link) },
                    )
                }
            }
        }
    }
}

@Composable
private fun GameLink(
    link: GameInfoLinkModel,
    onLinkClicked: () -> Unit,
) {
    Card(
        onClick = onLinkClicked,
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.chip_flow_default_item_background_corner_radius)
        ),
        backgroundColor = colorResource(R.color.game_links_chip_background_color),
        elevation = dimensionResource(R.dimen.chip_flow_item_background_shadow_dy),
        indication = rememberRipple(),
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = dimensionResource(R.dimen.game_links_chip_padding_vertical))
                .padding(
                    start = dimensionResource(R.dimen.game_links_chip_padding_start),
                    end = dimensionResource(R.dimen.game_links_chip_padding_end),
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(link.iconId),
                contentDescription = null,
                modifier = Modifier.size(
                    dimensionResource(R.dimen.chip_flow_default_item_icon_size)
                ),
                tint = colorResource(R.color.game_links_chip_content_color),
            )
            Text(
                text = link.text,
                modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.game_links_chip_text_padding_start),
                ),
                color = colorResource(R.color.game_links_chip_content_color),
                fontSize = textSizeResource(R.dimen.game_links_chip_text_size),
            )
        }
    }
}
