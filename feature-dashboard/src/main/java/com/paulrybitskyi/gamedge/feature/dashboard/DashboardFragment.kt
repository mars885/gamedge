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

package com.paulrybitskyi.gamedge.feature.dashboard

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.ktx.applyWindowTopInsetAsPadding
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getSerializable
import com.paulrybitskyi.commons.material.utils.setItemColors
import com.paulrybitskyi.commons.utils.viewBinding
import com.paulrybitskyi.gamedge.commons.ui.base.BaseFragment
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.feature.dashboard.DashboardPage.Companion.toDashboardPageFromMenuItemId
import com.paulrybitskyi.gamedge.feature.dashboard.adapter.DashboardViewPagerAdapter
import com.paulrybitskyi.gamedge.feature.dashboard.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class DashboardFragment : BaseFragment<
    FragmentDashboardBinding,
    DashboardViewModel,
    DashboardNavigator
>(R.layout.fragment_dashboard) {


    private companion object {

        private const val KEY_SELECTED_PAGE = "selected_page"

        private val DEFAULT_SELECTED_PAGE = DashboardPage.DISCOVER

    }


    override val viewBinding by viewBinding(FragmentDashboardBinding::bind)
    override val viewModel by viewModels<DashboardViewModel>()

    @Inject lateinit var viewPagerAdapter: DashboardViewPagerAdapter


    override fun onInit() {
        super.onInit()

        initToolbar()
        initBottomNavigation()
        initViewPager()
    }


    private fun initToolbar() = with(viewBinding.toolbar) {
        onRightButtonClickListener = { viewModel.onToolbarRightButtonClicked() }
        applyWindowTopInsetAsPadding()
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
        adapter = viewPagerAdapter
        offscreenPageLimit = viewPagerAdapter.itemCount
        isUserInputEnabled = false
    }


    override fun onPostInit() {
        super.onPostInit()

        selectPage(DEFAULT_SELECTED_PAGE)
    }


    private fun selectPage(page: DashboardPage) = with(viewBinding) {
        bottomNav.selectedItemId = page.menuItemId
        viewPager.setCurrentItem(page.position, false)
    }


    override fun onRoute(route: Route) {
        super.onRoute(route)

        when(route) {
            is DashboardRoute.Search -> navigator.navigateToSearch()
        }
    }


    override fun onRestoreState(state: Bundle) {
        super.onRestoreState(state)

        selectPage(state.getSerializable(KEY_SELECTED_PAGE, DEFAULT_SELECTED_PAGE))
    }


    override fun onSaveState(state: Bundle) {
        state.putSerializable(
            KEY_SELECTED_PAGE,
            viewBinding.bottomNav.selectedItemId.toDashboardPageFromMenuItemId()
        )

        super.onSaveState(state)
    }


}