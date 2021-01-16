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

package com.paulrybitskyi.hiltbinder.processor.providers

import com.paulrybitskyi.hiltbinder.BindType

// For some unknown reason, multiline string literals are misaligned
// in the build console. Standard strings work fine.
internal class MessageProvider {


    fun undefinedReturnTypeError(): String {
        return "Cannot determine a return type of the @${BindType::class.simpleName} binding. " +
            "Forgot to specify it explicitly?"
    }


    fun noSubtypeRelationError(bindingTypeName: String, returnTypeName: String): String {
        return "@${BindType::class.simpleName}-using type, $bindingTypeName, is not " +
            "a subtype of the $returnTypeName class."
    }


    fun duplicatedComponentError(): String {
        return "@${BindType::class.simpleName}-using type specifies the component both " +
            "by the scope annotation and in the 'installIn' field of the annotation."
    }


    fun noMapKeyError(): String {
        return "@${BindType::class.simpleName}-using type is contributed to a multibound map, " +
            "but does not have a @MapKey annotation."
    }


    fun qualifierAbsentError(): String {
        return "@${BindType::class.simpleName}-using type is specified to have a qualifier, " +
            "but does not have one."
    }


}