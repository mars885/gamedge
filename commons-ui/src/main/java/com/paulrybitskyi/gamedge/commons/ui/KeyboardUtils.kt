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

package com.paulrybitskyi.gamedge.commons.ui

import android.view.View
import android.view.inputmethod.InputMethodManager
import com.paulrybitskyi.commons.ktx.getSystemService
import com.paulrybitskyi.commons.ktx.postActionDelayed


private const val KEYBOARD_SHOWING_DELAY = 300L


fun View.showKeyboard(withDelay: Boolean = false) {
    requestFocus()

    val action: (() -> Unit) = {
        context.getSystemService<InputMethodManager>().showSoftInput(this, 0)
    }

    if(withDelay) {
        postActionDelayed(KEYBOARD_SHOWING_DELAY, action)
    } else {
        action()
    }
}


fun View.hideKeyboard() {
    clearFocus()

    context.getSystemService<InputMethodManager>().hideSoftInputFromWindow(windowToken, 0)
}
