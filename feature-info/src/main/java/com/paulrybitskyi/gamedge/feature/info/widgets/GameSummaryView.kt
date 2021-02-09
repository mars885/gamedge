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

package com.paulrybitskyi.gamedge.feature.info.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.doOnLayout
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimension
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.ktx.onClick
import com.paulrybitskyi.commons.ktx.views.isTextEllipsized
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.gamedge.feature.info.databinding.ViewGameSummaryBinding

internal class GameSummaryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    var contentText: CharSequence
        set(value) { onContentTextChanged(value) }
        get() = binding.contentTv.text


    private val binding = ViewGameSummaryBinding.inflate(context.layoutInflater, this)


    init {
        initCard()
    }


    private fun initCard() {
        setBackgroundColor(getColor(R.color.game_summary_card_background_color))
        cardElevation = getDimension(R.dimen.game_summary_card_elevation)
        onClick { binding.contentTv.toggle() }
    }


    private fun onContentTextChanged(newText: CharSequence) = with(binding.contentTv) {
        text = newText
        doOnLayout { this@GameSummaryView.isClickable = isTextEllipsized() }
    }


}