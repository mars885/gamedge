package com.paulrybitskyi.gamedge.feature.dashboard

import androidx.fragment.app.Fragment
import com.paulrybitskyi.gamedge.feature.dashboard.adapter.DashboardAdapterFragmentFactory
import com.paulrybitskyi.gamedge.feature.dashboard.adapter.DashboardViewPagerAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
internal object DashboardModule {


    @Provides
    fun provideDashboardViewPagerAdapter(
        fragment: Fragment,
        fragmentFactory: DashboardAdapterFragmentFactory
    ): DashboardViewPagerAdapter {
        return DashboardViewPagerAdapter(
            fragment = fragment,
            fragmentFactory = fragmentFactory
        )
    }


}