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

package com.paulrybitskyi.gamedge.core.sharers

import android.content.Context
import android.content.Intent
import com.paulrybitskyi.gamedge.core.R
import com.paulrybitskyi.gamedge.core.providers.StringProvider
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

interface TextSharer : ContentSharer<String>

@BindType
internal class TextSharerImpl @Inject constructor(
    private val stringProvider: StringProvider,
) : TextSharer {

    private companion object {
        private const val DATA_TYPE_TEXT = "text/*"
    }

    override fun share(context: Context, content: String) {
        val textSharingIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = DATA_TYPE_TEXT
            putExtra(Intent.EXTRA_TEXT, content)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(
            Intent.createChooser(
                textSharingIntent,
                stringProvider.getString(R.string.action_share_via),
            ),
        )
    }
}
