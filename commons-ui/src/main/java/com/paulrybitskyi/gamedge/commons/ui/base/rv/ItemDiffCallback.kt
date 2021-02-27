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

package com.paulrybitskyi.gamedge.commons.ui.base.rv

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class ItemDiffCallback<IT : Item<*, *>> : DiffUtil.ItemCallback<IT>() {


    override fun areItemsTheSame(oldItem: IT, newItem: IT): Boolean {
        return when {
            ((oldItem::class.java.isAssignableFrom(newItem::class.java) &&
            (oldItem is HasUniqueIdentifier<*>) &&
            (newItem is HasUniqueIdentifier<*>))) -> (oldItem.uniqueIdentifier == newItem.uniqueIdentifier)
            else -> (oldItem == newItem)
        }
    }


    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: IT, newItem: IT): Boolean {
        return (oldItem == newItem)
    }


}