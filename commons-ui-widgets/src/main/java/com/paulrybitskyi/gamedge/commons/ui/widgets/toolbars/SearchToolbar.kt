/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.commons.ui.widgets.toolbars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paulrybitskyi.gamedge.commons.ui.theme.GamedgeTheme
import com.paulrybitskyi.gamedge.commons.ui.widgets.R

private const val CLEAR_BUTTON_ANIMATION_DURATION = 100

@Composable
fun SearchToolbar(
    queryText: String,
    placeholderText: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    backgroundColor: Color = GamedgeTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = TOOLBAR_ELEVATION,
    titleTextStyle: TextStyle = GamedgeTheme.typography.h5,
    cursorColor: Color = GamedgeTheme.colors.secondary,
    focusRequester: FocusRequester = remember { FocusRequester() },
    onQueryChanged: ((query: String) -> Unit)? = null,
    onSearchConfirmed: ((query: String) -> Unit)? = null,
    onBackButtonClicked: (() -> Unit)? = null,
    onClearButtonClicked: (() -> Unit)? = null,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .height(TOOLBAR_HEIGHT),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                icon = painterResource(R.drawable.arrow_left),
                onClick = { onBackButtonClicked?.invoke() },
            )

            Input(
                queryText = queryText,
                placeholderText = placeholderText,
                focusRequester = focusRequester,
                modifier = Modifier.weight(1f),
                titleTextStyle = titleTextStyle,
                cursorColor = cursorColor,
                onQueryChanged = { onQueryChanged?.invoke(it) },
                onSearchConfirmed = onSearchConfirmed,
            )

            ClearButton(
                isVisible = queryText.isNotEmpty(),
                onClearButtonClicked = {
                    focusRequester.requestFocus()
                    onClearButtonClicked?.invoke()
                }
            )
        }
    }
}

@Composable
private fun Button(
    icon: Painter,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = Modifier.size(TOOLBAR_HEIGHT),
        onClick = onClick,
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
        )
    }
}

@Composable
private fun Input(
    queryText: String,
    placeholderText: String,
    focusRequester: FocusRequester,
    modifier: Modifier,
    titleTextStyle: TextStyle,
    cursorColor: Color,
    onQueryChanged: (query: String) -> Unit,
    onSearchConfirmed: ((query: String) -> Unit)?,
) {
    // Need custom TextFieldValue here to set the default selection
    // to the length of the query text after process death restoration.
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = queryText,
                selection = TextRange(queryText.length),
            )
        )
    }
    val textFieldValue = textFieldValueState.copy(text = queryText)

    // TextField has a preset height and some characters from the font
    // that is used are clipped. Therefore, using BasicTextField.
    BasicTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it

            if (queryText != it.text) {
                onQueryChanged(it.text)
            }
        },
        modifier = modifier
            .padding(horizontal = GamedgeTheme.spaces.spacing_4_0)
            .focusRequester(focusRequester),
        textStyle = titleTextStyle.copy(color = LocalContentColor.current),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            autoCorrect = false,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchConfirmed?.invoke(queryText) },
        ),
        singleLine = true,
        cursorBrush = SolidColor(cursorColor),
    ) { innerTextField ->
        if (queryText.isEmpty()) {
            Text(
                text = placeholderText,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = titleTextStyle,
            )
        }

        innerTextField()
    }
}

@Composable
private fun ClearButton(
    isVisible: Boolean,
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
        Button(
            icon = painterResource(R.drawable.close),
            onClick = onClearButtonClicked,
        )
    }
}

@Preview
@Composable
internal fun SearchToolbarWithQueryPreview() {
    GamedgeTheme {
        SearchToolbar(
            queryText = "God of War",
            placeholderText = "Search games",
        )
    }
}

@Preview
@Composable
internal fun SearchToolbarWithoutQueryPreview() {
    GamedgeTheme {
        SearchToolbar(
            queryText = "",
            placeholderText = "Search games",
        )
    }
}
