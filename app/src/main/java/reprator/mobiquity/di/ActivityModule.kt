package reprator.mobiquity.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import reprator.mobiquity.implementation.MobiquityAppNavigatorImpl
import reprator.mobiquity.navigation.AppNavigator

@InstallIn(ActivityComponent::class)
@Module
class ActivityModule {

    @Provides
    fun provideWillyNavigator(): AppNavigator {
        return MobiquityAppNavigatorImpl()
    }

}