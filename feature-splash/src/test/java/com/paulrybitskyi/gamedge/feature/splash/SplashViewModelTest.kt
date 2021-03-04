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

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.paulrybitskyi.gamedge.commons.ui.base.events.commons.GeneralCommand
import com.paulrybitskyi.gamedge.core.ErrorMapper
import com.paulrybitskyi.gamedge.core.Logger
import com.paulrybitskyi.gamedge.domain.auth.DomainOauthCredentials
import com.paulrybitskyi.gamedge.domain.auth.usecases.RefreshAuthUseCase
import com.paulrybitskyi.gamedge.domain.commons.DomainResult
import com.paulrybitskyi.gamedge.domain.commons.entities.Error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


private val DOMAIN_OAUTH_CREDENTIALS = DomainOauthCredentials(
    accessToken = "access_token",
    tokenType = "token_type",
    tokenTtl = 500L
)


internal class SplashViewModelTest {


    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var refreshAuthUseCase: FakeRefreshAuthUseCase
    private lateinit var logger: FakeLogger
    private lateinit var SUT: SplashViewModel


    @Before
    fun setup() {
        refreshAuthUseCase = FakeRefreshAuthUseCase()
        logger = FakeLogger()
        SUT = SplashViewModel(
            refreshAuthUseCase = refreshAuthUseCase,
            errorMapper = FakeErrorMapper(),
            logger = logger
        )
    }


    @Test
    fun `Routes to dashboard when auth refresh use case emits credentials`() {
        mainCoroutineRule.runBlockingTest {
            refreshAuthUseCase.shouldReturnCredentials = true

            SUT.init()

            val route = SUT.routeFlow.first()

            assertThat(route is SplashRoute.Dashboard).isTrue
        }
    }


    @Test
    fun `Logs error when auth refresh use case emits error result`() {
        refreshAuthUseCase.shouldReturnError = true

        SUT.init()

        assertThat(logger.errorMessage).isNotEmpty
    }


    @Test
    fun `Logs error when auth refresh use case throws error`() {
        refreshAuthUseCase.shouldThrowError = true

        SUT.init()

        assertThat(logger.errorMessage).isNotEmpty
    }


    @Test
    fun `Dispatches toast showing command when auth refresh use case emits error result`() {
        mainCoroutineRule.runBlockingTest {
            refreshAuthUseCase.shouldReturnError = true

            SUT.init()

            val command = SUT.commandFlow.first()

            assertThat(command is GeneralCommand.ShowLongToast).isTrue
        }
    }


    @Test
    fun `Dispatches toast showing command when auth refresh use cae throws error`() {
        mainCoroutineRule.runBlockingTest {
            refreshAuthUseCase.shouldThrowError = true

            SUT.init()

            val command = SUT.commandFlow.first()

            assertThat(command is GeneralCommand.ShowLongToast).isTrue
        }
    }


    @Test
    fun `Routes to application exit when auth refresh use case emits error result`() {
        mainCoroutineRule.runBlockingTest {
            refreshAuthUseCase.shouldReturnError = true

            SUT.init()

            val route = SUT.routeFlow.first()

            assertThat(route is SplashRoute.Exit).isTrue
        }
    }


    @Test
    fun `Routes to application exit when auth refresh use case throws error`() {
        mainCoroutineRule.runBlockingTest {
            refreshAuthUseCase.shouldThrowError = true

            SUT.init()

            val route = SUT.routeFlow.first()

            assertThat(route is SplashRoute.Exit).isTrue
        }
    }


    private class FakeRefreshAuthUseCase : RefreshAuthUseCase {

        var shouldReturnCredentials = false
        var shouldReturnError = false
        var shouldThrowError = false

        override suspend fun execute(params: Unit): Flow<DomainResult<DomainOauthCredentials>> {
            return when {
                shouldReturnCredentials -> flowOf(Ok(DOMAIN_OAUTH_CREDENTIALS))
                shouldReturnError -> flowOf(Err(Error.Unknown("unknown")))
                shouldThrowError -> flow { throw Exception("error") }

                else -> throw IllegalStateException()
            }
        }

    }


    private class FakeErrorMapper : ErrorMapper {

        override fun mapToMessage(error: Throwable): String {
            return "error"
        }

    }


    private class FakeLogger : Logger {

        var errorMessage = ""

        override fun info(tag: String, message: String, throwable: Throwable?) {}
        override fun debug(tag: String, message: String, throwable: Throwable?) {}
        override fun warning(tag: String, message: String, throwable: Throwable?) {}

        override fun error(tag: String, message: String, throwable: Throwable?) {
            errorMessage = message
        }

    }


}