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

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.utils.observeChanges
import kotlin.reflect.KClass


private inline class ViewType(val type: Int)


abstract class AbstractRecyclerViewAdapter<IT: Item<*, in Dependencies>, Dependencies: ItemDependencies>(
    context: Context,
    items: List<IT> = emptyList(),
    dependencies: ItemDependencies = NoDependencies
) : ListAdapter<IT, RecyclerView.ViewHolder>(ItemDiffCallback()) {


    private val inflater = context.layoutInflater
    private val viewHolderFactories = mutableMapOf<ViewType, ViewHolderFactory>()

    var dependencies by observeChanges(dependencies) { _, _ -> notifyDataSetChanged() }

    var listenerBinder: ListenerBinder<IT>? = null


    init {
        initItems(items)
    }


    private fun initItems(items: List<IT>) {
        if(items.isNotEmpty()) {
            submitList(items)
        }
    }


    override fun submitList(list: List<IT>?) {
        list?.extractViewHolderFactories()

        super.submitList(list)
    }


    override fun submitList(list: List<IT>?, commitCallback: Runnable?) {
        list?.extractViewHolderFactories()

        super.submitList(list, commitCallback)
    }


    private fun List<IT>.extractViewHolderFactories() {
        viewHolderFactories.clear()

        for(item in this) {
            val viewType = item::class.toViewType()

            if(viewHolderFactories[viewType] == null) {
                viewHolderFactories[viewType] = item
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return viewHolderFactories[ViewType(viewType)]?.create(inflater, parent, dependencies)
            ?: throw RuntimeException("The ViewHolder factory was not found.")
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(this[position]) {
            bind(holder, dependencies)

            if(holder is HasListeners) {
                listenerBinder?.invoke(this, holder)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return this[position]::class.toViewType().type
    }


    operator fun get(position: Int): IT {
        return currentList[position]
    }


    private fun KClass<out Item<*, *>>.toViewType(): ViewType {
        return ViewType(this.qualifiedName.hashCode())
    }


    override fun getItemId(position: Int): Long {
        return this[position].itemId
    }


}