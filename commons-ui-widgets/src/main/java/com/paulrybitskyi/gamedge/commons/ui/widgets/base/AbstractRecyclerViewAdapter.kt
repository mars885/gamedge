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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.paulrybitskyi.commons.ktx.layoutInflater
import com.paulrybitskyi.commons.utils.observeChanges
import kotlin.reflect.KClass


private inline class ViewType(val type: Int)


abstract class AbstractRecyclerViewAdapter<IT: Item<*, in Dependencies>, Dependencies: ItemDependencies>(
    context: Context,
    var items: List<IT> = emptyList(),
    dependencies: ItemDependencies = NoDependencies
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val inflater = context.layoutInflater
    private val viewHolderFactories = mutableMapOf<ViewType, ViewHolderFactory>()

    var dependencies by observeChanges(dependencies) { _, _ -> notifyDataSetChanged() }

    var listenerBinder: ListenerBinder<IT>? = null


    init {
        items.extractViewHolderFactories()
    }


    fun setItems(items: List<IT>, diff: DiffUtil.DiffResult? = null) {
        this.items = items

        items.extractViewHolderFactories()

        diff?.dispatchUpdatesTo(this) ?: notifyDataSetChanged()
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


    override fun getItemCount(): Int {
        return items.size
    }


    override fun getItemViewType(position: Int): Int {
        return this[position]::class.toViewType().type
    }


    operator fun get(position: Int): IT {
        return items[position]
    }


    private fun KClass<out Item<*, *>>.toViewType(): ViewType {
        return ViewType(this.qualifiedName.hashCode())
    }


}