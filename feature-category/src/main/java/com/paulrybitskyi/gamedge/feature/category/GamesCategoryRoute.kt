package com.paulrybitskyi.gamedge.feature.category

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class GamesCategoryRoute(
    val category: String,
) : NavKey
