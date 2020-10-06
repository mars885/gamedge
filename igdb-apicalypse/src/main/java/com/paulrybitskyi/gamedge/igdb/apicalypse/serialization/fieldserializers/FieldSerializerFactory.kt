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

package com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.fieldserializers

import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.fieldserializers.concrete.CompositeFieldSerializer
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.fieldserializers.concrete.SingleFieldSerializerImpl

internal object FieldSerializerFactory {


    fun create(
        fieldChain: List<String>,
        children: List<FieldSerializer>
    ): FieldSerializer {
        return if(children.isEmpty()) {
            SingleFieldSerializerImpl(fieldChain)
        } else {
            CompositeFieldSerializer(children)
        }
    }


}