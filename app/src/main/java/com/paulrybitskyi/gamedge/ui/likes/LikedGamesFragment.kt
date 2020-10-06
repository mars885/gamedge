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

package com.paulrybitskyi.gamedge.ui.likes

import androidx.fragment.app.viewModels
import com.paulrybitskyi.gamedge.R
import com.paulrybitskyi.gamedge.core.utils.viewBinding
import com.paulrybitskyi.gamedge.databinding.FragmentLikedGamesBinding
import com.paulrybitskyi.gamedge.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class LikedGamesFragment : BaseFragment<
    FragmentLikedGamesBinding,
    LikedGamesViewModel
>(R.layout.fragment_liked_games) {


    override val viewBinding by viewBinding(FragmentLikedGamesBinding::bind)
    override val viewModel by viewModels<LikedGamesViewModel>()


    override fun onInit() {
        super.onInit()

        viewBinding.root
    }


}