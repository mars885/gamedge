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

package com.paulrybitskyi.gamedge.igdb.api.utils

import com.github.michaelbull.result.Result
import com.paulrybitskyi.gamedge.igdb.api.entities.*


internal typealias ApiResult<T> = Result<T, Error>

internal typealias ApiAgeRating = AgeRating
internal typealias ApiAgeRatingCategory = AgeRatingCategory
internal typealias ApiAgeRatingType = AgeRatingType
internal typealias ApiCompany = Company
internal typealias ApiGame = Game
internal typealias ApiGenre = Genre
internal typealias ApiImage = Image
internal typealias ApiInvolvedCompany = InvolvedCompany
internal typealias ApiKeyword = Keyword
internal typealias ApiMode = Mode
internal typealias ApiPlatform = Platform
internal typealias ApiPlayerPerspective = PlayerPerspective
internal typealias ApiTheme = Theme
internal typealias ApiTimeToBeat = TimeToBeat
internal typealias ApiWebsite = Website
internal typealias ApiWebsiteCategory = WebsiteCategory