/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.common.data

import com.paulrybitskyi.gamedge.common.domain.auth.entities.OauthCredentials
import com.paulrybitskyi.gamedge.common.domain.games.entities.Company

internal val DOMAIN_OAUTH_CREDENTIALS = OauthCredentials(
    accessToken = "access_token",
    tokenType = "token_type",
    tokenTtl = 5000L,
)

internal val DOMAIN_COMPANY = Company(
    id = 1,
    name = "name",
    websiteUrl = "website_url",
    logo = null,
    developedGames = listOf(1, 2, 3),
)
