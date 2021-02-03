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

package com.paulrybitskyi.gamedge.commons.ui.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimension
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.gamedge.commons.ui.widgets.databinding.ViewGameDetailsBinding

class GameDetailsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    private val binding = ViewGameDetailsBinding.inflate(context.layoutInflater, this)

    var genresText: CharSequence?
        set(value) {
            binding.genresContainer.isVisible = (value != null)
            binding.genresTv.text = value
        }
        get() = binding.genresTv.text

    var platformsText: CharSequence?
        set(value) {
            binding.platformsContainer.isVisible = (value != null)
            binding.platformsTv.text = value
        }
        get() = binding.platformsTv.text

    var modesText: CharSequence?
        set(value) {
            binding.modesContainer.isVisible = (value != null)
            binding.modesTv.text = value
        }
        get() = binding.modesTv.text

    var playerPerspectivesText: CharSequence?
        set(value) {
            binding.playerPerspectivesContainer.isVisible = (value != null)
            binding.playerPerspectivesTv.text = value
        }
        get() = binding.playerPerspectivesTv.text

    var themesText: CharSequence?
        set(value) {
            binding.themesContainer.isVisible = (value != null)
            binding.themesTv.text = value
        }
        get() = binding.themesTv.text


    init {
        initCard()
    }


    private fun initCard() {
        setBackgroundColor(getColor(R.color.game_details_card_background_color))
        cardElevation = getDimension(R.dimen.game_details_card_elevation)
    }


}