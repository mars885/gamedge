package com.paulrybitskyi.gamedge.feature.info.presentation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class GameInfoRoute(
    val gameId: Int,
) : NavKey
