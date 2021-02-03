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

package com.paulrybitskyi.gamedge.core.providers

import android.content.Context
import android.graphics.drawable.Drawable
import com.paulrybitskyi.commons.ktx.getCompatDrawable
import com.paulrybitskyi.hiltbinder.BindType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


interface DrawableProvider {

    fun getDrawable(id: Int): Drawable?

}


@BindType
internal class DrawableProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DrawableProvider {


    override fun getDrawable(id: Int): Drawable? {
        return context.getCompatDrawable(id)
    }


}