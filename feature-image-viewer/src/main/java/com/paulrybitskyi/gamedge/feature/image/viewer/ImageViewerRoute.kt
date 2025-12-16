package com.paulrybitskyi.gamedge.feature.image.viewer

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ImageViewerRoute(
    val imageUrls: List<String>,
    val title: String?,
    val initialPosition: Int,
) : NavKey {

    init {
        require(imageUrls.isNotEmpty()) {
            "Image URLs must not be empty."
        }

        require(initialPosition in imageUrls.indices) {
            "Initial position must be within the bounds of the image URLs."
        }
    }

    internal fun requireTitle(): String {
        return checkNotNull(title) {
            "Title must not be null."
        }
    }
}
