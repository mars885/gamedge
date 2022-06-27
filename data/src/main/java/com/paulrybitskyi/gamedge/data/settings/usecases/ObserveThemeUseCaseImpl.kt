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

package com.paulrybitskyi.gamedge.data.settings.usecases

import com.paulrybitskyi.gamedge.domain.settings.entities.Theme
import com.paulrybitskyi.gamedge.domain.settings.usecases.ObserveSettingsUseCase
import com.paulrybitskyi.gamedge.domain.settings.usecases.ObserveThemeUseCase
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class ObserveThemeUseCaseImpl @Inject constructor(
    private val observeSettingsUseCase: ObserveSettingsUseCase,
) : ObserveThemeUseCase {

    override fun execute(): Flow<Theme> {
        return observeSettingsUseCase.execute()
            .map { settings -> settings.theme }
            .distinctUntilChanged()
    }
}
