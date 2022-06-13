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

package com.paulrybitskyi.gamedge.feature.splash

import app.cash.turbine.test
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_ERROR_UNKNOWN
import com.paulrybitskyi.gamedge.commons.testing.DOMAIN_OAUTH_CREDENTIALS
import com.paulrybitskyi.gamedge.commons.testing.FakeErrorMapper
import com.paulrybitskyi.gamedge.commons.testing.FakeLogger
import com.paulrybitskyi.gamedge.commons.testing.MainCoroutineRule
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.domain.auth.usecases.RefreshAuthUseCase
import com.paulrybitskyi.gamedge.domain.commons.extensions.execute
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class SplashViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(StandardTestDispatcher())

    @MockK private lateinit var refreshAuthUseCase: RefreshAuthUseCase

    private lateinit var logger: FakeLogger
    private lateinit var SUT: SplashViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        logger = FakeLogger()
        SUT = SplashViewModel(
            refreshAuthUseCase = refreshAuthUseCase,
            errorMapper = FakeErrorMapper(),
            logger = logger
        )
    }

    @Test
    fun `Routes to dashboard when auth refresh use case emits credentials`() {
        runTest {
            coEvery { refreshAuthUseCase.execute() } returns flowOf(Ok(DOMAIN_OAUTH_CREDENTIALS))

            SUT.routeFlow.test {
                assertThat(awaitItem()).isInstanceOf(SplashRoute.Dashboard::class.java)
            }
        }
    }

    @Test
    fun `Logs error when auth refresh use case emits error result`() {
        runTest {
            coEvery { refreshAuthUseCase.execute() } returns flowOf(Err(DOMAIN_ERROR_UNKNOWN))

            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }

    @Test
    fun `Logs error when auth refresh use case throws error`() {
        runTest {
            coEvery { refreshAuthUseCase.execute() } returns flow { throw IllegalStateException("error") }

            advanceUntilIdle()

            assertThat(logger.errorMessage).isNotEmpty
        }
    }

    @Test
    fun `Dispatches toast showing command when auth refresh use case emits error result`() {
        runTest {
            coEvery { refreshAuthUseCase.execute() } returns flowOf(Err(DOMAIN_ERROR_UNKNOWN))

            SUT.commandFlow.test {
                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Dispatches toast showing command when auth refresh use cae throws error`() {
        runTest {
            coEvery { refreshAuthUseCase.execute() } returns flow { throw IllegalStateException("error") }

            SUT.commandFlow.test {
                assertThat(awaitItem()).isInstanceOf(GeneralCommand.ShowLongToast::class.java)
            }
        }
    }

    @Test
    fun `Routes to application exit when auth refresh use case emits error result`() {
        runTest {
            coEvery { refreshAuthUseCase.execute() } returns flowOf(Err(DOMAIN_ERROR_UNKNOWN))

            SUT.routeFlow.test {
                assertThat(awaitItem()).isInstanceOf(SplashRoute.Exit::class.java)
            }
        }
    }

    @Test
    fun `Routes to application exit when auth refresh use case throws error`() {
        runTest {
            coEvery { refreshAuthUseCase.execute() } returns flow { throw IllegalStateException("error") }

            SUT.routeFlow.test {
                assertThat(awaitItem()).isInstanceOf(SplashRoute.Exit::class.java)
            }
        }
    }
}
