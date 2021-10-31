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

package com.paulrybitskyi.gamedge.commons.ui.extensions

import android.widget.ImageView

private const val STATE_CHECKED = android.R.attr.state_checked
private const val STATE_CHECKED_ON = (STATE_CHECKED * 1)
private const val STATE_CHECKED_OFF = (STATE_CHECKED * -1)

var ImageView.isChecked: Boolean
    set(value) { setImageState(intArrayOf(if (value) STATE_CHECKED_ON else STATE_CHECKED_OFF), true) }
    get() = drawableState.contains(STATE_CHECKED_ON)
