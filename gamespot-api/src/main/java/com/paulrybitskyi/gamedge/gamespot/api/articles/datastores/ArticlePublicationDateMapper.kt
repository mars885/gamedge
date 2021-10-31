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

package com.paulrybitskyi.gamedge.gamespot.api.articles.datastores

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter.ofPattern
import javax.inject.Inject

internal class ArticlePublicationDateMapper @Inject constructor() {

    private companion object {
        private const val PUBLICATION_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

        // The best we can do here, since the API does not return
        // the time zone for some reason.
        private const val PUBLICATION_DATE_TIME_ZONE = "America/Los_Angeles"
    }

    fun mapToTimestamp(publicationDate: String): Long {
        return LocalDateTime.parse(publicationDate, ofPattern(PUBLICATION_DATE_FORMAT))
            .atZone(ZoneId.of(PUBLICATION_DATE_TIME_ZONE))
            .toInstant()
            .toEpochMilli()
    }
}
