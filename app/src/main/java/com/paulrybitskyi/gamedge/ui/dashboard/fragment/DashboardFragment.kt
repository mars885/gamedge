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

package com.paulrybitskyi.gamedge.ui.dashboard.fragment

import android.view.MenuItem
import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.ktx.applyWindowTopInsetAsPadding
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.material.utils.setItemColors
import com.paulrybitskyi.commons.utils.viewBinding
import com.paulrybitskyi.gamedge.R
import com.paulrybitskyi.gamedge.databinding.FragmentDashboardBinding
import com.paulrybitskyi.gamedge.ui.base.BaseFragment
import com.paulrybitskyi.gamedge.ui.dashboard.fragment.DashboardPage.Companion.toDashboardPageFromMenuItemId

internal class DashboardFragment : BaseFragment<
    FragmentDashboardBinding,
    DashboardViewModel
>(R.layout.fragment_dashboard) {


    override val viewBinding by viewBinding(FragmentDashboardBinding::bind)
    override val viewModel by viewModels<DashboardViewModel>()

    private lateinit var viewPagerAdapter: DashboardViewPagerAdapter


    override fun onInit() {
        super.onInit()

        initToolbar()
        initBottomNavigation()
        initViewPager()
    }


    private fun initToolbar() {
        viewBinding.toolbar.applyWindowTopInsetAsPadding()
    }


    private fun initBottomNavigation() = with(viewBinding.bottomNav) {
        setItemColors(
            unselectedStateColor = getColor(R.color.bottom_navigation_item_color_state_unselected),
            selectedStateColor = getColor(R.color.bottom_navigation_item_color_state_selected)
        )
        setOnNavigationItemSelectedListener(::onNavigationItemSelected)
    }


    private fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        viewBinding.viewPager.setCurrentItem(menuItem.toPagePosition(), false)
        return true
    }


    private fun MenuItem.toPagePosition(): Int {
        return itemId.toDashboardPageFromMenuItemId().position
    }


    private fun initViewPager() = with(viewBinding.viewPager) {
        // Has to be set to stop ViewPager2 from crashing due to Fragment's
        // view preservation until onDestroy is called
        isSaveEnabled = false
        adapter = initAdapter()
        offscreenPageLimit = viewPagerAdapter.itemCount
        isUserInputEnabled = false
    }


    private fun initAdapter(): DashboardViewPagerAdapter {
        return DashboardViewPagerAdapter(this@DashboardFragment)
            .also { viewPagerAdapter = it }
    }


}