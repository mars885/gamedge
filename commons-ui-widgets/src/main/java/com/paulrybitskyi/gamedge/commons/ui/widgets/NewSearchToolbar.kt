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

package com.paulrybitskyi.gamedge.commons.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.paulrybitskyi.gamedge.commons.ui.textSizeResource

private const val CLEAR_BUTTON_ANIMATION_DURATION = 100

@Composable
fun NewSearchToolbar(
    queryText: String,
    placeholderText: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = colorResource(R.color.toolbar_background_color),
    titleTextColor: Color = colorResource(R.color.toolbar_title_text_color),
    iconColor: Color = colorResource(R.color.toolbar_button_icon_color),
    cursorColor: Color = colorResource(R.color.colorAccent),
    titleTextSize: TextUnit = textSizeResource(R.dimen.search_toolbar_query_input_text_size),
    focusRequester: FocusRequester = remember { FocusRequester() },
    onQueryChanged: ((String) -> Unit)? = null,
    onSearchActionRequested: ((String) -> Unit)? = null,
    onBackButtonClicked: (() -> Unit)? = null,
    onClearButtonClicked: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .then(modifier)
            .height(dimensionResource(R.dimen.toolbar_height)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SearchToolbarButton(
            icon = painterResource(R.drawable.arrow_left),
            iconColor = iconColor,
            onClick = { onBackButtonClicked?.invoke() },
        )

        SearchToolbarInput(
            queryText = queryText,
            placeholderText = placeholderText,
            focusRequester = focusRequester,
            modifier = Modifier.weight(1f),
            titleTextColor = titleTextColor,
            cursorColor = cursorColor,
            titleTextSize = titleTextSize,
            onQueryChanged = { onQueryChanged?.invoke(it) },
            onSearchActionRequested = onSearchActionRequested,
        )

        SearchToolbarClearButton(
            isVisible = queryText.isNotEmpty(),
            iconColor = iconColor,
            onClearButtonClicked = {
                focusRequester.requestFocus()
                onClearButtonClicked?.invoke()
            }
        )
    }
}

@Composable
private fun SearchToolbarButton(
    icon: Painter,
    iconColor: Color,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier.size(dimensionResource(R.dimen.toolbar_height)),
        onClick = onClick,
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = iconColor
        )
    }
}

@Composable
private fun SearchToolbarInput(
    queryText: String,
    placeholderText: String,
    focusRequester: FocusRequester,
    modifier: Modifier,
    titleTextColor: Color,
    cursorColor: Color,
    titleTextSize: TextUnit,
    onQueryChanged: (String) -> Unit,
    onSearchActionRequested: ((String) -> Unit)?,
) {
    // TextField has a preset height and some characters from the font
    // that is used are clipped. Therefore, using BasicTextField.

    BasicTextField(
        value = queryText,
        onValueChange = onQueryChanged,
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.search_toolbar_query_input_horizontal_margin))
            .focusRequester(focusRequester),
        textStyle = LocalTextStyle.current.copy(
            color = titleTextColor,
            fontSize = titleTextSize,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            autoCorrect = false,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchActionRequested?.invoke(queryText) },
        ),
        singleLine = true,
        cursorBrush = SolidColor(cursorColor),
    ) { innerTextField ->
        if (queryText.isEmpty()) {
            Text(
                text = placeholderText,
                color = titleTextColor,
                fontSize = titleTextSize,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

        innerTextField()
    }
}

@Composable
private fun SearchToolbarClearButton(
    isVisible: Boolean,
    iconColor: Color,
    onClearButtonClicked: () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { it / 2 },
            animationSpec = tween(durationMillis = CLEAR_BUTTON_ANIMATION_DURATION),
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { it / 2 },
            animationSpec = tween(durationMillis = CLEAR_BUTTON_ANIMATION_DURATION),
        ),
    ) {
        SearchToolbarButton(
            icon = painterResource(R.drawable.close),
            iconColor = iconColor,
            onClick = onClearButtonClicked,
        )
    }
}
