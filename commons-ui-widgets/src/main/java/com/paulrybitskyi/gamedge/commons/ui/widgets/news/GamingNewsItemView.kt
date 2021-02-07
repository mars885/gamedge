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

package com.paulrybitskyi.gamedge.commons.ui.widgets.news

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView
import com.paulrybitskyi.commons.ktx.*
import com.paulrybitskyi.commons.utils.observeChanges
import com.paulrybitskyi.gamedge.commons.ui.widgets.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.databinding.ViewGamingNewsItemBinding

class GamingNewsItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {


    private val binding = ViewGamingNewsItemBinding.inflate(context.layoutInflater, this)

    var imageUrl by observeChanges<String?>(null) { _, newValue ->
        if(newValue == null) {
            binding.imageView.isVisible = false
        } else {
            binding.imageView.isVisible = true
            binding.imageView.imageUrl = newValue
        }
    }

    var title: CharSequence
        set(value) { binding.titleTv.text = value }
        get() = binding.titleTv.text

    var lede: CharSequence
        set(value) { binding.ledeTv.text = value }
        get() = binding.ledeTv.text

    var publicationDate: CharSequence
        set(value) { binding.publicationDateTv.text = value }
        get() = binding.publicationDateTv.text

    var onNewsItemClickListener: (() -> Unit)? = null


    init {
        initCard()
    }


    private fun initCard() {
        setCardBackgroundColor(getColor(R.color.gaming_news_item_card_background_color))
        cardElevation = getDimension(R.dimen.gaming_news_item_card_elevation)
        onClick { onNewsItemClickListener?.invoke() }
    }


}