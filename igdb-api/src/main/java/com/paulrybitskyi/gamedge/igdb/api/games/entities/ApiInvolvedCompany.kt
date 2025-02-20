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

package com.paulrybitskyi.gamedge.igdb.api.games.entities

import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.Apicalypse
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.annotations.ApicalypseClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@ApicalypseClass
@Serializable
data class ApiInvolvedCompany(
    @Apicalypse(Schema.COMPANY)
    @SerialName(Schema.COMPANY)
    val company: ApiCompany,
    @Apicalypse(Schema.IS_DEVELOPER)
    @SerialName(Schema.IS_DEVELOPER)
    val isDeveloper: Boolean,
    @Apicalypse(Schema.IS_PUBLISHER)
    @SerialName(Schema.IS_PUBLISHER)
    val isPublisher: Boolean,
    @Apicalypse(Schema.IS_PORTER)
    @SerialName(Schema.IS_PORTER)
    val isPorter: Boolean,
    @Apicalypse(Schema.IS_SUPPORTING)
    @SerialName(Schema.IS_SUPPORTING)
    val isSupporting: Boolean,
) {

    object Schema {
        const val COMPANY = "company"
        const val IS_DEVELOPER = "developer"
        const val IS_PUBLISHER = "publisher"
        const val IS_PORTER = "porting"
        const val IS_SUPPORTING = "supporting"
    }
}
