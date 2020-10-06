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

package com.paulrybitskyi.gamedge.ui.info.mapping

import android.content.Context
import com.paulrybitskyi.commons.ktx.getDimensionPixelSize
import com.paulrybitskyi.gamedge.R
import com.paulrybitskyi.gamedge.commons.ui.widgets.info.model.GameInfoCompanyModel
import com.paulrybitskyi.gamedge.core.IgdbImageExtension
import com.paulrybitskyi.gamedge.core.IgdbImageSize
import com.paulrybitskyi.gamedge.core.IgdbImageUrlBuilder
import com.paulrybitskyi.gamedge.core.IgdbImageUrlBuilder.Config
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.gamedge.core.utils.width
import com.paulrybitskyi.gamedge.domain.games.entities.InvolvedCompany
import com.paulrybitskyi.gamedge.domain.games.entities.extensions.hasLogo
import kotlin.math.roundToInt


internal interface GameInfoCompanyModelFactory {

    fun createCompanyModels(companies: List<InvolvedCompany>): List<GameInfoCompanyModel>

    fun createCompanyModel(company: InvolvedCompany): GameInfoCompanyModel?

}


internal class GameInfoCompanyModelFactoryImpl(
    private val context: Context,
    private val igdbImageUrlBuilder: IgdbImageUrlBuilder,
    private val stringProvider: StringProvider
) : GameInfoCompanyModelFactory {


    private companion object {

        private const val COMPANY_ROLE_SEPARATOR = ", "

    }


    override fun createCompanyModels(companies: List<InvolvedCompany>): List<GameInfoCompanyModel> {
        if(companies.isEmpty()) return emptyList()

        val comparator = compareByDescending(InvolvedCompany::isDeveloper)
            .thenByDescending(InvolvedCompany::isPublisher)
            .thenByDescending(InvolvedCompany::isPorter)
            .thenByDescending { it.company.hasLogo }

        return companies
            .sortedWith(comparator)
            .mapNotNull(::createCompanyModel)
    }


    override fun createCompanyModel(company: InvolvedCompany): GameInfoCompanyModel? {
        val logoImageSize = company.calculateLogoImageSize()
        val logoViewHeight = context.getDimensionPixelSize(R.dimen.game_info_company_logo_view_height)
        val logoViewSize = (logoImageSize.width to logoViewHeight)

        return GameInfoCompanyModel(
            logoViewSize = logoViewSize,
            logoImageSize = logoImageSize,
            logoUrl = company.buildLogoUrl(),
            websiteUrl = company.company.websiteUrl,
            name = company.company.name,
            roles = company.buildRolesString()
        )
    }


    private fun InvolvedCompany.calculateLogoImageSize(): Pair<Int, Int> {
        val maxLogoWidth = context.getDimensionPixelSize(R.dimen.game_info_company_logo_view_width)
        val maxLogoHeight = context.getDimensionPixelSize(R.dimen.game_info_company_logo_view_height)

        val logoWidth = company.logo?.width
        val logoHeight = company.logo?.height

        if((logoWidth == null) || (logoHeight == null)) {
            return (maxLogoWidth to maxLogoHeight)
        }

        val aspectRatio = (logoWidth.toFloat() / logoHeight.toFloat())
        val adjustedWidth = (aspectRatio * maxLogoHeight).roundToInt()

        if(adjustedWidth <= maxLogoWidth) {
            return (adjustedWidth to maxLogoHeight)
        }

        val finalWidth = maxLogoWidth
        val finalHeight = ((maxLogoWidth.toFloat() / adjustedWidth.toFloat()) * maxLogoHeight).roundToInt()

        return (finalWidth to finalHeight)
    }


    private fun InvolvedCompany.buildLogoUrl(): String? {
        return company.logo?.let { image ->
            igdbImageUrlBuilder.buildUrl(image, Config(IgdbImageSize.HD, IgdbImageExtension.PNG))
        }
    }


    private fun InvolvedCompany.buildRolesString(): String {
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