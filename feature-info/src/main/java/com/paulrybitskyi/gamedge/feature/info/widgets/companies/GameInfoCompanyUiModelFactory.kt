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

package com.paulrybitskyi.gamedge.feature.info.widgets.companies

import com.paulrybitskyi.gamedge.core.factories.IgdbImageExtension
import com.paulrybitskyi.gamedge.core.factories.IgdbImageSize
import com.paulrybitskyi.gamedge.core.factories.IgdbImageUrlFactory
import com.paulrybitskyi.gamedge.core.factories.IgdbImageUrlFactory.Config
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.domain.games.entities.InvolvedCompany
import com.paulrybitskyi.gamedge.feature.info.R
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface GameInfoCompanyUiModelFactory {
    fun createCompanyUiModel(company: InvolvedCompany): GameInfoCompanyUiModel
}

@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GameInfoCompanyUiModelFactoryImpl @Inject constructor(
    private val igdbImageUrlFactory: IgdbImageUrlFactory,
    private val stringProvider: StringProvider
) : GameInfoCompanyUiModelFactory {

    private companion object {
        private const val COMPANY_ROLE_SEPARATOR = ", "
    }

    override fun createCompanyUiModel(company: InvolvedCompany): GameInfoCompanyUiModel {
        return GameInfoCompanyUiModel(
            id = company.company.id,
            logoUrl = company.createLogoUrl(),
            logoWidth = company.company.logo?.width,
            logoHeight = company.company.logo?.height,
            websiteUrl = company.company.websiteUrl,
            name = company.company.name,
            roles = company.createRolesString()
        )
    }

    private fun InvolvedCompany.createLogoUrl(): String? {
        return company.logo?.let { image ->
            igdbImageUrlFactory.createUrl(image, Config(IgdbImageSize.HD, IgdbImageExtension.PNG))
        }
    }

    private fun InvolvedCompany.createRolesString(): String {
        return buildList {
            if (isDeveloper) add(R.string.company_role_developer)
            if (isPublisher) add(R.string.company_role_publisher)
            if (isPorter) add(R.string.company_role_porter)
            if (isSupporting) add(R.string.company_role_supporting)
        }
        .joinToString(
            separator = COMPANY_ROLE_SEPARATOR,
            transform = stringProvider::getString
        )
    }
}

internal fun GameInfoCompanyUiModelFactory.createCompanyUiModels(
    companies: List<InvolvedCompany>,
): List<GameInfoCompanyUiModel> {
    if (companies.isEmpty()) return emptyList()

    val comparator = compareByDescending(InvolvedCompany::isDeveloper)
        .thenByDescending(InvolvedCompany::isPublisher)
        .thenByDescending(InvolvedCompany::isPorter)
        .thenByDescending { it.company.hasLogo }

    return companies
        .sortedWith(comparator)
        .map(::createCompanyUiModel)
}
