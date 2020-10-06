/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.commons.ui.widgets.utils

import android.widget.TextView


/**
 * Should only be called after layout phase is finished,
 * otherwise returns false.
 */
fun TextView.isTextEllipsized(): Boolean {
    if(layout == null) return false

    val textLayout = checkNotNull(layout)
    val lineCount = textLayout.lineCount

    return ((lineCount > 0) && (textLayout.getEllipsisCount(lineCount - 1) > 0))
}