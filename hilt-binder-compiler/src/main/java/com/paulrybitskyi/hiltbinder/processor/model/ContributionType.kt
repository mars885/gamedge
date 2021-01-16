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

package com.paulrybitskyi.hiltbinder.processor.model

import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement


internal sealed class ContributionType {

    object Set : ContributionType()

    class Map(val keyType: MapKeyType): ContributionType()

}


internal sealed class MapKeyType {

    class Standard(val value: StandardValue): MapKeyType()

    class Custom(val mirror: AnnotationMirror): MapKeyType()

}


internal sealed class StandardValue {

    class Int(val value: kotlin.Int): StandardValue()

    class Long(val value: kotlin.Long): StandardValue()

    class String(val value: kotlin.String): StandardValue()

    class Class(val value: TypeElement): StandardValue()

}