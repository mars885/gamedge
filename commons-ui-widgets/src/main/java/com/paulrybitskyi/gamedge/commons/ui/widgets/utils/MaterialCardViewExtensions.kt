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

package com.paulrybitskyi.gamedge.commons.ui.widgets.utils

import androidx.annotation.Px
import com.google.android.material.card.MaterialCardView


var MaterialCardView.contentLeftPadding: Int
    set(value) { updateContentPadding(leftPadding = value) }
    get() = contentPaddingLeft

var MaterialCardView.contentTopPadding: Int
    set(value) { updateContentPadding(topPadding = value) }
    get() = contentPaddingTop

var MaterialCardView.contentRightPadding: Int
    set(value) { updateContentPadding(rightPadding = value) }
    get() = contentPaddingRight

var MaterialCardView.contentBottomPadding: Int
    set(value) { updateContentPadding(bottomPadding = value) }
    get() = contentPaddingBottom


fun MaterialCardView.updateContentPadding(
    @Px leftPadding: Int = this.contentPaddingLeft,
    @Px topPadding: Int = this.contentPaddingTop,
    @Px rightPadding: Int = this.contentPaddingRight,
    @Px bottomPadding: Int = this.contentPaddingBottom
) {
    setContentPadding(leftPadding, topPadding, rightPadding, bottomPadding)
}