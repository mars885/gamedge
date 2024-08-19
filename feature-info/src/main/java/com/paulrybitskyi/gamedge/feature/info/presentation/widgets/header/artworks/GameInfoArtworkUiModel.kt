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

package com.paulrybitskyi.gamedge.feature.info.presentation.widgets.header.artworks

import androidx.compose.runtime.Immutable

private const val DEFAULT_IMAGE_ID = "default_image_id"

@Immutable
internal sealed class GameInfoArtworkUiModel {
    data object DefaultImage : GameInfoArtworkUiModel()
    data class UrlImage(val id: String, val url: String) : GameInfoArtworkUiModel()
}

internal val GameInfoArtworkUiModel.id: String
    get() = when (this) {
        is GameInfoArtworkUiModel.DefaultImage -> DEFAULT_IMAGE_ID
        is GameInfoArtworkUiModel.UrlImage -> id
    }
