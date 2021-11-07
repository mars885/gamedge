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

package com.paulrybitskyi.gamedge.feature.news.widgets

data class GamingNewsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val news: List<GamingNewsItemModel> = emptyList(),
)

val GamingNewsState.isInEmptyState: Boolean
    get() = (!isLoading && news.isEmpty())

val GamingNewsState.isInSuccessState: Boolean
    get() = news.isNotEmpty()
