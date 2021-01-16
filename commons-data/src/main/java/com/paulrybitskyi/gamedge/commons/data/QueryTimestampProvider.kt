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

package com.paulrybitskyi.gamedge.commons.data

import com.paulrybitskyi.hiltbinder.BindType
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject


private val POPULAR_GAMES_MIN_RELEASE_DATE_DURATION = TimeUnit.DAYS.toSeconds(90L)
private val RECENTLY_RELEASED_GAMES_MIN_RELEASE_DATE_DURATION = TimeUnit.DAYS.toSeconds(7L)


interface QueryTimestampProvider {

    fun getPopularGamesMinReleaseDate(): Long

    fun getRecentlyReleasedGamesMinReleaseDate(): Long

    fun getRecentlyReleasedGamesMaxReleaseDate(): Long

    fun getComingSoonGamesMinReleaseDate(): Long

    fun getMostAnticipatedGamesMinReleaseDate(): Long

}

@BindType
internal class QueryTimestampProviderImpl @Inject constructor(
    private val timestampProvider: TimestampProvider
) : QueryTimestampProvider {


    override fun getPopularGamesMinReleaseDate(): Long {
        val currentUnixTimestamp = getUnixTimestamp()
        val minReleaseDateTimestamp = (currentUnixTimestamp - POPULAR_GAMES_MIN_RELEASE_DATE_DURATION)

        return minReleaseDateTimestamp
    }


    override fun getRecentlyReleasedGamesMinReleaseDate(): Long {
        val maxReleaseDateTimestamp = getRecentlyReleasedGamesMaxReleaseDate()
        val minReleaseDateTimestamp = (maxReleaseDateTimestamp - RECENTLY_RELEASED_GAMES_MIN_RELEASE_DATE_DURATION)

        return minReleaseDateTimestamp
    }


    override fun getRecentlyReleasedGamesMaxReleaseDate(): Long {
        return getUnixTimestamp()
    }


    override fun getComingSoonGamesMinReleaseDate(): Long {
        return getUnixTimestamp()
    }


    override fun getMostAnticipatedGamesMinReleaseDate(): Long {
        return getUnixTimestamp()
    }


    private fun getUnixTimestamp(): Long {
        return timestampProvider.getUnixTimestamp(TimeUnit.SECONDS)
    }


}