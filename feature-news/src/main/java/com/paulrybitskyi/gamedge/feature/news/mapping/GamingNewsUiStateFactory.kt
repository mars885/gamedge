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

package com.paulrybitskyi.gamedge.feature.news.mapping

import com.paulrybitskyi.gamedge.domain.articles.entities.Article
import com.paulrybitskyi.gamedge.feature.news.widgets.GamingNewsUiState
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


interface GamingNewsUiStateFactory {

    fun createWithEmptyState(): GamingNewsUiState

    fun createWithLoadingState(): GamingNewsUiState

    fun createWithResultState(articles: List<Article>): GamingNewsUiState

}


@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GamingNewsUiStateFactoryImpl @Inject constructor(
    private val gamingNewsItemModelMapper: GamingNewsItemModelMapper
) : GamingNewsUiStateFactory {


    override fun createWithEmptyState(): GamingNewsUiState {
        return GamingNewsUiState.Empty
    }


    override fun createWithLoadingState(): GamingNewsUiState {
        return GamingNewsUiState.Loading
    }


    override fun createWithResultState(articles: List<Article>): GamingNewsUiState {
        if(articles.isEmpty()) return createWithEmptyState()

        return GamingNewsUiState.Result(
            articles.map(gamingNewsItemModelMapper::mapToGamingNewsItemModel)
        )
    }


}
