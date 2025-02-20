/*
 * Copyright 2020 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.feature.info.presentation.widgets.companies

import androidx.compose.runtime.Immutable

@Immutable
internal data class GameInfoCompanyUiModel(
    val id: Int,
    val logoUrl: String?,
    val logoWidth: Int?,
    val logoHeight: Int?,
    val websiteUrl: String,
    val name: String,
    val roles: String,
) {

    val hasLogoSize: Boolean
        get() = ((logoWidth != null) && (logoHeight != null))
}
