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

package com.paulrybitskyi.gamedge.feature.dashboard.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import androidx.fragment.app.viewModels
import com.paulrybitskyi.commons.ktx.applyWindowBottomInsetAsMargin
import com.paulrybitskyi.commons.ktx.applyWindowTopInsetAsPadding
import com.paulrybitskyi.commons.ktx.getColor
import com.paulrybitskyi.commons.ktx.getSerializable
import com.paulrybitskyi.commons.material.utils.setItemColors
import com.paulrybitskyi.commons.utils.viewBinding
import com.paulrybitskyi.gamedge.commons.ui.base.BaseFragment
import com.paulrybitskyi.gamedge.commons.ui.base.events.Route
import com.paulrybitskyi.gamedge.feature.dashboard.R
import com.paulrybitskyi.gamedge.feature.dashboard.fragment.DashboardPage.Companion.toDashboardPageFromMenuItemId
import com.paulrybitskyi.gamedge.feature.dashboard.fragment.adapter.DashboardViewPagerAdapter
import com.paulrybitskyi.gamedge.feature.dashboard.fragment.adapter.DashboardViewPagerAdapterFactory
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

        private const val KEY_ADAPTER_STATE = "adapter_state"
        private const val KEY_SELECTED_PAGE = "selected_page"

        private val DEFAULT_SELECTED_PAGE = DashboardPage.DISCOVER

    }


    override val viewBinding by viewBinding(FragmentDashboardBinding::bind)
    override val viewModel by viewModels<DashboardViewModel>()

    private lateinit var viewPagerAdapter: DashboardViewPagerAdapter

    @Inject lateinit var viewPagerAdapterFactory: DashboardViewPagerAdapterFactory


    override fun onInit() {
        super.onInit()

        initToolbar()
        initBottomNavigation()
        initViewPager()
    }


    private fun initToolbar() = with(viewBinding.toolbar) {
        applyWindowTopInsetAsPadding()
        onRightButtonClickListener = { viewModel.onToolbarRightButtonClicked() }
    }


    private fun initBottomNavigation() = with(viewBinding.bottomNav) {
        applyWindowBottomInsetAsMargin()
        setItemColors(
            unselectedStateColor = getColor(R.color.bottom_navigation_item_color_state_unselected),
            selectedStateColor = getColor(R.color.bottom_navigation_item_color_state_selected)
        )
        setOnNavigationItemSelectedListener(::onNavigationItemSelected)
    }


    private fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        viewBinding.viewPagerContainer.viewPager.setCurrentItem(
            menuItem.toPagePosition(),
            false
        )

        return true
    }


    private fun MenuItem.toPagePosition(): Int {
        return itemId.toDashboardPageFromMenuItemId().position
    }


    private fun initViewPager() = with(viewBinding.viewPagerContainer.viewPager) {
        adapter = initViewPagerAdapter()
        offscreenPageLimit = viewPagerAdapter.itemCount
        isUserInputEnabled = false
    }


    private fun initViewPagerAdapter(): DashboardViewPagerAdapter {
        return viewPagerAdapterFactory.createAdapter(this)
            .also { viewPagerAdapter = it }
    }


    override fun onPostInit() {
        super.onPostInit()

        selectPage(DEFAULT_SELECTED_PAGE)
    }


    private fun selectPage(page: DashboardPage) = with(viewBinding) {
        bottomNav.selectedItemId = page.menuItemId
        viewPagerContainer.viewPager.setCurrentItem(page.position, false)
    }


    override fun onRoute(route: Route) {
        super.onRoute(route)

        when(route) {
            is DashboardRoute.Search -> navigator.goToSearch()
        }
    }


    override fun onRestoreState(state: Bundle) {
        super.onRestoreState(state)

        // Restoring the adapter's state since for some reason it does not do this by itself
        state.getParcelable<Parcelable>(KEY_ADAPTER_STATE)?.let(viewPagerAdapter::restoreState)
        selectPage(state.getSerializable(KEY_SELECTED_PAGE, DEFAULT_SELECTED_PAGE))
    }


    override fun onSaveState(state: Bundle) {
        super.onSaveState(state)

        // Saving the adapter's state since for some reason it does not do this by itself
        state.putParcelable(KEY_ADAPTER_STATE, viewPagerAdapter.saveState())
        state.putSerializable(
            KEY_SELECTED_PAGE,
            viewBinding.bottomNav.selectedItemId.toDashboardPageFromMenuItemId()
        )
    }


}