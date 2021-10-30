package com.paulrybitskyi.gamedge.dashboard

import com.paulrybitskyi.gamedge.feature.dashboard.fragment.adapter.DashboardAdapterFragmentFactory
import com.paulrybitskyi.gamedge.feature.discovery.GamesDiscoveryFragment
import com.paulrybitskyi.gamedge.feature.likes.LikedGamesFragment
import com.paulrybitskyi.gamedge.feature.news.GamingNewsFragment
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

@BindType(installIn = BindType.Component.FRAGMENT)
internal class DashboardAdapterFragmentFactoryImpl @Inject constructor() :
    DashboardAdapterFragmentFactory {

    override fun createNewsFragment() = GamingNewsFragment()
    override fun createDiscoveryFragment() = GamesDiscoveryFragment()
    override fun createLikesFragment() = LikedGamesFragment()

}
