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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


typealias ListenerBinder<IT> = (IT, RecyclerView.ViewHolder) -> Unit


interface ViewHolderFactory {

    fun create(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: ItemDependencies
    ): RecyclerView.ViewHolder

}


interface Bindable {

    fun bind(
        viewHolder: RecyclerView.ViewHolder,
        dependencies: ItemDependencies
    )

}


interface HasUniqueIdentifier<out T> {

    val uniqueIdentifier: T

}


interface HasListeners


interface ItemDependencies


object NoDependencies: ItemDependencies


interface Item<Model: Any, Dependencies: ItemDependencies> : ViewHolderFactory, Bindable {

    val model: Model

    val itemId: Long
        get() = RecyclerView.NO_ID

}


abstract class AbstractItem<
    Model: Any,
    ViewHolder: RecyclerView.ViewHolder,
    Dependencies: ItemDependencies
>(final override val model: Model) : Item<Model, Dependencies> {


    @Suppress("unchecked_cast")
    final override fun create(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: ItemDependencies
    ) : RecyclerView.ViewHolder {
        return createViewHolder(
            inflater = inflater,
            parent = parent,
            dependencies = (dependencies as Dependencies)
        )
    }


    protected abstract fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        dependencies: Dependencies
    ) : ViewHolder


    @Suppress("unchecked_cast")
    final override fun bind(viewHolder: RecyclerView.ViewHolder, dependencies: ItemDependencies) {
        performBinding(
            viewHolder = (viewHolder as ViewHolder),
            dependencies = (dependencies as Dependencies)
        )
    }


    protected abstract fun performBinding(viewHolder: ViewHolder, dependencies: Dependencies)


    final override fun equals(other: Any?): Boolean {
        return (model == (other as? Item<*, *>)?.model)
    }


    final override fun hashCode(): Int {
        return model.hashCode()
    }


    final override fun toString(): String {
        return model.toString()
    }


}