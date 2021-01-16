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

package com.paulrybitskyi.hiltbinder.processor.generator

import com.paulrybitskyi.hiltbinder.processor.model.StandardValue
import com.paulrybitskyi.hiltbinder.processor.utils.toClassName
import com.squareup.javapoet.AnnotationSpec


internal fun StandardValue.toDaggerMapKeyAnnotationSpec(): AnnotationSpec {
    return when(this) {
        is StandardValue.Int -> createDaggerMapIntKeyAnnoSpec()
        is StandardValue.Long -> createDaggerMapLongKeyAnnoSpec()
        is StandardValue.String -> createDaggerMapStringKeyAnnoSpec()
        is StandardValue.Class -> createDaggerMapClassKeyAnnoSpec()
    }
}


internal fun StandardValue.Int.createDaggerMapIntKeyAnnoSpec(): AnnotationSpec {
    return AnnotationSpec.builder(DAGGER_TYPE_MAP_INT_KEY)
        .addMember("value", "\$L", value)
        .build()
}


internal fun StandardValue.Long.createDaggerMapLongKeyAnnoSpec(): AnnotationSpec {
    return AnnotationSpec.builder(DAGGER_TYPE_MAP_LONG_KEY)
        .addMember("value", "\$L", value)
        .build()
}


internal fun StandardValue.String.createDaggerMapStringKeyAnnoSpec(): AnnotationSpec {
    return AnnotationSpec.builder(DAGGER_TYPE_MAP_STRING_KEY)
        .addMember("value", "\$S", value)
        .build()
}


internal fun StandardValue.Class.createDaggerMapClassKeyAnnoSpec(): AnnotationSpec {
    return AnnotationSpec.builder(DAGGER_TYPE_MAP_CLASS_KEY)
        .addMember("value", "\$T.class", value.toClassName())
        .build()
}