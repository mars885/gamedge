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

package com.paulrybitskyi.gamedge.database.commons

import com.paulrybitskyi.gamedge.database.commons.di.qualifiers.Database
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import javax.inject.Inject

internal class JsonConverter @Inject constructor(@Database private val moshi: Moshi) {


    inline fun <reified T> toJson(clazz: T): String {
        return moshi.adapter(T::class.java).toJson(clazz)
    }


    inline fun <reified T> fromJson(json: String): T? {
        return moshi.adapter(T::class.java).fromJson(json)
    }


    inline fun <reified T> fromJsonList(json: String): List<T>? {
        val type = Types.newParameterizedType(List::class.java, T::class.java)

        return moshi.adapter<List<T>>(type).fromJson(json)
    }


    inline fun <reified K, reified V> fromJsonMap(json: String): Map<K, V>? {
        val type = Types.newParameterizedType(Map::class.java, K::class.java, V::class.java)

        return moshi.adapter<Map<K, V>>(type).fromJson(json)
    }


}