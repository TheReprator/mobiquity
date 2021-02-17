package reprator.mobiquity.cityDetail.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import reprator.mobiquity.base.MeasureMentUnitType
import reprator.mobiquity.base.SettingPreferenceManager
import reprator.mobiquity.base.util.AppCoroutineDispatchers
import reprator.mobiquity.base.util.ConnectionDetector
import reprator.mobiquity.base.util.DateUtils
import java.util.concurrent.Executors

@Module
@InstallIn(SingletonComponent::class)
class CityDetailTestModule {

    @Provides
    fun provideAppCoroutineDispatchers(
    ): AppCoroutineDispatchers {
        return AppCoroutineDispatchersImpl()
    }

    @Provides
    fun provideDateUtils(
    ): DateUtils {
        return DateUtilsImpl()
    }

    @Provides
    fun provideSettingPreferenceManager(
    ): SettingPreferenceManager {
        return SettingPreferenceManagerImpl()
    }

    @Provides
    fun provideConnectionDetector(
    ): ConnectionDetector {
        return ConnectionDetectorImpl()
    }
}

class AppCoroutineDispatchersImpl : AppCoroutineDispatchers {
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val computation: CoroutineDispatcher
        get() = Dispatchers.IO
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val singleThread: CoroutineDispatcher
        get() = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
}

class SettingPreferenceManagerImpl : SettingPreferenceManager {
    override val shouldClearSavedLocation: Boolean
        get() = false
    override val measureMentUnitType: MeasureMentUnitType
        get() = MeasureMentUnitType.STANDARD
}

class ConnectionDetectorImpl : ConnectionDetector {
    override val isInternetAvailable: Boolean
        get() = true
}