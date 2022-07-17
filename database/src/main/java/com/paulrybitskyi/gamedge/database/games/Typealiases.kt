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

package com.paulrybitskyi.gamedge.database.games

import com.paulrybitskyi.gamedge.database.games.entities.AgeRating
import com.paulrybitskyi.gamedge.database.games.entities.AgeRatingCategory
import com.paulrybitskyi.gamedge.database.games.entities.AgeRatingType
import com.paulrybitskyi.gamedge.database.games.entities.Category
import com.paulrybitskyi.gamedge.database.games.entities.Company
import com.paulrybitskyi.gamedge.database.games.entities.Game
import com.paulrybitskyi.gamedge.database.games.entities.Genre
import com.paulrybitskyi.gamedge.database.games.entities.Image
import com.paulrybitskyi.gamedge.database.games.entities.InvolvedCompany
import com.paulrybitskyi.gamedge.database.games.entities.Keyword
import com.paulrybitskyi.gamedge.database.games.entities.Mode
import com.paulrybitskyi.gamedge.database.games.entities.Platform
import com.paulrybitskyi.gamedge.database.games.entities.PlayerPerspective
import com.paulrybitskyi.gamedge.database.games.entities.ReleaseDate
import com.paulrybitskyi.gamedge.database.games.entities.ReleaseDateCategory
import com.paulrybitskyi.gamedge.database.games.entities.Theme
import com.paulrybitskyi.gamedge.database.games.entities.Video
import com.paulrybitskyi.gamedge.database.games.entities.Website
import com.paulrybitskyi.gamedge.database.games.entities.WebsiteCategory

typealias DatabaseGame = Game
typealias DatabaseCategory = Category
typealias DatabaseImage = Image
typealias DatabaseReleaseDate = ReleaseDate
typealias DatabaseReleaseDateCategory = ReleaseDateCategory
typealias DatabaseAgeRating = AgeRating
typealias DatabaseAgeRatingCategory = AgeRatingCategory
typealias DatabaseAgeRatingType = AgeRatingType
typealias DatabaseVideo = Video
typealias DatabaseGenre = Genre
typealias DatabasePlatform = Platform
typealias DatabasePlayerPerspective = PlayerPerspective
typealias DatabaseTheme = Theme
typealias DatabaseMode = Mode
typealias DatabaseKeyword = Keyword
typealias DatabaseInvolvedCompany = InvolvedCompany
typealias DatabaseCompany = Company
typealias DatabaseWebsite = Website
typealias DatabaseWebsiteCategory = WebsiteCategory
