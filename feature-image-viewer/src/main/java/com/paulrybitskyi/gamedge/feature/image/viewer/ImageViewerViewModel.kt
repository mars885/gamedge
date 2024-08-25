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
import androidx.navigation.toRoute
import com.paulrybitskyi.gamedge.common.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import com.paulrybitskyi.gamedge.core.R as CoreR

internal const val KEY_SELECTED_POSITION = "selected_position"

@HiltViewModel
internal class ImageViewerViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val args = savedStateHandle
        .toRoute<ImageViewerDestination>()
        .let { args ->
            args.copy(
                title = args.title
                    ?: stringProvider.getString(R.string.image_viewer_default_toolbar_title),
            )
        }

    private val _uiState = MutableStateFlow(createInitialUiState())

    private val currentUiState: ImageViewerUiState
        get() = _uiState.value

    val uiState: StateFlow<ImageViewerUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                selectedImageUrlIndex = getSelectedPosition(),
                imageUrls = args.imageUrls,
            )
        }

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
        return savedStateHandle[KEY_SELECTED_POSITION] ?: checkNotNull(args.initialPosition)
    }

    private fun observeSelectedPositionChanges() {
        uiState
            .map { it.selectedImageUrlIndex }
            .distinctUntilChanged()
            .onEach { selectedImageUrlIndex ->
                _uiState.update { it.copy(toolbarTitle = updateToolbarTitle()) }
                savedStateHandle[KEY_SELECTED_POSITION] = selectedImageUrlIndex
            }
            .launchIn(viewModelScope)
    }

    private fun updateToolbarTitle(): String {
        if (currentUiState.imageUrls.size == 1) return args.requireTitle()

        return stringProvider.getString(
            R.string.image_viewer_toolbar_title_template,
            args.requireTitle(),
            (currentUiState.selectedImageUrlIndex + 1),
            currentUiState.imageUrls.size,
        )
    }

    fun onToolbarRightButtonClicked() {
        val currentImageUrl = currentUiState.imageUrls[currentUiState.selectedImageUrlIndex]
        val textToShare = stringProvider.getString(
            CoreR.string.text_sharing_message_template,
            stringProvider.getString(CoreR.string.image),
            currentImageUrl,
        )

        dispatchCommand(ImageViewerCommand.ShareText(textToShare))
    }

    fun onImageChanged(imageIndex: Int) {
        _uiState.update { it.copy(selectedImageUrlIndex = imageIndex) }
    }

    fun onBackPressed() {
        route(ImageViewerRoute.Back)
    }
}
