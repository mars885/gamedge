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

internal typealias DatabaseGame = Game
internal typealias DatabaseCategory = Category
internal typealias DatabaseImage = Image
internal typealias DatabaseReleaseDate = ReleaseDate
internal typealias DatabaseReleaseDateCategory = ReleaseDateCategory
internal typealias DatabaseAgeRating = AgeRating
internal typealias DatabaseAgeRatingCategory = AgeRatingCategory
internal typealias DatabaseAgeRatingType = AgeRatingType
internal typealias DatabaseVideo = Video
internal typealias DatabaseGenre = Genre
internal typealias DatabasePlatform = Platform
internal typealias DatabasePlayerPerspective = PlayerPerspective
internal typealias DatabaseTheme = Theme
internal typealias DatabaseMode = Mode
internal typealias DatabaseKeyword = Keyword
internal typealias DatabaseInvolvedCompany = InvolvedCompany
internal typealias DatabaseCompany = Company
internal typealias DatabaseWebsite = Website
internal typealias DatabaseWebsiteCategory = WebsiteCategory
