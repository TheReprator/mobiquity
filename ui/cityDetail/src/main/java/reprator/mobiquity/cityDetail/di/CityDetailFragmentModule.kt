package reprator.mobiquity.cityDetail.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import reprator.mobiquity.navigation.AppNavigator
import reprator.mobiquity.navigation.CityDetailNavigator

@InstallIn(FragmentComponent::class)
@Module
class CityDetailFragmentModule {

    @Provides
    fun provideCityDetailNavigator(
        appNavigator: AppNavigator
    ): CityDetailNavigator {
        return appNavigator
    }

}