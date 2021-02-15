package reprator.mobiquity.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import reprator.mobiquity.base.util.PermissionHelper
import reprator.mobiquity.implementation.MobiquityAppNavigatorImpl
import reprator.mobiquity.implementation.PermissionHelperImpl
import reprator.mobiquity.navigation.AppNavigator

@InstallIn(ActivityComponent::class)
@Module
class ActivityModule {

    @ActivityScoped
    @Provides
    fun providePermissionHelper(@ActivityContext context: Context): PermissionHelper {
        return PermissionHelperImpl(context)
    }

    @ActivityScoped
    @Provides
    fun provideWillyNavigator(): AppNavigator {
        return MobiquityAppNavigatorImpl()
    }
}