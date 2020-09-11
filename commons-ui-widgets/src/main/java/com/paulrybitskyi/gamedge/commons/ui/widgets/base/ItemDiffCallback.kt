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

package com.paulrybitskyi.gamedge.commons.ui.widgets.base

import androidx.recyclerview.widget.DiffUtil
import kotlin.reflect.full.isSubclassOf


fun <IT : Item<*, *>> calculateDiff(
    oldItems : List<IT>,
    newItems : List<IT>
) : DiffUtil.DiffResult {
    return DiffUtil.calculateDiff(
        ItemDiffCallback(
            oldItems = oldItems,
            newItems = newItems
        )
    )
}


class ItemDiffCallback<IT : Item<*, *>>(
    private val oldItems : List<IT>,
    private val newItems : List<IT>
) : DiffUtil.Callback() {


    override fun getOldListSize() : Int {
        return oldItems.size
    }


    override fun getNewListSize() : Int {
        return newItems.size
    }


    override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) : Boolean {
        return (oldItems[oldItemPosition] == newItems[newItemPosition])
    }


    override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) : Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return when {
            ((oldItem::class.isSubclassOf(newItem::class) &&
            (oldItem is HasUniqueIdentifier<*>) &&
            (newItem is HasUniqueIdentifier<*>))) -> (oldItem.uniqueIdentifier == newItem.uniqueIdentifier)
            else -> (oldItem == newItem)
        }
    }


}