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

package com.paulrybitskyi.gamedge.data.articles.usecases.commons.throttling

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


internal interface ArticlesRefreshingThrottler {

    suspend fun canRefreshArticles(key: String): Boolean

    suspend fun updateArticlesLastRefreshTime(key: String)

}


@Singleton
@BindType
internal class ArticlesRefreshingThrottlerImpl @Inject constructor(
    private val articlesPreferences: DataStore<Preferences>,
    private val timestampProvider: TimestampProvider
) : ArticlesRefreshingThrottler {


    private companion object {

        val ARTICLES_REFRESH_TIMEOUT = TimeUnit.MINUTES.toMillis(10L)

    }


    override suspend fun canRefreshArticles(key: String): Boolean {
        return articlesPreferences.data
            .map { it[preferencesKey<Long>(key)] ?: 0L }
            .map { timestampProvider.getUnixTimestamp() > (it + ARTICLES_REFRESH_TIMEOUT) }
            .first()
    }


    override suspend fun updateArticlesLastRefreshTime(key: String) {
        articlesPreferences.edit {
            it[preferencesKey(key)] = timestampProvider.getUnixTimestamp()
        }
    }


}