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

package com.paulrybitskyi.gamedge.feature.image.viewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.commons.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

internal const val PARAM_TITLE = "title"
internal const val PARAM_INITIAL_POSITION = "initial_position"
internal const val PARAM_IMAGE_URLS = "image_urls"

internal const val KEY_SELECTED_POSITION = "selected_position"

@HiltViewModel
internal class ImageViewerViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val title: String

    private val _uiState = MutableStateFlow(createInitialUiState())

    private val currentUiState: ImageViewerUiState
        get() = _uiState.value

    val uiState: StateFlow<ImageViewerUiState>
        get() = _uiState

    init {
        title = savedStateHandle.get<String>(PARAM_TITLE)
            ?: stringProvider.getString(R.string.image_viewer_default_toolbar_title)

        val selectedPosition = getSelectedPosition()
        val imageUrls = checkNotNull(savedStateHandle.get<Array<String>>(PARAM_IMAGE_URLS))

        _uiState.value = _uiState.value.copy(
            selectedImageUrlIndex = selectedPosition,
            imageUrls = imageUrls.toList(),
        )

        observeSelectedPositionChanges()
    }

    private fun createInitialUiState(): ImageViewerUiState {
        return ImageViewerUiState(
            toolbarTitle = "",
            imageUrls = emptyList(),
            selectedImageUrlIndex = 0,
        )
    }

    private fun getSelectedPosition(): Int {
        return savedStateHandle.get(KEY_SELECTED_POSITION)
            ?: checkNotNull(savedStateHandle.get<Int>(PARAM_INITIAL_POSITION))
    }

    private fun observeSelectedPositionChanges() {
        uiState
            .map { it.selectedImageUrlIndex }
            .distinctUntilChanged()
            .onEach { selectedImageUrlIndex ->
                _uiState.update { it.copy(toolbarTitle = updateToolbarTitle()) }
                savedStateHandle.set(KEY_SELECTED_POSITION, selectedImageUrlIndex)
            }
            .launchIn(viewModelScope)
    }

    private fun updateToolbarTitle(): String {
        if (currentUiState.imageUrls.size == 1) return title

        return stringProvider.getString(
            R.string.image_viewer_toolbar_title_template,
            title,
            (currentUiState.selectedImageUrlIndex + 1),
            currentUiState.imageUrls.size
        )
    }

    fun onToolbarRightButtonClicked() {
        val currentImageUrl = currentUiState.imageUrls[currentUiState.selectedImageUrlIndex]
        val textToShare = stringProvider.getString(
            R.string.text_sharing_message_template,
            stringProvider.getString(R.string.image),
            currentImageUrl
        )

        dispatchCommand(ImageViewerCommand.ShareText(textToShare))
    }

    fun onImageChanged(position: Int) {
        _uiState.update { it.copy(selectedImageUrlIndex = position) }
    }

    fun onBackPressed() {
        dispatchCommand(ImageViewerCommand.ResetSystemWindows)
        route(ImageViewerRoute.Back)
    }
}
