package reprator.mobiquity.saveCity.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import reprator.mobiquity.navigation.AppNavigator
import reprator.mobiquity.navigation.SavedCityNavigator

@InstallIn(FragmentComponent::class)
@Module
class SaveCityFragmentModule {

    @Provides
    fun provideSavedCityNavigator(
        appNavigator: AppNavigator
    ): SavedCityNavigator {
        return appNavigator
    }
}