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

package com.paulrybitskyi.gamedge.data.entities

data class Game(
    val id: Int,
    val hypeCount: Int,
    val releaseDate: Long,
    val criticsRating: Double,
    val usersRating: Double,
    val totalRating: Double,
    val name: String,
    val summary: String,
    val storyline: String,
    val cover: Image,
    val timeToBeat: TimeToBeat,
    val ageRatings: List<AgeRating>,
    val artworks: List<Image>,
    val screenshots: List<Image>,
    val genres: List<Genre>,
    val platforms: List<Platform>,
    val playerPerspectives: List<PlayerPerspective>,
    val themes: List<Theme>,
    val modes: List<Mode>,
    val keywords: List<Keyword>,
    val involvedCompanies: List<InvolvedCompany>,
    val websites: List<Website>,
    val similarGames: List<Int>
)