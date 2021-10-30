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

package com.paulrybitskyi.gamedge.commons.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.paulrybitskyi.commons.ktx.detachFromParent

class ViewPager2Container @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    lateinit var viewPager: ViewPager2
        private set


    init {
        initViewPager()
    }


    private fun initViewPager() {
        viewPager = ViewPager2(context)
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        addView(
            viewPager,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        )
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        // Due to the fact that the Navigation Library (as of version 2.3.3) doesn't provide a way
        // to specify the Fragment View Handling Mode [RECREATE/RETAIN] (for the fragments put into stack),
        // certain steps need to be taken on our side in order to achieve the Fragment View retention;
        // as suggested by the library developer, we must handle it ourselves
        // on the side of the Fragments; but here's a catch, if we do that, due to the way
        // the library handles the Fragment View attachment/detachment the ViewPager widget gets
        // negatively affected; when the ViewPager gets re-attached to the window, its
        // children (i.e. Fragment Views) get re-associated with the parent container, even
        // though they've been associated with it previously, which causes an exception.
        // So, in order to prevent this exception, we must ensure that ViewPager's
        // children get detached from their parent container when the ViewPager gets detached
        // from the window. This is only applicable to the ViewPager widgets that are used
        // in conjunction with the Navigation Library.
        viewPager.detachFromParent()
    }


}
