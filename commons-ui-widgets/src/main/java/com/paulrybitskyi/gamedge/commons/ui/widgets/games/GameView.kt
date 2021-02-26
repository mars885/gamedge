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

package com.paulrybitskyi.gamedge.commons.ui.widgets.games

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getDimension
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.ktx.onClick
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.databinding.ViewGameBinding

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    private val binding = ViewGameBinding.inflate(context.layoutInflater, this)

    private var isDeveloperNameVisible: Boolean
        set(value) { binding.developerNameTv.isVisible = value }
        get() = binding.developerNameTv.isVisible

    private var isDescriptionVisible: Boolean
        set(value) { binding.descriptionTv.isVisible = value }
        get() = binding.descriptionTv.isVisible

    var coverImageUrl: String? = null
        set(value) {
            field = value
            binding.coverView.imageUrl = value
        }

    var name: CharSequence
        set(value) { binding.nameTv.text = value }
        get() = binding.nameTv.text

    var releaseDate: CharSequence
        set(value) { binding.releaseDateTv.text = value }
        get() = binding.releaseDateTv.text

    var developerName: CharSequence?
        set(value) {
            binding.developerNameTv.text = value
            isDeveloperNameVisible = (value != null)
        }
        get() = binding.developerNameTv.text

    var description: CharSequence?
        set(value) {
            binding.descriptionTv.text = value
            isDescriptionVisible = (value != null)
        }
        get() = binding.descriptionTv.text

    var onGameClickListener: (() -> Unit)? = null


    init {
        initCard()
        initCoverView()
        initDescription()
    }


    private fun initCard() {
        setCardBackgroundColor(getColor(R.color.game_card_background_color))
        cardElevation = getDimension(R.dimen.game_card_elevation)
        onClick { onGameClickListener?.invoke() }
    }


    private fun initCoverView() {
        binding.coverView.isTitleVisible = false
    }


    private fun initDescription() = with(binding.descriptionTv) {
        addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            maxLines = (height / lineHeight)
            ellipsize = TextUtils.TruncateAt.END

            // Resetting the text because maxLines and ellipsize properties
            // does not seem to apply the changes.
            description = description
        }
    }


}