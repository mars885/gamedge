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

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.Transformation
import com.paulrybitskyi.commons.ktx.centerX
import com.paulrybitskyi.commons.ktx.centerY
import com.paulrybitskyi.commons.ktx.hasTransparentPixels
import com.paulrybitskyi.commons.ktx.isOpaque
import com.paulrybitskyi.gamedge.commons.ui.CROSSFADE_ANIMATION_DURATION
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.core.utils.height
import com.paulrybitskyi.gamedge.core.utils.width
import com.paulrybitskyi.gamedge.feature.info.R

@Composable
internal fun GameInfoCompanies(
    companies: List<GameInfoCompanyModel>,
    onCompanyClicked: (GameInfoCompanyModel) -> Unit,
) {
    val density = LocalDensity.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        elevation = dimensionResource(R.dimen.game_companies_card_elevation),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.game_companies_card_vertical_padding)),
        ) {
            Text(
                text = stringResource(R.string.game_companies_title),
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.game_companies_title_padding_bottom))
                    .padding(horizontal = dimensionResource(R.dimen.game_companies_title_padding)),
                color = GamedgeTheme.colors.onPrimary,
                style = GamedgeTheme.typography.h6,
            )

            LazyRow(
                contentPadding = PaddingValues(
                    horizontal = dimensionResource(R.dimen.game_companies_horizontal_content_padding),
                ),
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.game_companies_horizontal_arrangement),
                )
            ) {
                items(companies, key = GameInfoCompanyModel::id) { company ->
                    Company(
                        logoImageUrl = company.logoUrl,
                        logoContainerSize = Pair(
                            with(density) { company.logoContainerSize.width.toDp() },
                            with(density) { company.logoContainerSize.height.toDp() },
                        ),
                        logoImageSize = company.logoImageSize,
                        name = company.name,
                        roles = company.roles,
                        onCompanyClicked = { onCompanyClicked(company) },
                    )
                }
            }
        }
    }
}

@Composable
private fun Company(
    logoImageUrl: String?,
    logoContainerSize: Pair<Dp, Dp>,
    logoImageSize: Pair<Int, Int>,
    name: String,
    roles: String,
    onCompanyClicked: () -> Unit,
) {
    Card(
        onClick = onCompanyClicked,
        backgroundColor = Color.Transparent,
        elevation = dimensionResource(R.dimen.game_company_card_elevation),
    ) {
        Column {
            CompanyLogoImage(
                logoImageUrl = logoImageUrl,
                logoContainerSize = logoContainerSize,
                logoImageSize = logoImageSize,
            )

            CompanyDetails(
                name = name,
                roles = roles,
                containerWidth = logoContainerSize.width,
            )
        }
    }
}

@Composable
private fun CompanyLogoImage(
    logoImageUrl: String?,
    logoContainerSize: Pair<Dp, Dp>,
    logoImageSize: Pair<Int, Int>,
) {
    val density = LocalDensity.current
    val logoContainerWidthInPx = with(density) { logoContainerSize.width.roundToPx() }
    val logoContainerHeightInPx = with(density) { logoContainerSize.height.roundToPx() }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(logoImageUrl)
            .fallback(R.drawable.game_landscape_placeholder)
            .placeholder(R.drawable.game_landscape_placeholder)
            .error(R.drawable.game_landscape_placeholder)
            .size(logoImageSize.width, logoImageSize.height)
            .transformations(
                LogoImageTransformation(
                    logoContainerWidth = logoContainerWidthInPx,
                    logoContainerHeight = logoContainerHeightInPx,
                )
            )
            .crossfade(CROSSFADE_ANIMATION_DURATION)
            .build(),
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(
            width = logoContainerSize.width,
            height = logoContainerSize.height,
        ),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun CompanyDetails(
    name: String,
    roles: String,
    containerWidth: Dp,
) {
    Surface(
        modifier = Modifier.width(containerWidth),
        color = GamedgeTheme.colors.primaryVariant,
        contentColor = GamedgeTheme.colors.onSurface,
    ) {
        Column(
            modifier = Modifier.padding(
                dimensionResource(R.dimen.game_company_label_padding)
            ),
        ) {
            Text(
                text = name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = GamedgeTheme.typography.caption,
            )
            Text(
                text = roles,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = GamedgeTheme.typography.caption,
            )
        }
    }
}

private class LogoImageTransformation(
    private val logoContainerWidth: Int,
    private val logoContainerHeight: Int,
) : Transformation {

    private companion object {
        private const val FILL_COLOR_CALCULATION_PIXEL_OFFSET = 10
        private const val TARGET_BITMAP_SCALE_FACTOR = 0.85f
    }

    override val cacheKey = "logo: w - $logoContainerWidth, h - $logoContainerHeight"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val targetBitmap = Bitmap
            .createBitmap(logoContainerWidth, logoContainerHeight, input.config)
            .apply { eraseColor(input.calculateFillColor()) }

        val targetCenterX = targetBitmap.centerX
        val targetCenterY = targetBitmap.centerY
        val sourceOffsetLeft = (targetCenterX - input.centerX)
        val sourceOffsetTop = (targetCenterY - input.centerY)

        return Canvas(targetBitmap)
            .apply {
                scale(
                    TARGET_BITMAP_SCALE_FACTOR,
                    TARGET_BITMAP_SCALE_FACTOR,
                    targetCenterX,
                    targetCenterY
                )

                drawBitmap(input, sourceOffsetLeft, sourceOffsetTop, null)
            }
            .also { input.recycle() }
            .let { targetBitmap }
    }

    private fun Bitmap.calculateFillColor(): Int {
        if (hasTransparentPixels()) return android.graphics.Color.WHITE

        var pixelColor: Int

        for (x in FILL_COLOR_CALCULATION_PIXEL_OFFSET..centerX.toInt()) {
            for (y in FILL_COLOR_CALCULATION_PIXEL_OFFSET..centerY.toInt()) {
                pixelColor = getPixel(x, y)

                if (pixelColor.isOpaque) return pixelColor
            }
        }

        return android.graphics.Color.WHITE
    }
}

@Preview
@Composable
internal fun GameInfoCompaniesPreview() {
    GamedgeTheme {
        GameInfoCompanies(
            companies = listOf(
                GameInfoCompanyModel(
                    id = 1,
                    logoContainerSize = 750 to 400,
                    logoImageSize = 0 to 0,
                    logoUrl = null,
                    websiteUrl = "",
                    name = "FromSoftware",
                    roles = "Main Developer",
                ),
                GameInfoCompanyModel(
                    id = 2,
                    logoContainerSize = 500 to 400,
                    logoImageSize = 0 to 0,
                    logoUrl = null,
                    websiteUrl = "",
                    name = "Bandai Namco Entertainment",
                    roles = "Publisher",
                ),
            ),
            onCompanyClicked = {},
        )
    }
}
