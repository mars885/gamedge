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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.widgets.GamedgeCard
import com.paulrybitskyi.gamedge.domain.games.entities.WebsiteCategory
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.widgets.utils.GameInfoSection
import java.util.Locale

@Composable
internal fun GameInfoLinks(
    links: List<GameInfoLinkModel>,
    onLinkClicked: (GameInfoLinkModel) -> Unit,
) {
    GameInfoSection(title = stringResource(R.string.game_links_title)) { paddingValues ->
        FlowRow(
            modifier = Modifier.padding(paddingValues),
            mainAxisSpacing = GamedgeTheme.spaces.spacing_2_0,
            crossAxisSpacing = GamedgeTheme.spaces.spacing_3_0,
        ) {
            for (link in links) {
                Link(
                    link = link,
                    onLinkClicked = { onLinkClicked(link) },
                )
            }
        }
    }
}

@Composable
private fun Link(
    link: GameInfoLinkModel,
    onLinkClicked: () -> Unit,
) {
    CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
        GamedgeCard(
            onClick = onLinkClicked,
            shape = GamedgeTheme.shapes.small,
            backgroundColor = GamedgeTheme.colors.primaryVariant,
            contentColor = GamedgeTheme.colors.onSurface,
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = GamedgeTheme.spaces.spacing_1_5)
                    .padding(
                        start = GamedgeTheme.spaces.spacing_2_5,
                        end = GamedgeTheme.spaces.spacing_3_0,
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(link.iconId),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = link.text,
                    modifier = Modifier.padding(start = GamedgeTheme.spaces.spacing_1_5),
                    style = GamedgeTheme.typography.button,
                )
            }
        }
    }
}

@Preview
@Composable
internal fun GameInfoLinksPreview() {
    val links = WebsiteCategory.values()
        .filterNot { it == WebsiteCategory.UNKNOWN }
        .mapIndexed { index, websiteCategory ->
            GameInfoLinkModel(
                id = index,
                text = websiteCategory.name
                    .replace("_", " ")
                    .lowercase()
                    .replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    },
                iconId = R.drawable.web,
                url = "url$index",
            )
    }

    GamedgeTheme {
        GameInfoLinks(
            links = links,
            onLinkClicked = {},
        )
    }
}
