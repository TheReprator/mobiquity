package reprator.mobiquity.di

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import reprator.mobiquity.implementation.PermissionHelperImpl
import reprator.mobiquity.R
import reprator.mobiquity.implementation.SettingPreferenceManagerImpl
import reprator.mobiquity.base.SaveSettingPreferenceManager
import reprator.mobiquity.base.SettingPreferenceManager
import reprator.mobiquity.base.util.AppCoroutineDispatchers
import reprator.mobiquity.base.util.ConnectionDetector
import reprator.mobiquity.base.util.DateUtils
import reprator.mobiquity.base_android.PermissionHelper
import reprator.mobiquity.implementation.DateUtilsImpl
import reprator.mobiquity.util.AppCoroutineDispatchersImpl
import reprator.mobiquity.util.connectivity.InternetChecker
import java.util.concurrent.Executors
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideAppPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    @Provides
    fun provideSettingPreferenceManager(sharedPreferences: SharedPreferences):
            SettingPreferenceManager {
        return SettingPreferenceManagerImpl(sharedPreferences)
    }

    @Provides
    fun provideSaveSettingPreferenceManager(sharedPreferences: SharedPreferences):
            SaveSettingPreferenceManager {
        return SettingPreferenceManagerImpl(sharedPreferences)
    }

    @Provides
    fun provideLifeCycle(): Lifecycle {
        return ProcessLifecycleOwner.get().lifecycle
    }

    @Provides
    fun providePermissionHelper(): PermissionHelper {
        return PermissionHelperImpl()
    }

    @Provides
    fun provideLifetimeScope(lifecycle: Lifecycle): CoroutineScope {
        return lifecycle.coroutineScope
    }

    @Provides
    fun provideCoroutineDispatcherProvider(): AppCoroutineDispatchers {
        return AppCoroutineDispatchersImpl(
                Dispatchers.Main, Dispatchers.IO, Dispatchers.IO, Dispatchers.Default,
                Executors.newFixedThreadPool(1).asCoroutineDispatcher()
        )
    }

    @Singleton
    @Provides
    fun provideConnectivityChecker(
            @ApplicationContext context: Context, lifecycle: Lifecycle
    ): ConnectionDetector {
        return InternetChecker(context, lifecycle)
    }

    @Singleton
    @Provides
    fun provideDateUtils(
    ): DateUtils {
        return DateUtilsImpl()
    }
}