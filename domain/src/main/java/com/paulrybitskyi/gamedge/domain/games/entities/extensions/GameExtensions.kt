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

package com.paulrybitskyi.gamedge.domain.games.entities.extensions

import com.paulrybitskyi.gamedge.domain.games.entities.Company
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.entities.InvolvedCompany


val Game.hasSimilarGames: Boolean
    get() = similarGames.isNotEmpty()

val Game.developerCompany: Company?
    get() = involvedCompanies.firstOrNull(InvolvedCompany::isDeveloper)?.company

val Game.publisherCompany: Company?
    get() = involvedCompanies.firstOrNull(InvolvedCompany::isPublisher)?.company

val Game.porterCompany: Company?
    get() = involvedCompanies.firstOrNull(InvolvedCompany::isPorter)?.company