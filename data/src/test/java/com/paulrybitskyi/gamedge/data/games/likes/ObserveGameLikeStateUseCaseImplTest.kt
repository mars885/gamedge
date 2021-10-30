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

package com.paulrybitskyi.gamedge.data.games.likes

import app.cash.turbine.test
import com.paulrybitskyi.gamedge.commons.testing.OBSERVE_GAME_LIKE_STATE_USE_CASE_PARAMS
import com.paulrybitskyi.gamedge.data.commons.DataPagination
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.data.games.datastores.LikedGamesLocalDataStore
import com.paulrybitskyi.gamedge.data.games.usecases.likes.ObserveGameLikeStateUseCaseImpl
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveGameLikeStateUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test

internal class ObserveGameLikeStateUseCaseImplTest {


    @MockK private lateinit var likedGamesLocalDataStore: LikedGamesLocalDataStore

    private lateinit var SUT: ObserveGameLikeStateUseCaseImpl


    @Before
    fun setup() {
        MockKAnnotations.init(this)

        SUT = ObserveGameLikeStateUseCaseImpl(likedGamesLocalDataStore)
    }


    @Test
    fun `Emits game like state successfully`() {
        runBlockingTest {
            coEvery { likedGamesLocalDataStore.observeGameLikeState(any()) } returns flowOf(true)
            SUT.execute(OBSERVE_GAME_LIKE_STATE_USE_CASE_PARAMS).test {
                assertThat(awaitItem()).isTrue
                awaitComplete()
            }

            coEvery { likedGamesLocalDataStore.observeGameLikeState(any()) } returns flowOf(false)
            SUT.execute(OBSERVE_GAME_LIKE_STATE_USE_CASE_PARAMS).test {
                assertThat(awaitItem()).isFalse
                awaitComplete()
            }
        }
    }


}