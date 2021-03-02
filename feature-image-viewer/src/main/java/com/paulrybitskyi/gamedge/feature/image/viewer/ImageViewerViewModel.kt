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

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.paulrybitskyi.gamedge.commons.ui.base.BaseViewModel
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


internal const val PARAM_TITLE = "title"
internal const val PARAM_INITIAL_POSITION = "initial_position"
internal const val PARAM_IMAGE_URLS = "image_urls"

internal const val KEY_SELECTED_POSITION = "selected_position"


@HiltViewModel
internal class ImageViewerViewModel @Inject constructor(
    private val stringProvider: StringProvider,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    private val title: String

    private val _selectedPosition: MutableStateFlow<Int>
    private val _imageUrls: MutableStateFlow<List<String>>
    private val _toolbarTitle: MutableStateFlow<String>

    val selectedPosition: StateFlow<Int>
        get() = _selectedPosition

    val imageUrls: StateFlow<List<String>>
        get() = _imageUrls

    val toolbarTitle: StateFlow<String>
        get() = _toolbarTitle


    init {
        title = savedStateHandle.get<String>(PARAM_TITLE)
            ?: stringProvider.getString(R.string.image_viewer_default_toolbar_title)

        val selectedPosition = getSelectedPosition()
        val imageUrls = checkNotNull(savedStateHandle.get<Array<String>>(PARAM_IMAGE_URLS))

        _selectedPosition = MutableStateFlow(selectedPosition)
        _imageUrls = MutableStateFlow(imageUrls.toList())
        _toolbarTitle = MutableStateFlow("")

        observeSelectedPositionChanges()
    }


    private fun getSelectedPosition(): Int {
        return savedStateHandle.get(KEY_SELECTED_POSITION) ?:
            checkNotNull(savedStateHandle.get<Int>(PARAM_INITIAL_POSITION))
    }


    private fun observeSelectedPositionChanges() {
        selectedPosition
            .onEach {
                _toolbarTitle.value = updateToolbarTitle()
                savedStateHandle.set(KEY_SELECTED_POSITION, it)
            }
            .launchIn(viewModelScope)
    }


    private fun updateToolbarTitle(): String {
        if(imageUrls.value.size == 1) return title

        return stringProvider.getString(
            R.string.image_viewer_toolbar_title_template,
            title,
            (selectedPosition.value + 1),
            imageUrls.value.size
        )
    }


    fun onToolbarRightButtonClicked() {
        val currentImageUrl = imageUrls.value[selectedPosition.value]
        val textToShare = stringProvider.getString(
            R.string.text_sharing_message_template,
            stringProvider.getString(R.string.image),
            currentImageUrl
        )

        dispatchCommand(ImageViewerCommand.ShareText(textToShare))
    }


    fun onPageChanged(position: Int) {
        _selectedPosition.value = position
    }


    fun onBackPressed() {
        dispatchCommand(ImageViewerCommand.ResetSystemWindows)
        route(ImageViewerRoute.Back)
    }


}