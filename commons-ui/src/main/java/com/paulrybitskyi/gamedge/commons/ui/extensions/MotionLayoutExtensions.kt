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

package com.paulrybitskyi.gamedge.commons.ui.extensions

import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet


inline fun MotionLayout.updateConstraintSets(action: (Int, ConstraintSet) -> Unit) {
    for(constraintSetId in constraintSetIds) {
        updateState(
            constraintSetId,
            getConstraintSet(constraintSetId).also { action(constraintSetId, it) }
        )
    }
}


inline fun MotionLayout.addTransitionListener(
    crossinline onTransitionStarted: (startId: Int, endId: Int) -> Unit = { _, _ -> },
    crossinline onTransitionChange: (startId: Int, endId: Int, progress: Float) -> Unit = { _, _, _ -> },
    crossinline onTransitionCompleted: (currentId: Int) -> Unit = {},
    crossinline onTransitionTrigger: (triggerId: Int, positive: Boolean, progress: Float) -> Unit = { _, _, _ -> }
): MotionLayout.TransitionListener {
    return object : MotionLayout.TransitionListener {

        override fun onTransitionStarted(ml: MotionLayout, startId: Int, endId: Int) {
            onTransitionStarted(startId, endId)
        }

        override fun onTransitionChange(ml: MotionLayout, startId: Int, endId: Int, progress: Float) {
            onTransitionChange(startId, endId, progress)
        }

        override fun onTransitionCompleted(ml: MotionLayout, currentId: Int) {
            onTransitionCompleted(currentId)
        }

        override fun onTransitionTrigger(
            ml: MotionLayout,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) {
            onTransitionTrigger(triggerId, positive, progress)
        }

    }
    .also(::addTransitionListener)
}