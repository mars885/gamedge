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

package com.paulrybitskyi.gamedge.image.loading

import android.widget.ImageView
import com.paulrybitskyi.hiltbinder.BindType
import com.paulrybitskyi.gamedge.image.loading.utils.PicassoTransformation
import com.paulrybitskyi.gamedge.image.loading.utils.into
import com.squareup.picasso.Picasso
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class ImageLoaderImpl @Inject constructor(
    private val picasso: Picasso
): ImageLoader {


    override fun loadImage(config: Config) {
        config.onStart?.invoke()

        val requestCreator = picasso.load(config.imageUrl)

        if(config.shouldCenterCrop) requestCreator.centerCrop()
        if(config.shouldCenterInside) requestCreator.centerInside()
        if(config.shouldFit) requestCreator.fit()
        if(config.hasTargetSize) requestCreator.resize(config.targetWidth, config.targetHeight)

        config.progressDrawable?.let(requestCreator::placeholder)
        config.errorDrawable?.let(requestCreator::error)

        if(config.hasTransformations) {
            config.transformations
                .map(::PicassoTransformation)
                .let { requestCreator.transform(it) }
        }

        if(config.hasAtLeastOneResultListener) {
            requestCreator.into(
                target = config.target,
                onSuccess = config.onSuccess,
                onFailure = config.onFailure
            )
        } else {
            requestCreator.into(config.target)
        }
    }


    override fun cancelRequests(target: ImageView) {
        picasso.cancelRequest(target)
    }


}