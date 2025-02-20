/*
 * Copyright 2020 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.core.utils

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import com.paulrybitskyi.commons.window.anims.WindowAnimations

internal fun CustomTabsIntent.Builder.setAnimations(
    context: Context,
    windowAnimations: WindowAnimations,
): CustomTabsIntent.Builder {
    setStartAnimations(
        context,
        windowAnimations.windowBEnterAnimation,
        windowAnimations.windowAExitAnimation,
    )
    setExitAnimations(
        context,
        windowAnimations.windowAEnterAnimation,
        windowAnimations.windowBExitAnimation,
    )

    return this
}
