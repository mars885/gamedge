/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimension
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.databinding.ViewGameLinksBinding

internal class GameLinksView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding = ViewGameLinksBinding.inflate(context.layoutInflater, this)

    var links by observeChanges<List<GameLinkItemModel>>(emptyList()) { _, newItems ->
        binding.linksCfv.items = newItems.mapToChipFlowItems()
    }

    var onLinkClicked: ((GameLinkItemModel) -> Unit)? = null

    init {
        initCard()
        initChipFlowView()
    }

    private fun initCard() {
        setBackgroundColor(getColor(R.color.game_links_card_background_color))
        cardElevation = getDimension(R.dimen.game_links_card_elevation)
    }

    private fun initChipFlowView() = with(binding.linksCfv) {
        itemBackgroundColor = getColor(R.color.game_links_chip_background_color)
        itemIconColor = getColor(R.color.game_links_chip_content_color)
        itemTextAppearanceResource = R.style.Gamedge_GameInfo_Links_ChipFlow_TextAppearance
        onItemClicked = { onLinkClicked?.invoke(it.mapToLinkItemModel()) }
    }
}
